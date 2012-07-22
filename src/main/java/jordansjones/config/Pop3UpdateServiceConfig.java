package jordansjones.config;

import com.yammer.dropwizard.util.Duration;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 */
public class Pop3UpdateServiceConfig extends BaseServiceConfig implements UpdateServiceConfig {

	@JsonProperty
	private boolean enabled = true;

	@NotNull
	@JsonProperty
	private String username;

	@NotNull
	@JsonProperty
	private String password;

	@NotNull
	@NotEmpty
	@JsonProperty
	private String server = "pop.gmail.com";

	@JsonProperty
	private int port = 995;

	@JsonProperty
	private boolean useSSL = true;

	@JsonProperty
	private Duration socketTimeout = Duration.seconds(3);

	public boolean isEnabled() {
		return enabled;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(final String username) {
		this.username = checkNotNull(username);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = checkNotNull(password);
	}

	public String getServer() {
		return server;
	}

	public int getPort() {
		return port;
	}

	public boolean isUseSSL() {
		return useSSL;
	}

	public Duration getSocketTimeout() {
		return socketTimeout;
	}
}
