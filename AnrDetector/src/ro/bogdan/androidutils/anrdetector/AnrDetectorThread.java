package ro.bogdan.androidutils.anrdetector;

import java.util.List;

import ro.bogdan.androidutils.anrdetector.Rule.RuleType;
import ro.bogdan.androidutils.anrdetector.loggers.AbstractLogger;
import ro.bogdan.anrdetectorutils.AnrType;
import ro.bogdan.anrdetectorutils.Trace;


public class AnrDetectorThread extends Thread {

	private boolean canRun;
	private long lastAckTimestamp;
	private Thread mainThread;
	private Trace savedTrace;
	private AbstractLogger logger;
	private Trace anrToLog;
	private List<Rule> rules;
	private Rule basePackageNameRule;

	public AnrDetectorThread(Thread mainThread, AbstractLogger logger) {
		this.logger = logger;
		this.mainThread = mainThread;
		lastAckTimestamp = System.currentTimeMillis();
	}

	@Override
	public void run() {
		super.run();
		while (canRun) {
			long now = System.currentTimeMillis();
			long offset = now - lastAckTimestamp;
			long waited = 0;
			if (offset > (AnrType.LARGE.getWaitTime() - AnrDetector.STUCK_TIME)) {
				waited = tryToLogAnr(AnrType.LARGE, now);
			} else if (offset > (AnrType.MEDIUM.getWaitTime() - AnrDetector.STUCK_TIME)) {
				waited = tryToLogAnr(AnrType.MEDIUM, now);
			} else if (offset > (AnrType.SMALL.getWaitTime() - AnrDetector.STUCK_TIME)) {
				waited = tryToLogAnr(AnrType.SMALL, now);
			}
			doAnrLogging();
			long stdWait = AnrDetector.PROBE_TIME / 2;
			long timeToSleep = waited > 0 ? ((stdWait - waited) > 0) ? stdWait - waited : 0 : stdWait;
			if (timeToSleep > 0) {
				try {
					Thread.sleep(timeToSleep);
				} catch (InterruptedException e) {
					// ignore
				}
			}
		}
	}

	private void doAnrLogging() {
		if (logger != null && anrToLog != null) {
			logger.logANR(anrToLog, System.currentTimeMillis() - anrToLog.getTimestamp() + anrToLog.getType().getWaitTime());
		}
		anrToLog = null;
	}

	private boolean areTracesEquals(StackTraceElement[] nowTrace, StackTraceElement[] stackTrace) {
		String nowTraceString = "";
		String targetTraceString = "";
		for (StackTraceElement e : nowTrace) {
			nowTraceString += e.toString();
		}
		for (StackTraceElement e : stackTrace) {
			targetTraceString += e.toString();
		}
		return nowTraceString.equals(targetTraceString);
	}

	/**
	 * Called only on worker thread
	 * 
	 * @param anrType
	 *            the anr type
	 * @param timestamp
	 *            timestamp of logging
	 */
	private long tryToLogAnr(AnrType anrType, long timestamp) {
		long waited = 0;
		Trace trace = new Trace(anrType, mainThread.getStackTrace(), timestamp);
		if (anrType == AnrType.LARGE) {
			if (savedTrace != null && savedTrace.getType() == AnrType.LARGE) {
				// ignore because it was logged before.
			} else {
				try {

					Thread.sleep(AnrDetector.STUCK_TIME);
					waited = AnrDetector.STUCK_TIME;
				} catch (InterruptedException e) {
					// ignore
				}
				if (areTracesEquals(mainThread.getStackTrace(), trace.getStackTrace()) && shouldBeConsidered(trace)) {
					logANR(trace);
					savedTrace = trace;
				} else {
					// not really a LARGE ANR, main thread progressed.
				}
			}
		} else {
			try {
				Thread.sleep(AnrDetector.STUCK_TIME);
				waited = AnrDetector.STUCK_TIME;
			} catch (InterruptedException e) {
				// ignore
			}
			if (areTracesEquals(mainThread.getStackTrace(), trace.getStackTrace()) && shouldBeConsidered(trace)) {
				savedTrace = trace;
			} else {
				// not really an anr, main thread progressed.
			}
		}
		return waited;
	}

	private boolean shouldBeConsidered(Trace trace) {
		if (rules != null && rules.size() > 0) {
			for (Rule rule : rules) {
				//we must match every rule to consider allowing
				if (!rule.matchesTrace(trace)) {
					return false;
				}
			}
		}
		if (basePackageNameRule != null && !basePackageNameRule.matchesTrace(trace)) {
			return false;
		}
		return true;
	}

	public void setRules(List<Rule> ignoreRules) {
		this.rules = ignoreRules;
	}
	
	public void setIgnoreNotFromPackage(String basePackageName) {
		this.basePackageNameRule = new Rule(RuleType.TRACE_CONTAINS, basePackageName);
	}

	private void logANR(Trace trace) {
		anrToLog = trace;
	}

	/**
	 * Will be invoked by main thread. Don't do anything blocking here.
	 */
	public void acknowledge() {
		lastAckTimestamp = System.currentTimeMillis();
		logAnrIfSaved();
		savedTrace = null;
	}

	private void logAnrIfSaved() {
		if (savedTrace != null && savedTrace.getType() != AnrType.LARGE) {
			// this trace was not saved. Large ones are saved directly.
			logANR(savedTrace);
		}
	}

	@Override
	public synchronized void start() {
		canRun = true;
		super.start();
	}

	public synchronized void stopThread() {
		canRun = false;
	}
}
