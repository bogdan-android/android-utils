package ro.bogdan.androidutils.anrdetector.loggers;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import ro.bogdan.anrdetectorutils.AnrDetectorConstants;
import ro.bogdan.anrdetectorutils.Trace;

public class IntentLogger implements AbstractLogger {

	private Context context;

	public IntentLogger(Context context) {
		this.context = context;
	}

	@Override
	public void logANR(Trace trace, long timeout) {
		Intent intent = new Intent();
		intent.setAction(AnrDetectorConstants.INTENT_ACTION_ANR);
		if (haveReceiverForIntent(intent)) {
			intent.putExtra(AnrDetectorConstants.EXTRA_ANR_FLATTENED_TRACE, trace.getFlattenedTrace());
			intent.putExtra(AnrDetectorConstants.EXTRA_ANR_TIMESTAMP, System.currentTimeMillis());
			intent.putExtra(AnrDetectorConstants.EXTRA_ANR_TYPE_STRING, trace.getType().getName());
			intent.putExtra(AnrDetectorConstants.EXTRA_ANR_PACKAGE_NAME, context.getPackageName());
			context.sendBroadcast(intent);
		}
	}

	private boolean haveReceiverForIntent(Intent intent) {
		final List<ResolveInfo> activities = context.getPackageManager().queryBroadcastReceivers(intent, 0);
		for (ResolveInfo resolveInfo : activities) {
			ActivityInfo activityInfo = resolveInfo.activityInfo;
			if (activityInfo != null) {
				return true;
			}
		}
		return false;
	}

}
