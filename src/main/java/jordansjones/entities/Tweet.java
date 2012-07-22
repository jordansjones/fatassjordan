package jordansjones.entities;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonSetter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Tweet {

	// Sat Jun 09 01:46:33 +0000 2012
	private static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern("EEE MMM dd HH:mm:ss Z YYYY");

	@JsonProperty
	private long id;
	@JsonProperty
	private String id_str;

	private DateTime createdAt;
	@JsonProperty
	private String text;
	@JsonProperty
	private boolean truncated;

	@JsonSetter("created_at")
	public void setCreatedAt(final String createdAt) {
		this.createdAt = FORMATTER.parseDateTime(createdAt);
	}

	public long getId() {
		return id;
	}

	public String getIdStr() {
		return id_str;
	}

	public DateTime getCreatedAt() {
		return createdAt;
	}

	public String getText() {
		return text;
	}

	public boolean isTruncated() {
		return truncated;
	}
}
