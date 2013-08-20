package com.lb.logic;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class HitMarker {

	private Marker marker;
	private Circle circleBlue;
	private Circle circleYellow;
	private Circle circleRed;

	public HitMarker(Marker marker, Circle circleBlue, Circle circleYellow,
			Circle circleRed) {
		this.marker = marker;
		this.circleBlue = circleBlue;
		this.circleYellow = circleYellow;
		this.circleRed = circleRed;
	}

	public void setPosition(LatLng position) {
		marker.setPosition(position);
		circleBlue.setCenter(position);
		circleYellow.setCenter(position);
		circleRed.setCenter(position);
	}

	public LatLng getPosition() {
		return marker.getPosition();
	}

	public void remove() {
		marker.remove();
		circleBlue.remove();
		circleYellow.remove();
		circleRed.remove();
	}

}
