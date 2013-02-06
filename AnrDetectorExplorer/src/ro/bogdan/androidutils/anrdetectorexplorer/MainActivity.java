package ro.bogdan.androidutils.anrdetectorexplorer;

import java.util.ArrayList;
import java.util.List;

import org.holoeverywhere.app.Activity;

import ro.bogdan.androidutils.anrdetectorexplorer.db.AnrDatabase;
import ro.bogdan.androidutils.anrdetectorexplorer.db.AnrLog;
import ro.bogdan.androidutils.anrdetectorexplorer.db.AnrLogsGroup;
import ro.bogdan.androidutils.anrdetectorexplorer.utils.Helper;
import ro.bogdan.anrdetectorutils.AnrType;

import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

public class MainActivity extends Activity implements LoaderCallbacks<List<AnrLogsGroup>> {
	ActionMode mode;
	private String selectedPackageName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getSupportLoaderManager().initLoader(0, null, this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_send:
			mode = startActionMode(new SendLogsActionMode());
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		if (!dismissActionMode()) {
			super.onBackPressed();
		}
	}

	private boolean dismissActionMode() {
		if (mode != null) {
			mode.finish();
			mode = null;
			return true;
		} else {
			return false;
		}
	}

	public void testInsert() {
		long start = System.currentTimeMillis();
		List<AnrLog> logList = new ArrayList<AnrLog>();
		logList.add(new AnrLog("ro.bogdan.test1", "trace1", "title1", AnrType.SMALL, System.currentTimeMillis()));
		logList.add(new AnrLog("ro.bogdan.test1", "trace2", "title2", AnrType.MEDIUM, System.currentTimeMillis()));
		logList.add(new AnrLog("ro.bogdan.test1", "trace3", "title3", AnrType.LARGE, System.currentTimeMillis()));
		logList.add(new AnrLog("ro.bogdan.test1", "trace4", "title4", AnrType.MEDIUM, System.currentTimeMillis()));
		logList.add(new AnrLog("ro.bogdan.test1", "trace5", "title5", AnrType.SMALL, System.currentTimeMillis()));
		logList.add(new AnrLog("ro.bogdan.test2", "trace6", "title6", AnrType.MEDIUM, System.currentTimeMillis()));
		logList.add(new AnrLog("ro.bogdan.test2", "trace7", "title7", AnrType.LARGE, System.currentTimeMillis()));
		logList.add(new AnrLog("ro.bogdan.test2", "trace8", "title8", AnrType.MEDIUM, System.currentTimeMillis()));
		logList.add(new AnrLog("ro.bogdan.test2", "trace9", "title9", AnrType.SMALL, System.currentTimeMillis()));
		logList.add(new AnrLog("ro.bogdan.test3", "trace10", "title10", AnrType.LARGE, System.currentTimeMillis()));
		AnrDatabase.getInstance().open();
		for (AnrLog log : logList) {
			AnrDatabase.getInstance().insertLog(log);
		}
		AnrDatabase.getInstance().close();
		long end = System.currentTimeMillis();
		System.out.println("DEBUG: testInsert: completed In = " + (end - start) + " ms");

	}

	public void testQuery() {
		long start = System.currentTimeMillis();
		AnrDatabase.getInstance().open();
		Cursor cursor = AnrDatabase.getInstance().getAllLogs();
		int count = 0;
		while (cursor.moveToNext()) {
			AnrLog log = AnrDatabase.getFromCursor(cursor);
			System.out.println("DEBUG: found log: " + log);
			count++;
		}
		Helper.closeCursor(cursor);
		AnrDatabase.getInstance().close();
		long end = System.currentTimeMillis();
		System.out.println("DEBUG: testQuery: " + count + " items in: " + (end - start) + " ms");
	}

	private final class SendLogsActionMode implements ActionMode.Callback {

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			getSupportMenuInflater().inflate(R.menu.action_mode, menu);
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			// TODO Auto-generated method stub

		}

	}

	private class AnrAsyncLoader extends AsyncTaskLoader<List<AnrLogsGroup>> {

		public AnrAsyncLoader(Context context) {
			super(context);
		}

		@Override
		public List<AnrLogsGroup> loadInBackground() {
			List<AnrLogsGroup> anrLogsGroupList = new ArrayList<AnrLogsGroup>();
			AnrDatabase.getInstance().open();
			Cursor cursor = AnrDatabase.getInstance().getLogsGroupedByPackageName(selectedPackageName);
			
			// TODO Auto-generated method stub
			return null;
		}

	}

	@Override
	public Loader<List<AnrLogsGroup>> onCreateLoader(int id, Bundle bundle) {
		return new AnrAsyncLoader(this);
	}

	@Override
	public void onLoadFinished(Loader<List<AnrLogsGroup>> loader, List<AnrLogsGroup> result) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onLoaderReset(Loader<List<AnrLogsGroup>> loader) {
		// TODO Auto-generated method stub

	}
}
