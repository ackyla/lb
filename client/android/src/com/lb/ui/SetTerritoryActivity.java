package com.lb.ui;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import net.vvakame.util.jsonpullparser.JsonFormatException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
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
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;

public class SetTerritoryActivity extends FragmentActivity implements OnGoogleMapFragmentListener, OnCharacterListFragmentItemClickListener {
	private GoogleMap gMap;
	private Circle mCircle;
	private ProgressDialog mProgressDialog;
	private AlertDialog mSelectDialog;
	private Character mCharacter;
	
	@Override
	public void onResume() {
		super.onResume();
		
		if(mSelectDialog == null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(SetTerritoryActivity.this);
			builder.setTitle("設置するテリトリーを選んで下さい");
			builder.setView(getLayoutInflater().inflate(R.layout.character_list_dialog, null));
			builder.setCancelable(false);
			builder.setNeutralButton("キャンセル", new AlertDialog.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
					dialog.cancel();
				}
				
			});
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
					mCircle = addTerritory(new LatLng(location.getLatitude(), location.getLongitude()), 100.0);
					gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 12));
					gMap.setOnMyLocationChangeListener(null); // 一回移動したらリスナーを殺す
				}
			});
			
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
							addTerritory(new LatLng(json.getDouble("latitude"), json.getDouble("longitude")), json.getDouble("radius"));
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
					if(mCircle != null) mCircle.setCenter(pos.target);
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
							API.createTerritory(Session.getUser(), mCircle.getCenter().latitude, mCircle.getCenter().longitude, mCharacter.getId(), new JsonHttpResponseHandler() {
								@Override
								public void onStart() {
									if (mProgressDialog == null) mProgressDialog = Utils.createProgressDialog(SetTerritoryActivity.this);
									mProgressDialog.show();
								}
								
								
								@Override
								public void onSuccess(JSONObject json) {
									try {
										Utils.updateSessionUserInfo(UserGen.get(json.getJSONObject("user").toString()));
										addTerritory(mCircle.getCenter(), mCircle.getRadius());
									} catch (IOException e) {
										e.printStackTrace();
									} catch (JsonFormatException e) {
										e.printStackTrace();
									} catch (JSONException e) {
										e.printStackTrace();
									}
									Toast.makeText(SetTerritoryActivity.this, "テリトリーを設置しました", Toast.LENGTH_LONG).show();
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
	
	public Circle addTerritory(LatLng latlng, Double radius) {
		CircleOptions circleOptions = new CircleOptions();
		circleOptions.center(latlng);
		circleOptions.strokeWidth(5);
		circleOptions.radius(radius);
		circleOptions.strokeColor(Color.argb(200, 0, 255, 0));
		circleOptions.fillColor(Color.argb(50, 0, 255, 0));
		return gMap.addCircle(circleOptions);
	}

	@Override
	public void onClickCharacterListItem(Character character) {
		mCharacter = character;
		mCircle.setRadius(character.getRadius());
		mSelectDialog.cancel();
	}
}
