package logic.map;

import com.example.lb.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.content.Context;
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
	}
	
	public Marker addMarker(double lat, double lng, String title) {
		MarkerOptions options = new MarkerOptions();
		options.position(new LatLng(lat, lng));
		options.title(title);
		return map.addMarker(options);
	}
	
	public void moveCamera(double lat, double lng, float zoom) {
		CameraPosition camera = new CameraPosition.Builder().target(new LatLng(lat, lng)).zoom(zoom)
        .bearing(0).tilt(25).build();
		map.animateCamera(CameraUpdateFactory.newCameraPosition(camera));
	}
	
	public Polyline drawLine(double latFrom, double lngFrom, double latTo, double lngTo) {
		PolylineOptions options = new PolylineOptions();
		options.add(new LatLng(latFrom, lngFrom));
		options.add(new LatLng(latTo, lngTo));
		options.color(0xcc00ffff);
		options.width(10);
		options.geodesic(true);
		return map.addPolyline(options);
	}
}
