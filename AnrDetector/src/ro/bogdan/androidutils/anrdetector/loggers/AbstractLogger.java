package ro.bogdan.androidutils.anrdetector.loggers;

import ro.bogdan.anrdetectorutils.Trace;


public interface AbstractLogger {
	public void logANR(Trace trace, long timeout);
}
