package pl.m4.photomaker;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class SettingsPreferences {
	private Context context;
	
	public SettingsPreferences(Context context) {
		this.context = context;
	}
	public boolean loadSavedPreferences(String key) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sp.getBoolean(key, false);
	}
	
	public String loadSavedPreferences(String key, String defVal) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sp.getString(key, defVal);
	}

	public void savePreferences(String key, boolean value) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor edit = sp.edit();
		edit.putBoolean(key, value);
		edit.commit();
	}

	public void savePreferences(String key, String value) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor edit = sp.edit();
		edit.putString(key, value);
		edit.commit();
	}
}
