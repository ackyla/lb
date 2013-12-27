package com.lb.ui;

import org.json.JSONObject;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.lb.api.API;
import com.lb.model.Session;
import com.lb.ui.TerritoryListFragment.onTerritoryListItemClickListener;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MapFragment extends SupportMapFragment {
	private OnGoogleMapFragmentListener listener;
	private GoogleMap gMap;

	public MapFragment() {
		super();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		Log.i("game", "start");
	}
	
	@Override
	public void onResume() {		
		super.onResume();
		Log.i("game", "resume");
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		listener = (OnGoogleMapFragmentListener) activity;
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
    	View v = super.onCreateView(inflater, container, savedInstanceState);
    	Log.i("game", "createview");
        if (listener != null) {
            listener.onMapReady(getMap());
        }
        return v; 
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	Log.i("game", "activitycreated");
    }
    
    public static interface OnGoogleMapFragmentListener {
        void onMapReady(GoogleMap map);
    }
}
