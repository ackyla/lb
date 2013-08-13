package com.example.lb;

import com.example.lb.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MapFragment extends Fragment {
	public MapFragment() {
		setRetainInstance(true);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v("life", "map create");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.v("life", "map createView");
    	View v = inflater.inflate(R.layout.fragment_map, container, false);
    	FragmentManager manager = getActivity().getSupportFragmentManager();
    	SupportMapFragment fragment = (SupportMapFragment)manager.findFragmentById(R.id.mapFragment);
    	GoogleMap map = fragment.getMap();
    	return v;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.v("life", "map attach");
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		Log.v("life", "map detach");
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.v("life", "map destroy");
	}
}
