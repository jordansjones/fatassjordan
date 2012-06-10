package jordansjones.api;

public class StatusUpdate {

	private final String id;
	private final String date;
	private final String text;

	public StatusUpdate(final String id, final String date, final String text) {
		this.id = id;
		this.date = date;
		this.text = text;
	}

	public String getId() {
		return id;
	}

	public String getDate() {
		return date;
	}

	public String getText() {
		return text;
	}
}
