package ro.bogdan.androidutils.anrdetectorexplorer;

import android.app.Application;

public class AnrExplorerApplication extends Application {
	private static AnrExplorerApplication instance;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
	}

	public static Application getInstance() {
		return instance;
	}
}
