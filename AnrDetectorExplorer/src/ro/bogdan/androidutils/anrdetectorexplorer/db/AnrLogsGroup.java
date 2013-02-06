package ro.bogdan.androidutils.anrdetectorexplorer.db;

import java.util.List;

public class AnrLogsGroup {
	private String title;
	private List<AnrLog> anrLogs;
	private long timestamp;
	private String packageName;

	public AnrLogsGroup(List<AnrLog> anrLogs, String title, String packageName, long mostRecentTimestamp) {
		this.anrLogs = anrLogs;
		this.title = title;
		this.timestamp = mostRecentTimestamp;
		this.packageName = packageName;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public String getTitle() {
		return title;
	}

	public List<AnrLog> getAnrLogs() {
		return anrLogs;
	}

	public String getPackageName() {
		return packageName;
	}
}
