package ro.bogdan.anrdetectorutils;


public class Trace {
	private AnrType type;
	private StackTraceElement[] stackTrace;
	private long timestamp;
	private String flattenedTrace;

	public Trace(AnrType type, StackTraceElement[] trace, long timestamp) {
		this.type = type;
		this.stackTrace = trace;
		this.timestamp = timestamp;
		this.flattenedTrace = flattenTrace();
	}

	private String flattenTrace() {
		String trace = "";
		String eol = System.getProperty("line.separator");
		for (StackTraceElement e : stackTrace) {
			trace += e.toString() + eol;
		}
		return trace;
	}

	public StackTraceElement[] getStackTrace() {
		return stackTrace;
	}

	@Override
	public String toString() {
		return "Trace type = " + type.toString() + ", trace = " + stackTrace[3];
	}

	public String getFlattenedTrace() {
		return flattenedTrace;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public AnrType getType() {
		return type;
	}

}
