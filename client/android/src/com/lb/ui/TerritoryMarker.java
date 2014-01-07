package com.lb.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.MarkerOptionsCreator;
import com.lb.R;
import com.lb.model.Utils;

public class TerritoryMarker {
	
	private MarkerOptions mMarkerOpt;
	private CircleOptions mCircleOpt;
	private Marker mMarker;
	private Circle mCircle;
	
	public TerritoryMarker() {
		mMarkerOpt = new MarkerOptions();
		mCircleOpt = new CircleOptions();
		
		
		mCircleOpt.strokeWidth(5);
	}
	
	public void setCenter(LatLng latlng) {
		mMarkerOpt.position(latlng);
		mCircleOpt.center(latlng);
	}	
	
	public void setRadius(Double radius) {
		mCircleOpt.radius(radius);
	}
	
	public void setIcon(BitmapDescriptor icon) {
		mMarkerOpt.icon(icon);
	}
	
	public void setColor(int r, int g, int b) {
		mCircleOpt.strokeColor(Color.argb(200, r, g, b));
		mCircleOpt.fillColor(Color.argb(50, r, g, b));
	}
	
	public void setTitle(String title) {
		mMarkerOpt.title(title);
	}
	
	public void setSnippet(String snippet) {
		mMarkerOpt.snippet(snippet);
	}
	
	public void addTo(GoogleMap map) {
		if(map != null) {
			mCircle = map.addCircle(mCircleOpt);
			mMarker = map.addMarker(mMarkerOpt);
		}
	}

	public void updateCenter(LatLng latlng) {
		mMarker.setPosition(latlng);
		mCircle.setCenter(latlng);
	}
	public void updateRadius(Double radius) {
		mCircle.setRadius(radius);
	}
	public LatLng getCenter() {
		if (mMarker == null) return Utils.getDefaultLatLng();
		return mMarker.getPosition();
	}
	public Double getRadius() {
		if (mCircle == null) return 100.0;
		return mCircle.getRadius();
	}
	
	public void showInfoWindow() {
		if(mMarker != null) mMarker.showInfoWindow();
	}
}
