package ro.bogdan.androidutils.anrdetectorexplorer.db;

import ro.bogdan.androidutils.anrdetectorexplorer.AnrExplorerApplication;
import ro.bogdan.anrdetectorutils.AnrType;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class AnrDatabase {

	private static final String DB_NAME = "anr_database";
	private static final int DB_VERSION = 1;
	private static final String TABLE_NAME = "table_anr_logs";
	private static final String ID = "_id";
	private static final String TRACE = "trace";
	private static final String TITLE = "title";
	private static final String TIMESTAMP = "timestamp";
	private static final String TYPE = "anr_type";

	private SQLiteOpenHelper helper;
	private static AnrDatabase instance;
	private int openCount = 0;
	private SQLiteDatabase db;
	private static String[] COLUMNS = { ID,/* 0 */TRACE/* 1 */, TITLE/* 2 */, TYPE /* 3 */, TIMESTAMP /* 4 */};
	private static String CREATE_QUERY = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TRACE
			+ " TEXT NOT NULL, " + TITLE + " TEXT, " + TYPE + " TEXT NOT NULL, " + TIMESTAMP + " INTEGER)";

	public static AnrDatabase getInstance() {
		if (instance == null) {
			instance = new AnrDatabase(AnrExplorerApplication.getInstance().getApplicationContext());
		}
		return instance;
	}

	private AnrDatabase(Context context) {
		helper = new AnrDatabaseHelper(context, DB_NAME, null, DB_VERSION);
	}

	public synchronized void open() {
		if (db == null || !db.isOpen()) {
			db = helper.getWritableDatabase();
		}
		openCount++;
	}

	public synchronized void close() {
		openCount--;
		if (openCount <= 0) {
			openCount = 0;
			helper.close();
		}
	}

	public Cursor getAllLogs() {
		return db.query(TABLE_NAME, COLUMNS, null, null, null, null, TIMESTAMP + " DESC");
	}

	public void insertLog(AnrLog log) {
		ContentValues values = getValues(log);
		db.insert(TABLE_NAME, null, values);
	}

	public void deleteAll() {
		db.delete(TABLE_NAME, null, null);
	}

	public void deleteLog(AnrLog log) {
		delete(log.getId());
	}

	public void delete(long id) {
		db.delete(TABLE_NAME, ID + " = ?", new String[] { String.valueOf(id) });
	}

	public static AnrLog getFromCursor(Cursor cursor) {
		long id = cursor.getLong(0);
		String trace = cursor.getString(1);
		String title = cursor.getString(2);
		String typeString = cursor.getString(3);
		long timestamp = cursor.getLong(4);
		return new AnrLog(id, trace, title, AnrType.findAnrType(typeString), timestamp);
	}

	private ContentValues getValues(AnrLog log) {
		ContentValues values = new ContentValues();
		values.put(TRACE, log.getTrace());
		values.put(TITLE, log.getTitle());
		values.put(TYPE, log.getType().getName());
		values.put(TIMESTAMP, log.getTimestamp());
		return values;
	}

	private class AnrDatabaseHelper extends SQLiteOpenHelper {

		public AnrDatabaseHelper(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_QUERY);

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		}

	}

}
