package jordansjones.resources;

import com.google.common.collect.Lists;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.yammer.dropwizard.logging.Log;
import jordansjones.api.StatusUpdate;
import jordansjones.core.Tweet;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Path("/tweets")
@Produces(MediaType.APPLICATION_JSON)
public class TweetsResource {

	private static final Log logger = Log.forClass(TweetsResource.class);

	private final DateTimeFormatter dateFormatter = DateTimeFormat.mediumDate();
	private final DateTimeFormatter timeFormatter = DateTimeFormat.mediumTime();

	private final ConcurrentHashMap<Long, StatusUpdate> statusUpdates = new ConcurrentHashMap<Long, StatusUpdate>();
	private final EventBus eventBus;

	@Inject
	public TweetsResource(EventBus eventBus) {
		this.eventBus = eventBus;
		this.eventBus.register(this);
	}

	@GET
	public Collection<StatusUpdate> get() {
		logger.info("Returning {} Status updates", statusUpdates.size());
		return statusUpdates.values();
	}

	@Subscribe
	@AllowConcurrentEvents
	public void handleNewTweets(Tweet[] tweets) {
		if (tweets == null)
			tweets = new Tweet[0];

		logger.info("{} new tweets on the event bus.", tweets.length);

		final List<StatusUpdate> updates = Lists.newArrayList();
		for (Tweet tweet : tweets) {
			final DateTime createdAt = tweet.getCreatedAt();
			final StatusUpdate statusUpdate = new StatusUpdate(
				tweet.getIdStr(),
				dateFormatter.print(createdAt),
				timeFormatter.print(createdAt),
				tweet.getText(),
				createdAt.toInstant().getMillis()
			);
			if (this.statusUpdates.putIfAbsent(tweet.getId(), statusUpdate) == null)
				updates.add(statusUpdate);
		}

		// TODO: Push status updates to the client
	}
}
