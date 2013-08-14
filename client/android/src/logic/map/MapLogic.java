package logic.map;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Context;
import logic.base.LogicBase;

public class MapLogic extends LogicBase {

	GoogleMap map;
	
	public MapLogic(Context context, GoogleMap map) {
		super(context);
		this.map = map;
	}

	public void setMap(GoogleMap map) {
		this.map = map;
	}
	
	public Marker addMarker(double lat, double lng, String title) {
		MarkerOptions options = new MarkerOptions();
		options.position(new LatLng(lat, lng));
		options.title(title);
		return map.addMarker(options);
	}
	
}
