package ro.bogdan.androidutils.anrdetectorexplorer.receivers;

import ro.bogdan.androidutils.anrdetectorexplorer.db.AnrDatabase;
import ro.bogdan.androidutils.anrdetectorexplorer.db.AnrLog;
import ro.bogdan.anrdetectorutils.AnrDetectorConstants;
import ro.bogdan.anrdetectorutils.AnrType;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

public class AnrReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		final String flattenedTrace = intent.getStringExtra(AnrDetectorConstants.EXTRA_ANR_FLATTENED_TRACE);
		final String title = "test title: "+System.currentTimeMillis();
		final long timestamp = intent.getLongExtra(AnrDetectorConstants.EXTRA_ANR_TIMESTAMP, System.currentTimeMillis());
		final String typeString = intent.getStringExtra(AnrDetectorConstants.EXTRA_ANR_TYPE_STRING);
		final String packageName = intent.getStringExtra(AnrDetectorConstants.EXTRA_ANR_PACKAGE_NAME);
		final AnrType type = AnrType.findAnrType(typeString);
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				AnrDatabase.getInstance().open();
				AnrLog log = new AnrLog(packageName, flattenedTrace, title, type, timestamp);
				AnrDatabase.getInstance().insertLog(log);
				AnrDatabase.getInstance().close();
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				// TODO notify here
			}
		}.execute();

	}
}
