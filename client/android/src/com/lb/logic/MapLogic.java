package com.lb.logic;

import java.util.Iterator;
import java.util.List;

import com.lb.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.util.Log;

public class MapLogic extends LogicBase {

	GoogleMap map;
	SupportMapFragment mapFragment;
	
	public MapLogic(Context context, SupportMapFragment mapFragment) {
		super(context);
		this.mapFragment = mapFragment;
	}

	public void init(){
		this.map = mapFragment.getMap();
		this.map.setMyLocationEnabled(true);
		UiSettings settings = this.map.getUiSettings();
		settings.setMyLocationButtonEnabled(true);

		// カメラを現在位置にフォーカスする
		map.setOnMyLocationChangeListener(new OnMyLocationChangeListener(){
		      @Override
		      public void onMyLocationChange(Location location) {
		    	  moveCamera(location.getLatitude(), location.getLongitude(), 15);
		    	  map.setOnMyLocationChangeListener(null); // 一回移動したらリスナーを殺す
		      }
		});    
	}
	
	public void setOnClickListener(OnMapClickListener listener) {
		map.setOnMapClickListener(listener);
	}
	
	public void setOnLongClickListener(OnMapLongClickListener listener) {
		map.setOnMapLongClickListener(listener);
	}
	
	public Marker addLocationMarker(double lat, double lng, String title, String time) {
		MarkerOptions options = new MarkerOptions();
		options.position(new LatLng(lat, lng));
		options.title(title);
		options.snippet(time);
		options.draggable(false);
		return map.addMarker(options);
	}
	
	public Polyline drawLine(List<Marker> markerList) {
		Iterator<Marker> mi = markerList.iterator();
		PolylineOptions options = new PolylineOptions();

		while (mi.hasNext()) {
			options.add(mi.next().getPosition());
		}
		options.color(0xccff00ff);
		options.width(10);
		options.geodesic(true);
		return map.addPolyline(options);
	}
	
	public HitMarker addHitMarker(LatLng position, double radiusBlue, double radiusYellow, double radiusRed) {
		MarkerOptions markerOptions = new MarkerOptions();
		CircleOptions circleOptions = new CircleOptions();
		
		markerOptions.position(position);
		markerOptions.draggable(true);
		Marker marker = map.addMarker(markerOptions);
		
		circleOptions.center(position);
		circleOptions.strokeWidth(5);

		circleOptions.radius(radiusRed);
		circleOptions.strokeColor(Color.argb(200, 255, 0, 255));
		circleOptions.fillColor(Color.argb(20, 255, 0, 255));
		Circle circleRed = map.addCircle(circleOptions);
		
		circleOptions.radius(radiusYellow);
		circleOptions.strokeColor(Color.argb(200, 255, 255, 0));
		circleOptions.fillColor(Color.argb(40, 255, 255, 0));
		Circle circleYellow = map.addCircle(circleOptions);
		
		circleOptions.radius(radiusBlue);
		circleOptions.strokeColor(Color.argb(200, 0, 255, 255));
		circleOptions.fillColor(Color.argb(80, 0, 255, 255));
		Circle circleBlue = map.addCircle(circleOptions);
		
		return new HitMarker(marker, circleBlue, circleYellow, circleRed);
	}
	
	public TerritoryMarker addTerritory(LatLng position, double radius) {
		CircleOptions circleOptions = new CircleOptions();
		
		circleOptions.center(position);
		circleOptions.strokeWidth(5);
		circleOptions.radius(radius);
		circleOptions.strokeColor(Color.argb(200, 0, 255, 0));
		circleOptions.fillColor(Color.argb(50, 0, 255, 0));
		Circle circle = map.addCircle(circleOptions);

		return new TerritoryMarker(circle);
	}
	
	public void moveCamera(double lat, double lng, float zoom) {
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), zoom));
		//CameraPosition camera = new CameraPosition.Builder().target(
        //.bearing(0).tilt(25).build();
		//map.animateCamera(CameraUpdateFactory.newCameraPosition(camera));
	}
}
