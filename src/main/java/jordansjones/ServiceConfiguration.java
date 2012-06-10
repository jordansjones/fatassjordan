package jordansjones;

import com.yammer.dropwizard.client.JerseyClientConfiguration;
import com.yammer.dropwizard.config.Configuration;
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
	private TweetUpdateServiceConfig updateService = new TweetUpdateServiceConfig();

	public JerseyClientConfiguration getHttpClientConfiguration() {
		return httpClient;
	}

	public TweetUpdateServiceConfig getUpdateServiceConfiguration() {
		return updateService;
	}
}
