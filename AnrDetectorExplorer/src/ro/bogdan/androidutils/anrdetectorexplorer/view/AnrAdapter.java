package ro.bogdan.androidutils.anrdetectorexplorer.view;


import ro.bogdan.androidutils.anrdetectorexplorer.db.AnrDatabase;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;

public class AnrAdapter extends CursorAdapter {

	public AnrAdapter(Context context, Cursor c) {
		super(context, c);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		AnrLogView anrLogView = (AnrLogView)view;
		anrLogView.setAnrLog(AnrDatabase.getFromCursor(cursor));
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
		return new AnrLogView(context);
	}

}
