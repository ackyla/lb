package com.lb.ui;

import com.lb.R;
import com.lb.logic.LocationUpdateService;
import com.lb.model.Session;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.util.Log;
import android.widget.Toast;

public class PreferenceScreenActivity extends Activity {
	
	public static final String PREF_KEY_BACKGROUND = "background";
	public static final String PREF_KEY_DEBUG_MODE_URL = "debug_mode_url";
	
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
			
			// デバッグモードじゃないときはURL選択を表示しない
			if (!getResources().getBoolean(R.bool.debug_mode)) {
				getPreferenceScreen().removePreference(this.getPreferenceScreen().findPreference(PREF_KEY_DEBUG_MODE_URL));
			}
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
        		if (key.equals(PREF_KEY_DEBUG_MODE_URL)) {        			
        			Toast.makeText(getActivity(), "APIのurlを" + sharedPreferences.getString(PREF_KEY_DEBUG_MODE_URL, "") + "に設定しました", Toast.LENGTH_LONG).show();
        		}
            }

        };
		
	}

}
