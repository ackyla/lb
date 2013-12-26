package com.lb.ui;

import com.lb.R;
import com.lb.logic.LocationUpdateService;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.util.Log;

public class PreferenceScreenActivity extends Activity{
	
	public static final String PREF_KEY_BACKGROUND = "background";
	private static LocationUpdateService updateService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getFragmentManager().beginTransaction().replace(android.R.id.content, new CustomPreferenceScreenFragment()).commit();
	}
	
	public static class CustomPreferenceScreenFragment extends PreferenceFragment {
	
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preference_screen);
		}
		
        @Override
        public void onResume() {
            super.onResume();
            SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
            sharedPreferences.registerOnSharedPreferenceChangeListener(onPreferenceChangeListener);
        }
        
        @Override
        public void onPause() {
            super.onPause();
            SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(onPreferenceChangeListener);
        }
		
        private OnSharedPreferenceChangeListener onPreferenceChangeListener = new OnSharedPreferenceChangeListener() {
            
        	@Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        		
        		if (key.equals(PREF_KEY_BACKGROUND)) {
        			if(sharedPreferences.getBoolean(PREF_KEY_BACKGROUND, true)) {
        				Log.i("game", "on");
        			}
        		}
            }

        };
		
	}

}
