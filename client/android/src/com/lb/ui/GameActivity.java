package com.lb.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.vvakame.util.jsonpullparser.JsonFormatException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
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
import com.lb.model.Territory;
import com.lb.model.TerritoryGen;
import com.lb.model.User;
import com.lb.model.UserGen;
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
    private Integer mTerritoryId = 0;
    private LocationClient mLocationClient;
    private List<TerritoryMarker> mTerritoryMarkerList;
    private int mTerritoryMarkerListIndex = 0;
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
		getMenuInflater().inflate(R.menu.game, menu);
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
		case R.id.action_back:
			backTerritory();
			break;
		case R.id.action_forward:
			forwardTerritory();
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
        item.setMessage("Lv" + user.getLevel() + " (" + user.getExp() + "/100)");
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
					Intent intent = new Intent();
					intent.setClass(getActivity(), LocationHistoryActivity.class);
					startActivity(intent);
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
        addTab("tab2", "テリトリー", TerritoryListFragment.class);
        addTab("tab3", "通知", NotificationListFragment.class);
        
        mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				getCurrentFragment().clearBackStack();
			}
        });

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
		configureActionBar();
	}
	
	@Override
	public void onLocationUpdate(JSONObject json) {
		try {
			user = Utils.updateSessionUserInfo(UserGen.get(json.getJSONObject("user").toString()));
			refreshGameDropdownAdapter();	
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JsonFormatException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onStopLogging() {

	}

	@Override
	public Activity getActivity() {
		return this;
	}

	public void refreshMap() {
		if(gMap != null) {
			gMap.clear();
			if(mTerritoryMarkerList == null) mTerritoryMarkerList = new ArrayList<TerritoryMarker>();
			mTerritoryMarkerList.clear();
			
			// テリトリーを表示
			API.getUserTerritories(Session.getUser(), new JsonHttpResponseHandler() {
				
				@Override
				public void onStart() {
					if (mProgressDialog == null) mProgressDialog = Utils.createProgressDialog(GameActivity.this);
					mProgressDialog.show();
				}
				
				@Override
				public void onSuccess(JSONArray jsonArray) {
					List<Territory> territories = new ArrayList<Territory>();
					
					try {
						territories = TerritoryGen.getList(jsonArray.toString());
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (JsonFormatException e1) {
						e1.printStackTrace();
					}
					
					for(int i = 0; i < territories.size(); i ++) {
						Territory territory = territories.get(i);
						TerritoryMarker territoryMarker = new TerritoryMarker();
						territoryMarker.setCenter(new LatLng(territory.getLatitude(), territory.getLongitude()));
						territoryMarker.setRadius(territory.getRadius());
						if (Utils.parseStringToDate(territory.getExpirationDate()).compareTo(Calendar.getInstance().getTime()) <= 0) {
							territoryMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
							territoryMarker.setColor(50, 50, 50);
						}else{
							territoryMarker.setColor(0, 255, 0);
						}
						territoryMarker.setTitle("territory_" + territory.getId());
						territoryMarker.setSnippet(territory.getDetectionCount() + "人発見しました");
						territoryMarker.addTo(gMap);
						if(territory.getId() == mTerritoryId) {
							territoryMarker.showInfoWindow();
							mTerritoryId = 0;
						}
						
						mTerritoryMarkerList.add(territoryMarker);
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
	public void onMapReady(GoogleMap map, final View v) {
		if(gMap == null && map != null) {
			v.setVisibility(View.INVISIBLE);
			gMap = map;
			gMap.setMyLocationEnabled(true);
			UiSettings settings = gMap.getUiSettings();
			settings.setMyLocationButtonEnabled(true);
			
			// カメラを現在位置にフォーカスする
			mLocationClient = new LocationClient(getApplicationContext(), new ConnectionCallbacks() {
				@Override
				public void onConnected(Bundle bundle) {					
					Location location = mLocationClient.getLastLocation();
					LatLng latlng = Utils.getDefaultLatLng();
					if(location != null) latlng = new LatLng(location.getLatitude(), location.getLongitude());
					gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 12));
					v.setVisibility(View.VISIBLE);
					mLocationClient.disconnect();
				}

				@Override
				public void onDisconnected() {
				}
	    	}, new OnConnectionFailedListener() {
				@Override
				public void onConnectionFailed(ConnectionResult result) {
					v.setVisibility(View.VISIBLE);
				}
			});
	    	mLocationClient.connect();

			// マーカーに吹き出しつける
			gMap.setInfoWindowAdapter(new TerritoryInfoWindowAdapter());
			
			// 吹き出しクリックした時
			gMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

				@Override
				public void onInfoWindowClick(Marker marker) {
				}
				
			});
			
			refreshMap();

			// ノーティフィケーションから来た時
			Intent intent = getIntent();
			Integer notificationId = intent.getIntExtra("notification_id", 0);
			Double latitude = intent.getDoubleExtra("latitude", 35.0);
			Double longitude = intent.getDoubleExtra("longitude", 135.8);
			String title = intent.getStringExtra("title");
			String message = intent.getStringExtra("message");
			Integer type = intent.getIntExtra("notification_type", NotificationData.TYPE_DETECTED);
			boolean read = intent.getBooleanExtra("read", true);
			Integer territoryId = intent.getIntExtra("territory_id", 0);
			if (notificationId > 0) onClickNotificationListItem(notificationId, latitude, longitude, type, title, message, read, territoryId);
		}
	}
	
    @Override
    public void onBackPressed() {
        if (!getCurrentFragment().popBackStack()) {
            // タブ内FragmentのBackStackがない場合は何もしない
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
	public void onClickNotificationListItem(Integer id, Double latitude, Double longitude, Integer type, String title, String message, boolean read, Integer territoryId) {
		mTabHost.setCurrentTabByTag("tab1");
		
		mTerritoryId = territoryId;
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
		
		// 既読にする
		if(!read) API.readNotification(Session.getUser(), id, null);
	}

	@Override
	public void onSupply() {
		refreshGameDropdownAdapter();
	}
	
	private void refreshGameDropdownAdapter() {
		if(mGameDropdownAdapter != null) {
			mGameDropdownAdapter.refreshGpsPoint(0, user.getGps_Point(), user.getGps_Point_Limit());
			mGameDropdownAdapter.notifyDataSetChanged();
		}
	}
	
	private void forwardTerritory() {
		if(gMap != null && mTerritoryMarkerList != null) {
			TerritoryMarker tm = mTerritoryMarkerList.get(mTerritoryMarkerListIndex);
			gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(tm.getCenter(), 12));			
			mTerritoryMarkerListIndex ++;
			if(mTerritoryMarkerListIndex >= mTerritoryMarkerList.size()) mTerritoryMarkerListIndex = 0;
		}
	}
	
	private void backTerritory() {
		if(gMap != null && mTerritoryMarkerList != null) {
			TerritoryMarker tm = mTerritoryMarkerList.get(mTerritoryMarkerListIndex);
			gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(tm.getCenter(), 12));
			mTerritoryMarkerListIndex --;
			if(mTerritoryMarkerListIndex < 0) mTerritoryMarkerListIndex = mTerritoryMarkerList.size()-1;
		}
		
	}
}
