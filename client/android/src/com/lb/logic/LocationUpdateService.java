package com.lb.logic;

import org.json.JSONObject;

import com.google.android.gms.location.LocationListener;
import com.lb.api.API;
import com.lb.model.Session;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class LocationUpdateService extends Service{
	
	private static int NOTIFICATION_ID = 921470102;
	
	public class LocationUpdateBinder extends Binder {
		LocationUpdateService getService() {
			return LocationUpdateService.this;
		}
	}
	
	private final IBinder mBinder = new LocationUpdateBinder();
	
	@Override
	public void onCreate() {
		Log.v("main", "create service");
		startUpdate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.v("main", "startId="+startId);
		HandleIntent(intent);
		return START_REDELIVER_INTENT;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		Log.v("main", "bind service");
		return mBinder;
	}

	@Override
	public void onRebind(Intent intent) {
		Log.v("main", "rebind service");
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		Log.v("main", "unbind service");
		return true;
	}
	
	@Override
	public void onDestroy() {
		Log.v("main", "destroy service");
		stopUpdate();
		super.onDestroy();
	}
	
    @Override
    public void onLowMemory()
    {
        Log.v("main", "low memory service");
        super.onLowMemory();
    }
    
    private void HandleIntent(Intent intent) {
    	
    }
    
    protected void startUpdate() {
    	
    	// 既に位置情報アップデートが動いているときは何もしない
    	if(Session.getIsStarted()) {
    		return;
    	}
    	
    	try {
    		startForeground(NOTIFICATION_ID, new Notification());    		
    	} catch(Exception e) {
    	}
    	
    	Session.setIsStarted(true);
    }
    
    protected void stopUpdate() {
    	Session.setIsStarted(false);
    	stopForeground(true);
    }
}
