package com.lb.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.lb.R;
import com.lb.api.API;
import com.lb.model.Session;
import com.lb.model.Utils;
import com.lb.ui.MapFragment.OnGoogleMapFragmentListener;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

public class LocationHistoryActivity extends FragmentActivity implements OnGoogleMapFragmentListener {

	private GoogleMap gMap;
	private ProgressDialog mProgressDialog;
	private Calendar mCal;
	private LocationClient mLocationClient;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location_history);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		mCal = Calendar.getInstance();
		
		final Button bt1 = (Button) findViewById(R.id.bt_date);
		bt1.setText(getDateString(mCal));
		bt1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DatePickerDialog datePickerDialog = new DatePickerDialog(LocationHistoryActivity.this, new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						Calendar cal = Calendar.getInstance();
						cal.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
						mCal = cal;
						bt1.setText(getDateString(mCal));
						getLocationHistory(mCal);
					}
					
				}, mCal.get(Calendar.YEAR), mCal.get(Calendar.MONTH), mCal.get(Calendar.DAY_OF_MONTH));
				datePickerDialog.setTitle("ロケーション履歴");
				datePickerDialog.show();
			}
			
		});
		
		getLocationHistory(mCal);
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
		if(gMap == null) {
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
			
		}
	}
	
	private void getLocationHistory(Calendar cal) {
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		df.setTimeZone(cal.getTimeZone());
		String date = df.format(cal.getTime());
		
		API.getUserLocations(Session.getUser(), date, new JsonHttpResponseHandler() {
			@Override
			public void onStart() {
				gMap.clear();
				if (mProgressDialog == null) mProgressDialog = Utils.createProgressDialog(LocationHistoryActivity.this);
				mProgressDialog.show();
			}
			
			
			@Override
			public void onSuccess(JSONArray jsonArray) {				
				if(jsonArray.length() > 0) {
					showLocations(jsonArray);
				}else{
					Toast.makeText(LocationHistoryActivity.this, getDateString(mCal) + "のロケーション履歴はありません", Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onFailure(Throwable throwable) {
				Toast.makeText(LocationHistoryActivity.this, getDateString(mCal) + "のロケーション履歴はありません", Toast.LENGTH_LONG).show();
				Log.v("game","getUserLocationsOnFailure="+ throwable);
			}
			
			
			@Override
			public void onFinish() {
				mProgressDialog.dismiss();
			}
		});
	}
	
	private void showLocations(JSONArray jsonArray) {
		PolylineOptions lineOpts = new PolylineOptions();
		for(int i = 0; i < jsonArray.length(); i ++) {
			JSONObject json;
			try {
				json = jsonArray.getJSONObject(i);
				LatLng latlng = new LatLng(json.getDouble("latitude"), json.getDouble("longitude")); 
				
				lineOpts.add(latlng);
				lineOpts.color(Color.argb(50, 0, 0, 255));
				lineOpts.width(5);
				lineOpts.geodesic(true);
				
				MarkerOptions markerOpts = new MarkerOptions();
				markerOpts.position(latlng);
				gMap.addMarker(markerOpts);
			} catch (JSONException e) {
				e.printStackTrace();
			} 
		}
		gMap.addPolyline(lineOpts);
	}
	
	private String getDateString(Calendar cal) {
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		df.setTimeZone(cal.getTimeZone());
		return df.format(cal.getTime());
	}
}
