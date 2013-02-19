package ro.bogdan.androidutils.anrdetector;

import java.util.List;

import ro.bogdan.androidutils.anrdetector.loggers.AbstractLogger;


import android.os.Handler;
import android.os.Looper;

/**
 * 
 * @author Bogdan Muresan
 * 
 */
public class AnrDetector {

	protected static long PROBE_TIME;
	protected static long STUCK_TIME = 200;
	public final static long PROBE_FAST = 400;
	public final static long PROBE_NORMAL = 600;
	public final static long PROBE_SLOW = 800;
	private static AnrDetector instance;
	private Handler mainLooperHandler;
	private AnrDetectorThread detectorThread;

	private final Runnable ackRunnable = new Runnable() {
		@Override
		public void run() {
			detectorThread.acknowledge();
			mainLooperHandler.postDelayed(this, PROBE_TIME);
		}
	};

	private AnrDetector() {
	}

	public void init(AbstractLogger logger) {
		this.init(PROBE_NORMAL, logger);
	}

	public void init(long probeTime, AbstractLogger logger) {
		this.init(probeTime, logger, Rule.getDefaultFalsePositivesRuleList());
	}

	public void init(long probeTime, AbstractLogger logger, List<Rule> ruleList) {
		// must be in main looper at init time.
		if (Looper.myLooper() != Looper.getMainLooper()) {
			throw new RuntimeException("init() must be called on main thread!");
		}
		resetState();
		PROBE_TIME = probeTime;
		mainLooperHandler = new Handler();
		detectorThread = new AnrDetectorThread(Thread.currentThread(), logger);
		detectorThread.start();
		mainLooperHandler.post(ackRunnable);
		detectorThread.setRules(ruleList);
	}

	private void resetState() {
		if (mainLooperHandler != null) {
			mainLooperHandler.removeCallbacks(ackRunnable);
		}
		if (detectorThread != null) {
			detectorThread.stopThread();
		}
	}

	public void setIgnoreNotFromPackage(String basePackageName) {
		detectorThread.setIgnoreNotFromPackage(basePackageName);
	}

	public static AnrDetector getInstance() {
		if (instance == null) {
			instance = new AnrDetector();
		}
		return instance;
	}

}
