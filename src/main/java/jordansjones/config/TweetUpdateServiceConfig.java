package jordansjones.config;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.validation.constraints.NotNull;

public class TweetUpdateServiceConfig extends BaseServiceConfig implements UpdateServiceConfig {

	@JsonProperty
	private boolean enabled = true;

	@NotNull
	@JsonProperty
	private String screenName;

	@NotNull
	@JsonProperty
	private String consumerKey;

	@NotNull
	@JsonProperty
	private String consumerSecret;

	public boolean isEnabled() {
		return enabled;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public String getConsumerKey() {
		return consumerKey;
	}

	public void setConsumerKey(final String consumerKey) {
		this.consumerKey = consumerKey;
	}

	public String getConsumerSecret() {
		return consumerSecret;
	}

	public void setConsumerSecret(final String consumerSecret) {
		this.consumerSecret = consumerSecret;
	}
}
