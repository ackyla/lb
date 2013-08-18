package logic.map;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;

import android.content.Context;
import android.util.Log;
import logic.base.LogicBase;

public class LocationMarker {

	private List<Marker> markerList;
	private Polyline line;

	public LocationMarker() {
		markerList = new ArrayList<Marker>();
	}
	
	public List<Marker> getMarkers() {
		return markerList;
	}
	
	public void addMarker(Marker marker) {
		markerList.add(marker);
	}

	public void addLine(Polyline line) {
		this.line = line;
	}

	public void remove() {
		Iterator<Marker> mi = markerList.iterator();
		while (mi.hasNext()) {
			mi.next().remove();
			mi.remove();
		}

		line.remove();
		line = null;
	}

}
