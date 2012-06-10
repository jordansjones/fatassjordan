package jordansjones.views;

import com.yammer.dropwizard.views.View;

public class IndexView extends View {

	private final String message;

	public IndexView(final String message) {
		super("index.ftl");

		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
