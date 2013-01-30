package ro.bogdan.androidutils.anrdetectorexplorer.db;

import ro.bogdan.anrdetectorutils.AnrType;

public class AnrLog {

	private long id;
	private long timestamp;
	private String trace;
	private String title;
	private AnrType type;

	public AnrLog(long id, String trace, String title, AnrType type, long timestamp) {
		this.id = id;
		this.trace = trace;
		this.type = type;
		this.title = title;
		this.timestamp = timestamp;
	}

	public AnrLog(String trace, String title, AnrType type, long timestamp) {
		this.id = -1;
		this.trace = trace;
		this.type = type;
		this.title = title;
		this.timestamp = timestamp;
	}

	public long getId() {
		return id;
	}

	public String getTrace() {
		return trace;
	}

	public String getTitle() {
		return title;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public AnrType getType() {
		return type;
	}

	@Override
	public String toString() {
		return "AnrLog: id = " + id + ", title = " + title + ", time: " + timestamp + ", type = " + type.getName();
	}
}
