package com.lb.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lb.R;
import com.lb.api.API;
import com.lb.logic.ILocationUpdateServiceClient;
import com.lb.logic.LocationUpdateService;
import com.lb.model.Session;
import com.lb.model.User;
import com.lb.model.Utils;
import com.lb.ui.MapFragment.OnGoogleMapFragmentListener;
import com.lb.ui.NotificationListFragment.OnNotificationListFragmentItemClickListener;
import com.lb.ui.TerritoryDetailFragment.OnTerritoryDetailFragmentListener;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

public class GameActivity extends FragmentActivity implements ILocationUpdateServiceClient, OnGoogleMapFragmentListener, OnTerritoryDetailFragmentListener, OnNotificationListFragmentItemClickListener {

	private static Intent serviceIntent;
	private LocationUpdateService updateService;
	private User user;
	private GoogleMap gMap;
    private FragmentTabHost mTabHost;
    private ProgressDialog mProgressDialog;
    private GameDropdownAdapter mGameDropdownAdapter;
	
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
		case R.id.action_settings:
			Intent intent = new Intent();
			intent.setClass(GameActivity.this,
					PreferenceScreenActivity.class);
			startActivity(intent);
			break;
		case R.id.action_reload:
			refreshMap();
			break;
		default:
			break;
		}
		return false;
	}

    private void configureActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        GameDropdownData item = new GameDropdownData();
        item.setTitle(user.getName());
        item.setMessage(" Lv" + user.getLevel());
        item.setGpsPoint(user.getGps_Point());
        item.setGpsPointMax(user.getGps_Point_Limit());
        item.setImage(R.drawable.ic_launcher);
        
        GameDropdownData item2 = new GameDropdownData();
        item2.setTitle("ロケーション履歴");
        item2.setImage(android.R.drawable.ic_menu_recent_history);
        
        mGameDropdownAdapter = new GameDropdownAdapter(this);
        mGameDropdownAdapter.add(item);
        mGameDropdownAdapter.add(item2);
        actionBar.setListNavigationCallbacks(mGameDropdownAdapter, new OnNavigationListener() {

			@Override
			public boolean onNavigationItemSelected(int itemPosition, long itemId) {
				if(itemPosition == 0) {
					
				}else{
					getActionBar().setSelectedNavigationItem(0);
				}
				return true;
			}
        	
        });
    }
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v("game", "life create");
		setContentView(R.layout.activity_game);

		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.content);

        addTab("tab1", "マップ", MapFragment.class);
        addTab("tab2", "テリトリー", TerritoryTopFragment.class);
        addTab("tab3", "通知", NotificationListFragment.class);
        
        mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				getCurrentFragment().clearBackStack();
			}
        });
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
		configureActionBar();
	}
	
	@Override
	public void onLocationUpdate(Location loc) {
		if(mGameDropdownAdapter != null && user != null && user.getGps_Point() < user.getGps_Point_Limit()) {
			user.setGps_Point(user.getGps_Point() + 1);
			mGameDropdownAdapter.refreshGpsPoint(0, user.getGps_Point(), user.getGps_Point_Limit());
			mGameDropdownAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onStopLogging() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Activity getActivity() {
		return this;
	}

	public void refreshMap() {
		if(gMap != null) {
			gMap.clear();
			
			// テリトリーを表示
			API.getUserTerritories(Session.getUser(), new JsonHttpResponseHandler() {
				
				@Override
				public void onStart() {
					if (mProgressDialog == null) mProgressDialog = Utils.createProgressDialog(GameActivity.this);
					mProgressDialog.show();
				}
				
				@Override
				public void onSuccess(JSONArray jsonArray) {
					for(int i = 0; i < jsonArray.length(); i ++) {
						try {
							JSONObject json = jsonArray.getJSONObject(i);

							// TODO
							TerritoryMarker territoryMarker = new TerritoryMarker();
							territoryMarker.setCenter(new LatLng(json.getDouble("latitude"), json.getDouble("longitude")));
							territoryMarker.setRadius(json.getDouble("radius"));
							territoryMarker.setColor(0, 255, 0);
							territoryMarker.setTitle("territory_" + json.getInt("id"));
							territoryMarker.setSnippet("7,200,000,000人発見しました");
							territoryMarker.addTo(gMap);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}

				@Override
				public void onFailure(Throwable throwable) {
					Log.i("game","getUserTerritoryListOnFailure="+ throwable);
				}
				
				@Override
				public void onFinish() {
					mProgressDialog.dismiss();
				}
			});
		
		}
	}
	
	private class TerritoryInfoWindowAdapter implements InfoWindowAdapter {
		private final View mWindow;
		
		public TerritoryInfoWindowAdapter() {
			mWindow = getLayoutInflater().inflate(R.layout.territory_info_window, null);
		}
		
		@Override
		public View getInfoContents(Marker marker) {
			showInfoWindow(marker, mWindow);
			return mWindow;
		}

		@Override
		public View getInfoWindow(Marker marker) {
			return null;
		}
		
		private void showInfoWindow(Marker marker, View v) {
			
			TextView title = (TextView) v.findViewById(R.id.title);
			title.setText(marker.getTitle());
			
			TextView snippet = (TextView) v.findViewById(R.id.snippet);
			snippet.setText(marker.getSnippet());
		}
	}
	
	@Override
	public void onMapReady(GoogleMap map) {
		if(gMap == null) {
			gMap = map;
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
			
			// マーカーに吹き出しつける
			gMap.setInfoWindowAdapter(new TerritoryInfoWindowAdapter());
			
			// 吹き出しクリックした時
			gMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

				@Override
				public void onInfoWindowClick(Marker marker) {
					
				}
				
			});
			
			refreshMap();
			
			Intent intent = getIntent();
			Integer notificationId = intent.getIntExtra("notification_id", 0);
			
			if (notificationId > 0) {
				API.readNotification(Session.getUser(), notificationId, new JsonHttpResponseHandler() {

					@Override
					public void onSuccess(JSONObject json) {
						//NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);	
						//manager.cancel(mId);
					}

					@Override
					public void onFailure(Throwable throwable) {
						Log.i("game","getUserTerritoryListOnFailure="+ throwable);
					}
				});
			}
		}
	}
	
    @Override
    public void onBackPressed() {
        if (!getCurrentFragment().popBackStack()) {
            // タブ内FragmentのBackStackがない場合は終了
            super.onBackPressed();
        }
    }
	
    /**
     * タブ追加.
     * @param tag タグ
     * @param indector タブウィジェット表示ラベル
     * @param clazz Fragmentクラス
     */
    private void addTab(String tag, String indector, Class<? extends Fragment> clazz) {

        TabSpec tabSpec = mTabHost.newTabSpec(tag).setIndicator(indector);

        // TabRootFragmentに渡すことでクラス名から初期Fragmentを決定
        Bundle args = new Bundle();
        args.putString("root", clazz.getName());

        mTabHost.addTab(tabSpec, TabRootFragment.class, args);

    }

    /**
     * カレントFragment取得.
     * @return TabRootFragment
     */
    private TabRootFragment getCurrentFragment() {
        return (TabRootFragment) getSupportFragmentManager().findFragmentById(R.id.content);
    }

	@Override
	public void onClickShowTerritoryButton(Double latitude, Double longitude) {		
		mTabHost.setCurrentTabByTag("tab1");		
		refreshMap();
		gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 12));
	}

	@Override
	public void onClickNotificationListItem(Double latitude, Double longitude, Integer type, String title, String message) {
		mTabHost.setCurrentTabByTag("tab1");
		refreshMap();
		gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 12));
		
		if (type == NotificationData.TYPE_DETECTED) {
			MarkerOptions markerOpt = new MarkerOptions();
			markerOpt.title(title);
			markerOpt.snippet(message);
			markerOpt.position(new LatLng(latitude, longitude));
			//BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(android.R.drawable.ic_dialog_info);
			BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
			markerOpt.icon(icon);
			Marker marker = gMap.addMarker(markerOpt);
			marker.showInfoWindow();
		}
	}
}
