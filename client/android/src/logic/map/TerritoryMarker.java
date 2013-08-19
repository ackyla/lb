package logic.map;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;

public class TerritoryMarker {
	private Circle circle;
	
	public TerritoryMarker(Circle circle) {
		this.circle = circle;
	}
	
	public void setPosition(LatLng position) {
		circle.setCenter(position);
	}
	public LatLng getPosition() {
		return circle.getCenter();
	}
	
	public void remove() {
		circle.remove();
	}
}