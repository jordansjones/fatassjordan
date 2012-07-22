package jordansjones.config;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.validation.constraints.NotNull;

public class TweetUpdateServiceConfig extends BaseServiceConfig implements UpdateServiceConfig {

	@JsonProperty
	private boolean enabled = true;

	@NotNull
	@JsonProperty
	private String screenName;

	public boolean isEnabled() {
		return enabled;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

}
