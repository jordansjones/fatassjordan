package jordansjones.entities.oauth;

import org.codehaus.jackson.annotate.JsonSetter;

public class AccessToken {

	private String type;

	private String token;

	public String getType() {
		return type;
	}

	@JsonSetter("token_type")
	public void setType(final String type) {
		this.type = type;
	}

	public String getToken() {
		return token;
	}

	@JsonSetter("access_token")
	public void setToken(final String token) {
		this.token = token;
	}
}
