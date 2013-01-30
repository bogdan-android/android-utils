package ro.bogdan.androidutils.anrdetectorexplorer.utils;

import android.database.Cursor;

public final class Helper {
	private Helper() {
		throw new RuntimeException("do not create instances!");
	}

	public static void closeCursor(Cursor cursor) {
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
	}
}
