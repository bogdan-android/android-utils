package ro.bogdan.anrdetectorutils;

public enum AnrType {
	SMALL(1200, AnrDetectorConstants.ANR_TYPE_SMALL), MEDIUM(2400,
			AnrDetectorConstants.ANR_TYPE_MEDIUM), LARGE(3600,
			AnrDetectorConstants.ANR_TYPE_LARGE);
	private final long waitTime;
	private String name;

	AnrType(long waitTime, String name) {
		this.waitTime = waitTime;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public long getWaitTime() {
		return waitTime;
	}

	public static AnrType findAnrType(String stringRepresentation) {
		if (AnrDetectorConstants.ANR_TYPE_SMALL
				.equalsIgnoreCase(stringRepresentation)) {
			return SMALL;
		} else if (AnrDetectorConstants.ANR_TYPE_MEDIUM
				.equalsIgnoreCase(stringRepresentation)) {
			return MEDIUM;
		} else {
			//TODO fix defaulting to large.
			return LARGE;
		}
	}
}