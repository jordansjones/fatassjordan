package jordansjones.api;

public class StatusUpdate {

	private final String id;
	private final String date;
	private final String time;
	private final float weight;
	private final long timestamp;

	public StatusUpdate(final String id, final String date, final String time, final float weight, final long timestamp) {
		this.id = id;
		this.date = date;
		this.time = time;
		this.weight = weight;
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

	public float getWeight() {
		return weight;
	}

	public String getFormattedWeight() {
		return String.format("%3.1f", this.weight);
	}

	public long getTimestamp() {
		return timestamp;
	}
}
