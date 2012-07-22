package jordansjones;

import com.yammer.dropwizard.client.JerseyClientConfiguration;
import com.yammer.dropwizard.config.Configuration;
import jordansjones.config.Pop3UpdateServiceConfig;
import jordansjones.config.TweetUpdateServiceConfig;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class ServiceConfiguration extends Configuration {

	@Valid
	@NotNull
	@JsonProperty
	private JerseyClientConfiguration httpClient = new JerseyClientConfiguration();

	@Valid
	@NotNull
	@JsonProperty
	private TweetUpdateServiceConfig tweetUpdateService = new TweetUpdateServiceConfig();

	@Valid
	@NotNull
	@JsonProperty
	private Pop3UpdateServiceConfig pop3UpdateService = new Pop3UpdateServiceConfig();

	public JerseyClientConfiguration getHttpClientConfiguration() {
		return httpClient;
	}

	public TweetUpdateServiceConfig getTweetUpdateServiceConfiguration() {
		return tweetUpdateService;
	}

	public Pop3UpdateServiceConfig getPop3UpdateServiceConfiguration() {
		return pop3UpdateService;
	}
}
