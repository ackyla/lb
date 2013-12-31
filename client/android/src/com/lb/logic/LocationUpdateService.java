package com.lb.logic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.lb.R;
import com.lb.api.API;
import com.lb.model.Session;
import com.lb.model.User;
import com.lb.model.Utils;
import com.lb.ui.GameActivity;
import com.lb.ui.NotificationAdapter;
import com.lb.ui.NotificationData;
import com.lb.ui.NotificationDetailActivity;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
	
	private TimerTask getNotificationTask;
	
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
    	startNotificationReceiver();
    }
    
    public void stopUpdate() {
    	Session.setIsStarted(false);
    	stopForeground(true);
    	Log.v("game", "stop update");
    	stopGpsManager();
    	stopNotificationReceiver();
    }
    
    private void startNotificationReceiver() {
    	TimerLogic timerLogic = new TimerLogic(this);
		getNotificationTask = timerLogic.create(new Runnable() {
			@Override
			public void run() {
				API.getUserNotifications(Session.getUser(), false, new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONArray jsonArray) {
						for(int i = 0; i < jsonArray.length(); i ++) {
							try {
								JSONObject json = jsonArray.getJSONObject(i);
						        SimpleDateFormat sdf = new SimpleDateFormat("yyyy'/'MM'/'dd' 'HH':'mm':'ss");
							    Intent intent = new Intent(LocationUpdateService.this, GameActivity.class);
							    intent.putExtra("notification_id", json.getInt("notification_id"));

							    Notification.Builder builder = new Notification.Builder(getApplicationContext());
								String type = json.getString("notification_type");
								if(type.equals("entering")) {
									// みつかった
								    builder.setTicker(json.getJSONObject("territory_owner").getString("name") + " のテリトリーに入りました");
								    builder.setContentTitle(json.getJSONObject("territory_owner").getString("name") + " のテリトリーに入りました");
								    builder.setContentText("タップして詳細を見る");
								    builder.setContentInfo(sdf.format(Utils.parseStringToDate(json.getString("created_at")))+" に見つかった");
								    builder.setSmallIcon(android.R.drawable.ic_menu_info_details);
								    intent.putExtra("latitude", json.getJSONObject("location").getDouble("latitude"));
								    intent.putExtra("longitude", json.getJSONObject("location").getDouble("longitude"));
								    intent.putExtra("title", json.getJSONObject("territory_owner").getString("name") + " のテリトリーに入りました");
								    intent.putExtra("message", sdf.format(Utils.parseStringToDate(json.getString("created_at")))+" に見つかった");
								}else{
									// みつけた
								    builder.setTicker("テリトリー_"+json.getJSONObject("territory").getInt("territory_id")+"への侵入者発見");
								    builder.setContentTitle("テリトリー_"+json.getJSONObject("territory").getInt("territory_id")+"への侵入者発見");
								    builder.setContentText("タップして詳細を見る");
								    builder.setContentInfo(sdf.format(Utils.parseStringToDate(json.getString("created_at")))+" に侵入");
								    builder.setSmallIcon(android.R.drawable.ic_menu_mylocation);
								    intent.putExtra("latitude", json.getJSONObject("territory").getDouble("latitude"));
								    intent.putExtra("longitude", json.getJSONObject("territory").getDouble("longitude"));
								}
								intent.putExtra("notification_type", type);
								PendingIntent pendingIntent = PendingIntent.getActivity(LocationUpdateService.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
							    builder.setContentIntent(pendingIntent);
							    builder.setVibrate(new long[] {1000, 700, 250});
							    builder.setAutoCancel(true);
							    builder.setOnlyAlertOnce(true);
								Notification notification = builder.getNotification();
								NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
								manager.notify(json.getInt("notification_id"), notification);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}

					@Override
					public void onFailure(Throwable throwable) {
						Log.i("game","getUserNotificationListOnFailure="+ throwable);
					}
				});
			}
		});
		timerLogic.start(getNotificationTask, 10000);
    }
    
    private void stopNotificationReceiver() {
    	if(getNotificationTask != null) getNotificationTask.cancel();
    }

    private void startGpsManager() {
    	locationListener = new LocationListener() {
			@Override
			public void onLocationChanged(Location location) {
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
				request.setInterval(10000);
				request.setFastestInterval(10000);
				//request.setSmallestDisplacement(5);
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
				Log.v("game", "gps connection failed, "+result.toString());
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
