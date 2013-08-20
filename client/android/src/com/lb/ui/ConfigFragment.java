package com.lb.ui;

import com.lb.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ConfigFragment extends Fragment {
	
	public ConfigFragment() {
		setRetainInstance(true);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v("life", "config create");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	final View v = inflater.inflate(R.layout.fragment_config, container, false);
		Log.v("life", "config createView");
    	return v;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.v("life", "config attach");
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		Log.v("life", "config detach");
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.v("life", "config destroy");
	}
}
