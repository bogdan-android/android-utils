
package org.holoeverywhere.app;

import org.holoeverywhere.IHolo;
import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.LayoutInflater.LayoutInflaterCreator;
import org.holoeverywhere.Setting;
import org.holoeverywhere.SystemServiceManager;
import org.holoeverywhere.SystemServiceManager.SuperSystemService;
import org.holoeverywhere.ThemeManager;
import org.holoeverywhere.ThemeManager.SuperStartActivity;
import org.holoeverywhere.app.Application.Config.PreferenceImpl;
import org.holoeverywhere.preference.PreferenceManagerHelper;
import org.holoeverywhere.preference.SharedPreferences;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;

public class Application extends android.app.Application implements
        IHolo, SuperStartActivity, SuperSystemService {
    public static final class Config extends Setting<Config> {
        public static enum PreferenceImpl {
            JSON, XML
        }

        private static final String HOLO_EVERYWHERE_PACKAGE = "org.holoeverywhere";

        @SettingProperty(create = true, defaultBoolean = false)
        public BooleanProperty alwaysUseParentTheme;
        @SettingProperty(create = true, defaultBoolean = false)
        public BooleanProperty debugMode;
        @SettingProperty(create = true)
        public BooleanProperty disableContextMenu;
        @SettingProperty(create = true, defaultBoolean = true)
        public BooleanProperty disableOverscrollEffects;
        @SettingProperty(create = true, defaultString = Config.HOLO_EVERYWHERE_PACKAGE)
        public StringProperty holoEverywherePackage;
        @SettingProperty(create = true, defaultBoolean = true)
        public BooleanProperty namedPreferences;
        @SettingProperty(create = true, defaultEnum = "XML", enumClass = PreferenceImpl.class)
        public EnumProperty<PreferenceImpl> preferenceImpl;
    }

    private static Application lastInstance;

    static {
        SystemServiceManager.register(LayoutInflaterCreator.class);
        config().disableContextMenu.setValue(VERSION.SDK_INT >= 14);
        config().disableOverscrollEffects.setValue(VERSION.SDK_INT <= 10);
    }

    public static Config config() {
        return Setting.get(Config.class);
    }

    public static Application getLastInstance() {
        return Application.lastInstance;
    }

    public static boolean isDebugMode() {
        return Application.config().debugMode.getValue();
    }

    public Application() {
        Application.lastInstance = this;
    }

    @Override
    public Config getConfig() {
        return config();
    }

    @Override
    public SharedPreferences getDefaultSharedPreferences() {
        return PreferenceManagerHelper.getDefaultSharedPreferences(this);
    }

    @Override
    public SharedPreferences getDefaultSharedPreferences(PreferenceImpl impl) {
        return PreferenceManagerHelper.getDefaultSharedPreferences(this, impl);
    }

    @Override
    public LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(this);
    }

    @Override
    public SharedPreferences getSharedPreferences(PreferenceImpl impl, String name, int mode) {
        return PreferenceManagerHelper.wrap(this, impl, name, mode);
    }

    @Override
    public SharedPreferences getSharedPreferences(String name, int mode) {
        return PreferenceManagerHelper.wrap(this, name, mode);
    }

    @Override
    public Application getSupportApplication() {
        return this;
    }

    @Override
    public void onTerminate() {
        LayoutInflater.clearInstances();
        super.onTerminate();
    }

    @Override
    @SuppressLint("NewApi")
    public void startActivities(Intent[] intents) {
        startActivities(intents, null);
    }

    @Override
    @SuppressLint("NewApi")
    public void startActivities(Intent[] intents, Bundle options) {
        for (Intent intent : intents) {
            startActivity(intent, options);
        }
    }

    @Override
    @SuppressLint("NewApi")
    public void startActivity(Intent intent) {
        startActivity(intent, null);
    }

    @Override
    public void startActivity(Intent intent, Bundle options) {
        if (config().alwaysUseParentTheme.getValue()) {
            ThemeManager.startActivity(this, intent, options);
        } else {
            superStartActivity(intent, -1, options);
        }
    }

    public android.content.SharedPreferences superGetSharedPreferences(
            String name, int mode) {
        return super.getSharedPreferences(name, mode);
    }

    @Override
    public Object superGetSystemService(String name) {
        return super.getSystemService(name);
    }

    @Override
    @SuppressLint("NewApi")
    public void superStartActivity(Intent intent, int requestCode,
            Bundle options) {
        if (VERSION.SDK_INT >= 16) {
            super.startActivity(intent, options);
        } else {
            super.startActivity(intent);
        }
    }
}
