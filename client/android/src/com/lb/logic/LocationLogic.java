package com.lb.logic;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.lb.dao.UserEntity;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

public class LocationLogic extends LogicBase {

	private LocationListener locationListener;
	private LocationClient locationClient;
	private long interval;
	
	public LocationLogic(Context context) {
		super(context);
		setInterval(10000);
	}

	public void setLocationListener(LocationListener _locationListener) {
		locationListener = _locationListener;
	}
	
	public void setInterval(long _interval) {
		interval = _interval;
	}
	
	public void start() {
		if(locationListener == null) {
			return;
		}
		
		locationClient = new LocationClient(context, new ConnectionCallbacks() {

			@Override
			public void onConnected(Bundle bundle) {
				LocationRequest request = LocationRequest.create();
				request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
				request.setInterval(interval);
				locationClient.requestLocationUpdates(request, locationListener);
			}

			@Override
			public void onDisconnected() {
				locationClient = null;
			}
			
		}, new OnConnectionFailedListener() {

			@Override
			public void onConnectionFailed(ConnectionResult result) {
				// TODO 位置を取れなかった時の処理
				
			}
			
		});
		
		locationClient.connect();
	}
	
	public void stop() {
		if(locationClient == null || !locationClient.isConnected()){
			return;
		}
		
		locationClient.removeLocationUpdates(locationListener);
		locationClient.disconnect();
	}
	
	public void post(Location location) {
		UserLogic userLogic = new UserLogic(context);
		UserEntity userEntity = userLogic.getUser();
		
		if(userEntity == null){
			return;
		}

		// API.postLocation(userEntity, location, null);
	}
}
