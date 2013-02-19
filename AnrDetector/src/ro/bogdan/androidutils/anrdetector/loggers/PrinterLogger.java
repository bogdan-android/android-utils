package ro.bogdan.androidutils.anrdetector.loggers;

import ro.bogdan.anrdetectorutils.Trace;
import android.util.Log;

public class PrinterLogger implements AbstractLogger {
	private static final String TAG = "AnrDetector";

	@Override
	public void logANR(Trace trace, long timeout) {
		Log.d(TAG, "Detected ANR." + trace.getType().toString() + ", more than " + trace.getType().getWaitTime() + " ms,   Approx: "+timeout+" ms\n" + trace.getFlattenedTrace());
	}
}
