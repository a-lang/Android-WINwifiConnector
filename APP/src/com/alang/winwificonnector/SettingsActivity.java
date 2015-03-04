package com.alang.winwificonnector;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

	@TargetApi(Build.VERSION_CODES.HONEYCOMB) public static class SettingsFragment extends PreferenceFragment {
		@Override
		public void onCreate (Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource ( BuildConfig.DEBUG ? R.xml.preferences_debug : R.xml.preferences);
		}
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB) public void addPreferencesFragment() {
		getFragmentManager().beginTransaction()
		.replace(android.R.id.content, new SettingsFragment())
		.commit();
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			addPreferencesFromResource ( BuildConfig.DEBUG ? R.xml.preferences_debug : R.xml.preferences);
		}else {
			addPreferencesFragment();
		}
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this); 
		prefs.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPrefs, String key) {
		if (key.equals(getString(R.string.pref_EnableBackgroundAuth))) {
			WifiBroadcastReceiver.setEnabled(this, sharedPrefs.getBoolean(key,true));
		}
	}
	
}
