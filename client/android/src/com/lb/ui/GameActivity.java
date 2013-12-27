package com.lb.ui;

import org.json.JSONObject;

import com.lb.R;
import com.lb.api.API;
import com.lb.logic.ILocationUpdateServiceClient;
import com.lb.logic.LocationUpdateService;
import com.lb.model.Session;
import com.lb.model.User;
import com.lb.ui.TerritoryListFragment.onTerritoryListItemClickListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class GameActivity extends FragmentActivity implements ILocationUpdateServiceClient, onTerritoryListItemClickListener {

	private static Intent serviceIntent;
	private LocationUpdateService updateService;
	private User user;
	private static SupportMapFragment mapFragment;
	private static GoogleMap gMap;
	
	/** 遺産
	private static final int GET_LOCATION_INTERVAL = 5000; // msec
	private static final double TERRITORY_RADIUS = 10000; // m

	Player player;
	LocationLogic locationLogic;
	TimerLogic timerLogic;
	MapLogic mapLogic;
	RoomEntity roomEntity;
	TimerTask getLocationTask;
	TimerTask countLeftTimeTask;
	TimerTask getLeftTimeTask;
	Integer leftTime;
	Integer leftTerritory;
	List<LatLng> territoryList;**/

	private final ServiceConnection serviceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.v("game", "on service connected");
			updateService = ((LocationUpdateService.LocationUpdateBinder) service).getService();
            LocationUpdateService.setServiceClient(GameActivity.this);
            
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(GameActivity.this);
            if (prefs.getBoolean(PreferenceScreenActivity.PREF_KEY_BACKGROUND, true)) {
            	updateService.startUpdate();
            }else{
            	updateService.stopUpdate();
            }
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.v("game", "on service disconnected");
			updateService = null;
		}
		
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_territories:
			FragmentManager manager = getSupportFragmentManager();
			TerritoryListFragment territoryListFragment = (TerritoryListFragment) manager.findFragmentByTag("territoryList");
			if(territoryListFragment == null){
				territoryListFragment = new TerritoryListFragment();
				FragmentTransaction fragmentTransaction = manager.beginTransaction();
				fragmentTransaction.replace(R.id.mainLayout, territoryListFragment, "territoryList");
				fragmentTransaction.addToBackStack("map");
				fragmentTransaction.commit();
			}
			break;
		case R.id.action_settings:
			Intent intent = new Intent();
			intent.setClass(GameActivity.this,
					PreferenceScreenActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
		return false;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v("game", "life create");
		setContentView(R.layout.activity_game);
		
		// マップを表示
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = manager.beginTransaction();
		mapFragment = (SupportMapFragment) manager
				.findFragmentByTag("map");
		if (mapFragment == null) {
			mapFragment = SupportMapFragment.newInstance();
			mapFragment.setRetainInstance(true);
			fragmentTransaction.replace(R.id.mainLayout, mapFragment, "map");
			fragmentTransaction.commit();
		}

		/** 遺産
		 mapLogic = new MapLogic(this, mapFragment);

		// ロケーション送信
		locationLogic = new LocationLogic(this);
		locationLogic.setLocationListener(new LocationListener() {
			@Override
			public void onLocationChanged(Location location) {
				AuthLogic authLogic = new AuthLogic(getApplicationContext());
				API.postLocation(authLogic.getAuth(), location, new JsonHttpResponseHandler() {
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
		});

		// 一定間隔で位置情報を取得
		timerLogic = new TimerLogic(this);
		getLocationTask = timerLogic.create(new Runnable() {
			SparseArray<LocationMarker> locationMarkers = new SparseArray<LocationMarker>();

			@Override
			public void run() {
				if (player != null && player.getRoomId() > 0) {
					API.getRoomLocations(player.getRoomId(), new JsonHttpResponseHandler() {
								@Override
								public void onSuccess(JSONObject ret) {
									try {
										// ロケーションに紐づくユーザ情報を取得
										Log.v("game", "ret="+ret.toString());
										JSONArray users = ret.getJSONArray("members");
										SparseArray<JSONObject> membersMap = new SparseArray<JSONObject>();
										for (int i = 0; i < users.length(); i++) {
											JSONObject userObj = users.getJSONObject(i);
											int id = userObj.getInt("id");
											membersMap.put(userObj.getInt("id"), userObj);
											LocationMarker locationMarker = locationMarkers.get(id);
											if (locationMarker != null) {
												locationMarker.remove();
											}
										}
										
										// 位置マーカーを追加表示する
										JSONArray locations = ret.getJSONArray("locations");
										for (int i = 0; i < locations.length(); i++) {
											PlayerLocation playerLocation = new PlayerLocation(locations.getJSONObject(i));
											double lat = playerLocation.getLatitude();
											double lng = playerLocation.getLongitude();

											LocationMarker locationMarker = locationMarkers.get(playerLocation.getUserId());
											
											// ユーザの位置マーカーを保存するクラスがなかったら新しく作る
											if (locationMarker == null) {
												locationMarker = new LocationMarker();
												locationMarkers.put(playerLocation.getUserId(), locationMarker);
											}
											
											if(playerLocation.getUserId() == player.getUserId()){
												// プレーヤーの場所は常に表示
												Marker marker = mapLogic.addLocationMarker(lat, lng, player.getName(), playerLocation.getCreatedAt());
												locationMarker.addMarker(marker);
											}else{
												// テリトリー内のマーカーだけ追加・表示する
												if (territoryList == null){
													territoryList = new ArrayList<LatLng>();
												}
												for (int j = 0; j < territoryList.size(); j ++){
													LatLng latlng = territoryList.get(j);
													GeodeticCalculator geoCalc = new GeodeticCalculator();
													Ellipsoid ellipsoid = Ellipsoid.WGS84;
													GlobalPosition point = new GlobalPosition(lat,lng,0.0);
													GlobalPosition territoryPos = new GlobalPosition(latlng.latitude,latlng.longitude,0.0);
													double distance = geoCalc.calculateGeodeticCurve(ellipsoid, territoryPos, point).getEllipsoidalDistance();
													if(distance <= TERRITORY_RADIUS){
														Marker marker = mapLogic.addLocationMarker(lat, lng, membersMap.get(playerLocation.getUserId()).getString("name"), playerLocation.getCreatedAt());
														locationMarker.addMarker(marker);
														break;
													}
												}
											}
										}

										// 位置マーカー間に線を引く
										for (int i = 0; i < users.length(); i++) {
											JSONObject userObj = users.getJSONObject(i);
											LocationMarker locationMarker = locationMarkers.get(userObj.getInt("id"));
											if (locationMarker != null) {
												locationMarker.addLine(mapLogic.drawLine(locationMarker.getMarkers()));
											}
										}
									} catch (JSONException e) {
										Log.e("game", e.toString());
									}
								}

								@Override
								public void onFailure(Throwable throwable) {
									Log.v("game", "getRoomLocationsOnFailure="
											+ throwable);
								}
							});
				}
			}
		});
		timerLogic.start(getLocationTask, GET_LOCATION_INTERVAL);
		**/
		getUser();
		startAndBindService();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		getUser();
		startAndBindService();
		
		initMap();
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.v("game", "life resume");
		getUser();
		startAndBindService();
	}

	@Override
	public void onPause() {
		Log.v("game", "life pause");
		stopAndUnBindService();
		super.onPause();
	}
	
	@Override
	public void onDestroy() {
		Log.v("game", "life destroy");
		stopAndUnBindService();
		super.onDestroy();
/**
		// ロケーション殺す
		locationLogic.stop();

		// タイマー殺す
		timerLogic.cancel(getLocationTask);
		timerLogic.cancel(countLeftTimeTask);
		timerLogic.cancel(getLeftTimeTask);
		**/
		
	}

	/** ゲーム終了時の処理
	private void onGameEnd() {
		final Button hitButton = (Button) findViewById(R.id.hitButton);

		mapLogic.setOnLongClickListener(null);
		mapLogic.setOnClickListener(new OnMapClickListener() {

			private HitMarker hitMarker;

			@Override
			public void onMapClick(LatLng latlng) {
				if (hitMarker != null)
					hitMarker.remove();
				hitMarker = mapLogic.addHitMarker(latlng, 1000, 10000, 20000);
				hitButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						LatLng latlng = hitMarker.getPosition();
						AuthLogic authLogic = new AuthLogic(getApplicationContext());
							API.postHitLocation(authLogic.getAuth(), 0, latlng.latitude,
									latlng.longitude, 0,
									new JsonHttpResponseHandler() {
										@Override
										public void onSuccess(JSONObject json) {
											Intent intent = new Intent();
											intent.setClass(GameActivity.this,
													ResultActivity.class);
											startActivity(intent);
											finish();
										}

										@Override
										public void onFailure(
												Throwable throwable) {
											Log.v("game",
													"postHitLocationOnFailure="
															+ throwable);
										}
									});

					}
				});
			}
		});

		// ロケーション殺す
		locationLogic.stop();

		// タイマー殺す
		timerLogic.cancel(getLocationTask);
		timerLogic.cancel(countLeftTimeTask);
		timerLogic.cancel(getLeftTimeTask);
	}
	**/

	private void initMap() {
		gMap = mapFragment.getMap();
		if (gMap != null) {
			gMap.setMyLocationEnabled(true);
			UiSettings settings = gMap.getUiSettings();
			settings.setMyLocationButtonEnabled(true);

			// カメラを現在位置にフォーカスする
			gMap.setOnMyLocationChangeListener(new OnMyLocationChangeListener(){
				@Override
				public void onMyLocationChange(Location location) {
					gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
					gMap.setOnMyLocationChangeListener(null); // 一回移動したらリスナーを殺す
				}
			});

			// テリトリー作成
			gMap.setOnMapLongClickListener(new OnMapLongClickListener() {
				@Override
				public void onMapLongClick(LatLng latlng) {
					double radius = 10000;
					API.postTerritoryLocation(user, latlng.latitude, latlng.longitude, radius, new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(JSONObject json) {
							Log.v("game", "territory = "+ json.toString());
						}

						@Override
						public void onFailure(Throwable throwable) {
							Log.v("game","postHitLocationOnFailure="+ throwable);
						}
					});
					CircleOptions circleOptions = new CircleOptions();
					circleOptions.center(latlng);
					circleOptions.strokeWidth(5);
					circleOptions.radius(radius);
					circleOptions.strokeColor(Color.argb(200, 0, 255, 0));
					circleOptions.fillColor(Color.argb(50, 0, 255, 0));
					Circle circle = gMap.addCircle(circleOptions);
				}
			});
		}
	}
	
	private void startAndBindService() {
		serviceIntent = new Intent(getActivity(), LocationUpdateService.class);
		getActivity().startService(serviceIntent);
        getActivity().bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        Session.setIsBound(true);
	}
	
	private void stopAndUnBindService() {
        if (Session.getIsBound()) {
    		Log.v("game", "unbind");
    	    getActivity().unbindService(serviceConnection);
            Session.setIsBound(false);
        }

        if(!Session.getIsStarted()) {
    		Log.v("game", "stop");
        	getActivity().stopService(serviceIntent);	
        }
	}
	
	private void getUser() {
		user = Session.getUser();
		if(user == null) {
			Intent intent = new Intent();
			intent.setClass(this, SignupActivity.class);
			startActivity(intent);
			finish();
		}
	}
	
	@Override
	public void onLocationUpdate(Location loc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopLogging() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Activity getActivity() {
		return this;
	}

	@Override
	public void onTerritoryListItemClickListener(long latitude, long longitude) {
		FragmentManager manager = getSupportFragmentManager();
		manager.popBackStack("map", FragmentManager.POP_BACK_STACK_INCLUSIVE);
		Log.i("game", "latitude="+latitude+", longitude="+longitude);
	}

	
}
