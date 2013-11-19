package com.lb.logic;

import org.json.JSONObject;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.lb.api.API;
import com.lb.model.Session;
import com.lb.model.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class LocationUpdateService extends Service{
	
	private static int NOTIFICATION_ID = 921470102;
	private final IBinder mBinder = new LocationUpdateBinder();
	private static ILocationUpdateServiceClient mainServiceClient;
    
	private LocationListener locationListener;
	private LocationClient locationClient;
	
	@Override
	public IBinder onBind(Intent intent) {
		Log.v("game", "bind service");
		return mBinder;
	}
	
	@Override
	public void onCreate() {
		Log.v("game", "create service");
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		Log.v("game", "start service");
		HandleIntent(intent);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.v("game", "startId="+startId);
		HandleIntent(intent);
		return START_REDELIVER_INTENT;
	}
	
	@Override
	public void onDestroy() {
		Log.v("game", "destroy service");
		mainServiceClient = null;
		super.onDestroy();
	}
	
    @Override
    public void onLowMemory()
    {
        Log.v("game", "low memory service");
        super.onLowMemory();
    }
	
    private void HandleIntent(Intent intent) {
    	Log.v("game", "Null intent? " + String.valueOf(intent == null));
    	
    	if (intent != null) {
    		Bundle bundle = intent.getExtras();
    		
    		Log.v("game", "Null bundle? " + String.valueOf(bundle == null));
    		if (bundle != null) {
    			
    		}    	
    	}else{
    		Log.v("game", "Service restarted with null intent. Start logging.");
			startUpdate();
    	}
    }

	@Override
	public void onRebind(Intent intent) {
		Log.v("game", "rebind service");
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		Log.v("game", "unbind service");
		return true;
	}
    
	public class LocationUpdateBinder extends Binder {
		public LocationUpdateService getService() {
			return LocationUpdateService.this;
		}
	}
	
    public static void setServiceClient(ILocationUpdateServiceClient mainForm)
    {
        mainServiceClient = mainForm;
    }
	
    public void startUpdate() {
    	
    	// 既に位置情報アップデートが動いているときは何もしない
    	if(Session.getIsStarted()) {
    		return;
    	}
    	
    	try {
    		startForeground(NOTIFICATION_ID, new Notification());    		
    	} catch(Exception e) {
    	}
    	
    	Session.setIsStarted(true);
    	Log.v("game", "start update");
    	startGpsManager();
    }
    
    public void stopUpdate() {
    	Session.setIsStarted(false);
    	stopForeground(true);
    	Log.v("game", "stop update");
    	stopGpsManager();
    }
    
    private void startGpsManager() {
    	locationListener = new LocationListener() {
			@Override
			public void onLocationChanged(Location location) {
				Log.v("game", "location="+location.toString());
				API.postLocation(Session.getUser(), location, new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject json) {
						Log.v("game", "location=" + json.toString());
					}
					@Override
					public void onFailure(Throwable throwable) {
						Log.v("game", "postLocationOnFailure=" + throwable);
					}
				});
			}
    	};
    	
    	locationClient = new LocationClient(getApplicationContext(), new ConnectionCallbacks() {

			@Override
			public void onConnected(Bundle bundle) {
				Log.v("game", "gps connect");
				LocationRequest request = LocationRequest.create();
				request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
				request.setInterval(60000);
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
    
    private void stopGpsManager() {
		if(locationClient == null || !locationClient.isConnected()){
			return;
		}
		
		Log.v("game", "gps disconnect");
		locationClient.removeLocationUpdates(locationListener);
		locationClient.disconnect();
    }
}
