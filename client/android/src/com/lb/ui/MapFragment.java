package com.lb.ui;

import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.lb.api.API;
import com.lb.model.Session;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MapFragment extends SupportMapFragment {

	private GoogleMap gMap;
	
	public MapFragment() {
		super();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		Log.i("game", "start");
		initMap();
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
    	View v = super.onCreateView(inflater, container, savedInstanceState);
        return v; 
    }

    private void initMap() {
    	gMap = getMap();
    	if(gMap != null) {
			gMap.setMyLocationEnabled(true);
			UiSettings settings = gMap.getUiSettings();
			settings.setMyLocationButtonEnabled(true);
			
			// カメラを現在位置にフォーカスする
			gMap.setOnMyLocationChangeListener(new OnMyLocationChangeListener(){
				@Override
				public void onMyLocationChange(Location location) {
					gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
					gMap.setOnMyLocationChangeListener(null); // 一回移動したらリスナーを殺す
				}
			});
			
			// テリトリー作成
			gMap.setOnMapLongClickListener(new OnMapLongClickListener() {
				@Override
				public void onMapLongClick(LatLng latlng) {
					double radius = 10000;
					API.postTerritoryLocation(Session.getUser(), latlng.latitude, latlng.longitude, radius, new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(JSONObject json) {
							Log.v("game", "territory = "+ json.toString());
						}

						@Override
						public void onFailure(Throwable throwable) {
							Log.v("game","postHitLocationOnFailure="+ throwable);
						}
					});
					CircleOptions circleOptions = new CircleOptions();
					circleOptions.center(latlng);
					circleOptions.strokeWidth(5);
					circleOptions.radius(radius);
					circleOptions.strokeColor(Color.argb(200, 0, 255, 0));
					circleOptions.fillColor(Color.argb(50, 0, 255, 0));
					Circle circle = gMap.addCircle(circleOptions);
				}
			});
    	}
    }
}
