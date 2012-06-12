package jordansjones.api;

public class StatusUpdate {

	private final String id;
	private final String date;
	private final String time;
	private final String text;
	private final long timestamp;

	public StatusUpdate(final String id, final String date, final String time, final String text, final long timestamp) {
		this.id = id;
		this.date = date;
		this.time = time;
		this.text = text;
		this.timestamp = timestamp;
	}

	public String getId() {
		return id;
	}

	public String getDate() {
		return date;
	}

	public String getTime() {
		return time;
	}

	public String getText() {
		return text;
	}

	public long getTimestamp() {
		return timestamp;
	}
}
