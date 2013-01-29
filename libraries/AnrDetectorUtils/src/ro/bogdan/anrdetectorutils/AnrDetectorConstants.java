package ro.bogdan.anrdetectorutils;

public interface AnrDetectorConstants {
	String INTENT_ACTION_ANR = "ro.bogdan.anr_detect_action";
	String EXTRA_ANR_TIMESTAMP = "ro.bogdan.anr_timestamp";
	String EXTRA_ANR_FLATTENED_TRACE = "ro.bogdan.anr_flattened_trace";
	String EXTRA_ANR_TYPE_STRING = "ro.bogdan.anr_anr_type_string";
	String ANR_TYPE_SMALL = "anr_type_small";
	String ANR_TYPE_MEDIUM = "anr_type_medium";
	String ANR_TYPE_LARGE = "anr_type_large";
}
