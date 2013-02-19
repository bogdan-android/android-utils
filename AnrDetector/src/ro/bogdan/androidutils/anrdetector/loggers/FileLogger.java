package ro.bogdan.androidutils.anrdetector.loggers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.os.Environment;

/**
 * @author bogdan.muresan
 */
public final class FileLogger{

	private static final String ROOT_FOLDER = Environment.getExternalStorageDirectory().getPath();
	private static final String DIRECTORY = "";//TODO set directory here.
	private static final boolean SEPARATE_FILES_FOR_DAYS = true;//set to true if you want separate files for days.
	private static final String FILE_NAME = "anr.log";
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	private static String sFileName;
	private static int mDay;
	private static int mMonth;
	private static int mYear;
	private static String mLatestTimestamp;

	public static void logTextToFile(String text) {
		if (canWriteToDisk()) {
			String time = convertToUTC(System.currentTimeMillis());
			writeMessage("\n" + time + " | " + text);
		}
	}

	/**
	 * This method writes a string to the destination folder/file.
	 * 
	 * @param text
	 *            The text to write
	 */
	private static void writeMessage(String text) {
		Calendar cal = Calendar.getInstance();
		FileOutputStream fos = null;
		try {
			File logDirectory = new File(ROOT_FOLDER + DIRECTORY);
			if (!logDirectory.exists()) {
				logDirectory.mkdirs();
			}
			fos = new FileOutputStream(new File(logDirectory, getFileName(cal)), true);
			fos.write(text.getBytes());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * This method determines if the external storages is available for writing
	 * 
	 * @return true if the the media is mounted and can be written into
	 */
	private static boolean canWriteToDisk() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	/**
	 * Returns the filename of the log file. It can be the same file, or it can
	 * be a separate file for each day
	 * 
	 * @return the filename
	 */
	private static String getFileName(Calendar cal) {
		if (!SEPARATE_FILES_FOR_DAYS) {
			if (sFileName == null) {
				sFileName = FILE_NAME;// TODO replace here
			}
		} else {
			if (mDay != cal.get(Calendar.DAY_OF_MONTH) || mMonth != cal.get(Calendar.MONTH) || mYear != cal.get(Calendar.YEAR)) {
				// needs change.
				mYear = cal.get(Calendar.YEAR);
				mMonth = cal.get(Calendar.MONTH);
				mDay = cal.get(Calendar.DAY_OF_MONTH);
				mLatestTimestamp = mYear + "_" + (mMonth + 1) + "_" + mDay;
			}
			sFileName = mLatestTimestamp + "_" + FILE_NAME;
		}
		return sFileName;
	}
	
	@SuppressLint("SimpleDateFormat")
	public static String convertToUTC(long timestamp) {
		Date date = new Date(timestamp);
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		return sdf.format(date);
	}

}