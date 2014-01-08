package com.lb.ui;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import net.vvakame.util.jsonpullparser.JsonFormatException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.lb.R;
import com.lb.api.API;
import com.lb.model.Character;
import com.lb.model.CharacterGen;
import com.lb.model.Session;
import com.lb.model.User;
import com.lb.model.UserGen;
import com.lb.model.Utils;
import com.lb.ui.CharacterListFragment.OnCharacterListFragmentItemClickListener;
import com.lb.ui.MapFragment.OnGoogleMapFragmentListener;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Rect;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;

public class SetTerritoryActivity extends FragmentActivity implements OnGoogleMapFragmentListener, OnCharacterListFragmentItemClickListener {
	private GoogleMap gMap;
	private TerritoryMarker mTerritoryMarker;
	private Circle mDistance;
	private ProgressDialog mProgressDialog;
	private AlertDialog mSelectDialog;
	private Character mCharacter;
	private LocationClient mLocationClient;
	
	@Override
	public void onResume() {
		super.onResume();
		
		if(mSelectDialog == null) {
			LinearLayout v = (LinearLayout) getLayoutInflater().inflate(R.layout.character_list_dialog, null);
			Button bt = (Button) v.findViewById(R.id.bt_cancel);
			bt.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (mSelectDialog != null) {
						finish();
						mSelectDialog.cancel();
					}
				}
				
			});
			
			AlertDialog.Builder builder = new AlertDialog.Builder(SetTerritoryActivity.this);
			builder.setTitle("設置するテリトリーを選んで下さい");
			builder.setMessage("残り陣力: "+Session.getUser().getGps_Point());
			builder.setView(v);
			builder.setCancelable(false);
			/*builder.setNegativeButton("キャンセル", new AlertDialog.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
					dialog.cancel();
				}
				
			});*/
			
			mSelectDialog = builder.create();
			mSelectDialog.show();
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_territory);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction tran = manager.beginTransaction();
		Fragment fragment = manager.findFragmentByTag("map");
		if(fragment == null) fragment = new MapFragment();
		fragment.setRetainInstance(false);
		tran.replace(R.id.map, fragment, "map");
		tran.commit();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	        NavUtils.navigateUpFromSameTask(this);
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
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
					mTerritoryMarker = addTerritory(latlng, 100.0, BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
					mDistance = addDistance(latlng, 10000.0);
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
			
			// テリトリーを表示
			API.getUserTerritories(Session.getUser(), new JsonHttpResponseHandler() {
				@Override
				public void onStart() {
					if (mProgressDialog == null) mProgressDialog = Utils.createProgressDialog(SetTerritoryActivity.this);
					mProgressDialog.show();
				}
				
				@Override
				public void onSuccess(JSONArray jsonArray) {
					for(int i = 0; i < jsonArray.length(); i ++) {
						try {
							JSONObject json = jsonArray.getJSONObject(i);
							addTerritory(new LatLng(json.getDouble("latitude"), json.getDouble("longitude")), json.getDouble("radius"), BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
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
			
			// 設置場所移動
			gMap.setOnCameraChangeListener(new OnCameraChangeListener() {
				@Override
				public void onCameraChange(CameraPosition pos) {
					if(mTerritoryMarker != null) mTerritoryMarker.updateCenter(pos.target);
				}
			});
			
			// テリトリー設置
			Button button = (Button) findViewById(R.id.territory_set_button);
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					AlertDialog.Builder builder = new AlertDialog.Builder(SetTerritoryActivity.this);
					builder.setTitle("テリトリーを設置します");
					builder.setMessage("消費陣力: " + mCharacter.getCost());
					builder.setPositiveButton("する", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							API.createTerritory(Session.getUser(), mTerritoryMarker.getCenter().latitude, mTerritoryMarker.getCenter().longitude, mCharacter.getId(), new JsonHttpResponseHandler() {
								@Override
								public void onStart() {
									if (mProgressDialog == null) mProgressDialog = Utils.createProgressDialog(SetTerritoryActivity.this);
									mProgressDialog.show();
								}
								
								
								@Override
								public void onSuccess(JSONObject json) {
									try {
										Utils.updateSessionUserInfo(UserGen.get(json.getJSONObject("user").toString()));
										addTerritory(mTerritoryMarker.getCenter(), mTerritoryMarker.getRadius(), BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
									} catch (IOException e) {
										e.printStackTrace();
									} catch (JsonFormatException e) {
										e.printStackTrace();
									} catch (JSONException e) {
										e.printStackTrace();
									}
									Toast.makeText(SetTerritoryActivity.this, "テリトリーを設置しました", Toast.LENGTH_LONG).show();
									
									mSelectDialog.setMessage("残り陣力: " + Session.getUser().getGps_Point());
									mSelectDialog.show();
								}

								@Override
								public void onFailure(Throwable throwable) {
									Toast.makeText(SetTerritoryActivity.this, "テリトリーを設置できませんでした", Toast.LENGTH_LONG).show();
									Log.v("game","postHitLocationOnFailure="+ throwable);
								}
								
								
								@Override
								public void onFinish() {
									mProgressDialog.dismiss();
								}
							});
						}
					});

					builder.setNegativeButton("しない", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
						}
					});
					builder.create().show();
				}
				
			});
		}
	}
	
	public TerritoryMarker addTerritory(LatLng latlng, Double radius, BitmapDescriptor icon) {
		TerritoryMarker tm = new TerritoryMarker();
		tm.setIcon(icon);
		tm.setCenter(latlng);
		tm.setRadius(radius);
		tm.setColor(0, 255, 0);
		tm.addTo(gMap);
		return tm;
		/*CircleOptions circleOptions = new CircleOptions();
		circleOptions.center(latlng);
		circleOptions.strokeWidth(5);
		circleOptions.radius(radius);
		circleOptions.strokeColor(Color.argb(200, 0, 255, 0));
		circleOptions.fillColor(Color.argb(50, 0, 255, 0));
		return gMap.addCircle(circleOptions);*/
	}
	
	public Circle addDistance(LatLng latlng, Double radius) {
		CircleOptions circleOptions = new CircleOptions();
		circleOptions.center(latlng);
		circleOptions.strokeWidth(2);
		circleOptions.radius(radius);
		circleOptions.strokeColor(Color.argb(200, 0, 255, 255));
		circleOptions.fillColor(Color.argb(50, 0, 255, 255));
		return gMap.addCircle(circleOptions);
	}

	@Override
	public void onClickCharacterListItem(Character character) {
		if(character.getCost() > Session.getUser().getGps_Point()) {
			Toast.makeText(SetTerritoryActivity.this, "陣力が足りません", Toast.LENGTH_LONG).show();
		}else{
			mCharacter = character;
			mTerritoryMarker.updateRadius(character.getRadius());
			mDistance.setRadius(character.getDistance());
			mSelectDialog.cancel();
		}
	}
}
