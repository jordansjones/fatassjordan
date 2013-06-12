package jordansjones.core;

import com.google.common.base.Charsets;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.eventbus.EventBus;
import com.google.common.io.BaseEncoding;
import com.google.common.util.concurrent.AbstractScheduledService;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.yammer.dropwizard.client.JerseyClient;
import com.yammer.dropwizard.logging.Log;
import jordansjones.EnvKeys;
import jordansjones.config.TweetUpdateServiceConfig;
import jordansjones.entities.Tweet;
import jordansjones.entities.oauth.AccessToken;
import org.apache.http.HttpHost;
import org.apache.http.conn.params.ConnRoutePNames;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

public class TweetUpdateService extends AbstractScheduledService implements UpdateService {

	private static final Log logger = Log.forClass(TweetUpdateService.class);

	private final String UriBase = "https://api.twitter.com";

	private final TweetUpdateServiceConfig config;
	private final JerseyClient httpClient;
	private final EventBus eventBus;
	private UriBuilder endpointUriBuilder;
	private Optional<Tweet> recentTweet = Optional.absent();
	private AccessToken accessToken;

	@Inject
	public TweetUpdateService(final TweetUpdateServiceConfig config, final JerseyClient httpClient, final EventBus eventBus) {
		this.config = config;
		this.httpClient = httpClient;
		this.eventBus = eventBus;
		this.endpointUriBuilder = UriBuilder.fromPath(UriBase + "/1.1/statuses/user_timeline.json")
			.queryParam("screen_name", config.getScreenName())
			.queryParam("trim_user", true);



		final String proxyHost = System.getenv(EnvKeys.ENV_PROXY_HOST);
		final String proxyPort = System.getenv(EnvKeys.ENV_PROXY_PORT);
		if (!Strings.isNullOrEmpty(proxyHost) && !Strings.isNullOrEmpty(proxyPort)) {
			final HttpHost proxy = new HttpHost(proxyHost, Integer.valueOf(proxyPort));
			this.httpClient.getClientHandler().getHttpClient().getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		}
	}

	/**
	 * Start the service.
	 */
	@Override
	protected void startUp() throws Exception {
		final Charset utf8 = Charsets.UTF_8;
		final String preEncodedToken = String.format(
			"%s:%s",
			URLEncoder.encode(config.getConsumerKey(), utf8.name()),
			URLEncoder.encode(config.getConsumerSecret(), utf8.name())
		);
		final String credToken = BaseEncoding.base64Url().encode(preEncodedToken.getBytes(utf8));

		logger.info("Requesting OAuth token");

		final URI uri = UriBuilder.fromPath(UriBase + "/oauth2/token").build();
		final MultivaluedMapImpl entity = new MultivaluedMapImpl();
		entity.add("grant_type", "client_credentials");

		this.accessToken = this.httpClient
			.resource(uri)
			.type(new MediaType(MediaType.APPLICATION_FORM_URLENCODED_TYPE.getType(), MediaType.APPLICATION_FORM_URLENCODED_TYPE.getSubtype(), ImmutableMap.of("charset", "UTF-8")))
			.header("Authorization", formatAuthorizationHeader("Basic", credToken))
			.post(AccessToken.class, entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void runOneIteration() throws Exception {
		logger.info("Update Tweets");
		Tweet tweet = null;
		try {
			if (recentTweet.isPresent()) {
				tweet = recentTweet.get();
				this.endpointUriBuilder = this.endpointUriBuilder.replaceQueryParam("since_id", tweet.getId());
			}
		}
		catch (IllegalArgumentException iae) {
			logger.error(iae, "when trying to replace the query param");
			throw iae;
		}

		Tweet[] tweets;

		try {
			logger.info("Building endpoint URL");
			final URI uri = this.endpointUriBuilder.build();
			logger.info("Requesting new tweets");
			tweets = this.get(uri, accessToken.getToken(), MediaType.APPLICATION_JSON_TYPE, Tweet[].class);
		}
		catch (RuntimeException re) {
			logger.error(re, "Error loading tweets.");
			throw re;
		}
		finally {
			logger.info("Finished loading tweets.");
		}

		if (tweets == null)
			tweets = new Tweet[0];

		logger.info("{} tweets returned.", tweets.length);
		if (tweets.length > 0)
			eventBus.post(tweets);

		if (tweets.length > 0) {
			if (tweet == null)
				tweet = tweets[0];

			for (Tweet t : tweets) {
				if (tweet.getCreatedAt().isBefore(t.getCreatedAt()))
					tweet = t;
			}
		}

		this.recentTweet = Optional.fromNullable(tweet);
	}

	@Override
	protected Scheduler scheduler() {
		final long initialDelay = config.getInitialDelay().toSeconds();
		final long period = config.getPeriod().toSeconds();
		return Scheduler.newFixedRateSchedule(initialDelay, period, TimeUnit.SECONDS);
	}

	private String formatAuthorizationHeader (final String type, final String auth) {
		return String.format("%s %s", type, auth);
	}

	private <T> T get(final URI uri, final String authorization, final MediaType contentType, Class<T> returnType) {
		return this.httpClient
			.resource(uri)
			.header("Authorization", formatAuthorizationHeader("Bearer", authorization))
			.accept(contentType)
			.get(returnType);
	}

}
