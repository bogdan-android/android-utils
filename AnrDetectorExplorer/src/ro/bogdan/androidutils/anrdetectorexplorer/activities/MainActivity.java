package ro.bogdan.androidutils.anrdetectorexplorer.activities;

import java.util.ArrayList;
import java.util.List;

import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.app.AlertDialog.Builder;
import org.holoeverywhere.app.ListActivity;
import org.holoeverywhere.app.ProgressDialog;

import ro.bogdan.androidutils.anrdetectorexplorer.R;
import ro.bogdan.androidutils.anrdetectorexplorer.db.AnrDatabase;
import ro.bogdan.androidutils.anrdetectorexplorer.db.AnrLog;
import ro.bogdan.androidutils.anrdetectorexplorer.utils.Helper;
import ro.bogdan.androidutils.anrdetectorexplorer.view.AnrAdapter;
import ro.bogdan.anrdetectorutils.AnrType;

import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

public class MainActivity extends ListActivity implements LoaderCallbacks<Cursor> {
	private static final int ID_LOAD_ANR = 121;
	private static final int DIALOG_CLEAR = 11;
	private static final int DIALOG_PROGRESS = 12;
	ActionMode mode;
	private String selectedPackageName;
	private AnrAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		selectedPackageName = "ro.bogdan.test1";
		adapter = new AnrAdapter(this, null);
		setListAdapter(adapter);
		getSupportLoaderManager().initLoader(ID_LOAD_ANR, null, this);

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
			case R.id.menu_clear:
				showDialog(DIALOG_CLEAR);
				break;

			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case DIALOG_CLEAR:
				AlertDialog.Builder builder = new Builder(this);
				builder.setTitle(getString(R.string.delete_all_logs));
				builder.setMessage(getString(R.string.are_you_sure_delete_logs));
				builder.setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						deleteLogs();
					}
				});
				builder.setNegativeButton(getString(R.string.cancel), null);
				return builder.create();
			case DIALOG_PROGRESS:
				ProgressDialog progress = new ProgressDialog(this);
				progress.setMessage(getString(R.string.please_wait));
				return progress;
		}
		return super.onCreateDialog(id);
	}

	private void deleteLogs() {
		new AsyncTask<Void, Void, Void>() {

			protected void onPreExecute() {
				showDialog(DIALOG_PROGRESS);
			};

			@Override
			protected Void doInBackground(Void... params) {
				AnrDatabase.getInstance().open();
				try {
					AnrDatabase.getInstance().deleteForPackageName(selectedPackageName);
				} finally {
					AnrDatabase.getInstance().close();
				}
				return null;
			}

			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				getSupportLoaderManager().restartLoader(ID_LOAD_ANR, null, MainActivity.this);
			};
		}.execute();
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

	private static class AnrAsyncLoader extends CursorLoader {

		private String packageName;

		public AnrAsyncLoader(Context context, String packageName) {
			super(context);
			this.packageName = packageName;
		}

		@Override
		public Cursor loadInBackground() {
			System.out.println("AnrAsyncLoader.loadInBackground()");
			AnrDatabase.getInstance().open();
			Cursor cursor = AnrDatabase.getInstance().getLogsGroupedByPackageName(packageName);
			return cursor;
		}

	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
		showDialog(DIALOG_PROGRESS);
		return new AnrAsyncLoader(this, selectedPackageName);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		while (cursor.moveToNext()) {
			AnrLog log = AnrDatabase.getFromCursor(cursor);
			System.out.println("found log: " + log);
		}
		adapter.changeCursor(cursor);
		removeDialog(DIALOG_PROGRESS);
		System.out.println("onLoadFinished()");
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		System.out.println("onLoaderReset()");
	}

	private final class SendLogsActionMode implements ActionMode.Callback {

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			getSupportMenuInflater().inflate(R.menu.action_mode, menu);
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			return false;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
		}

	}

}
