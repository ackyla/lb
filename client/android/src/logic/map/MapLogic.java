package logic.map;

import com.example.lb.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
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
import logic.base.LogicBase;

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
	
	public Marker addMarker(double lat, double lng, String title) {
		MarkerOptions options = new MarkerOptions();
		options.position(new LatLng(lat, lng));
		options.title(title);
		return map.addMarker(options);
	}
	
	public HitMarkerController addHitMarker(LatLng position, double radius) {
		MarkerOptions markerOptions = new MarkerOptions();
		CircleOptions circleOptions = new CircleOptions();
		
		markerOptions.position(position);
		markerOptions.draggable(true);
		Marker marker = map.addMarker(markerOptions);
		circleOptions.center(position);
		circleOptions.radius(radius);
		circleOptions.strokeColor(Color.argb(200, 0, 255, 255));
		circleOptions.strokeWidth(10);
		circleOptions.fillColor(Color.argb(50, 0, 255, 255));
		Circle circle = map.addCircle(circleOptions);
		
		return new HitMarkerController(marker, circle);
	}
	
	public class HitMarkerController {
		
		private Marker marker;
		private Circle circle;
		
		public HitMarkerController(Marker marker, Circle circle) {
			this.marker = marker;
			this.circle = circle;
		}
		
		public void setPosition(LatLng position) {
			marker.setPosition(position);
			circle.setCenter(position);
		}
		public LatLng getPosition() {
			return marker.getPosition();
		}
		
		public void setRadius(double radius) {
			circle.setRadius(radius);
		}
		public double getRadius() {
			return circle.getRadius();
		}
		
		public void remove() {
			marker.remove();
			circle.remove();
		}
	}

	public void moveCamera(double lat, double lng, float zoom) {
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), zoom));
		//CameraPosition camera = new CameraPosition.Builder().target(
        //.bearing(0).tilt(25).build();
		//map.animateCamera(CameraUpdateFactory.newCameraPosition(camera));
	}
	
	public Polyline drawLine(double latFrom, double lngFrom, double latTo, double lngTo) {
		PolylineOptions options = new PolylineOptions();
		options.add(new LatLng(latFrom, lngFrom));
		options.add(new LatLng(latTo, lngTo));
		options.color(0xccff00ff);
		options.width(10);
		options.geodesic(true);
		return map.addPolyline(options);
	}
}
