package com.example.lb;

import logic.db.DBLogic;
import logic.location.LocationLogic;
import logic.user.UserLogic;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.lb.R.id;
import com.google.android.gms.location.LocationListener;

public class MainActivity extends FragmentActivity {
	
	//GoogleMap gMap;
	private LocationLogic locationLogic;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent();
		intent.setClass(this, SignupActivity.class);
		startActivity(intent);
		return true;
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		locationLogic = new LocationLogic(this);
		locationLogic.setLocationListener(new LocationListener() {

			@Override
			public void onLocationChanged(Location location) {
				locationLogic.post(location);
			}
			
		});
		
		/*Button bt = (Button)findViewById(R.id.button1);
		bt.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				FragmentManager manager = getSupportFragmentManager();
				SupportMapFragment mapFragment01 = (SupportMapFragment)manager.findFragmentByTag("map01");
				gMap = mapFragment01.getMap();
				
				double[] latArray = {mLatitude, mLatitude-0.5, mLatitude-1.0, mLatitude};
				double[] lonArray = {mLongitude, mLongitude, mLongitude-1.0, mLongitude+2.2};
				
				LatLng pos = new LatLng(mLatitude, mLongitude);
				
				CameraPosition cameraPos = new CameraPosition.Builder()
					.target(pos).zoom(15.5f)
					.bearing(0).tilt(25).build();
				gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPos));
				
				MarkerOptions options = new MarkerOptions();
				PolylineOptions pOptions = new PolylineOptions();
				pOptions.color(0xcc00ffff);
				pOptions.width(10);
				pOptions.geodesic(true); // 測地線で表示
				
				
				for(int i = 0; i < latArray.length; i++){
					pos = new LatLng(latArray[i], lonArray[i]);
					options.position(pos);
					options.title("なう");
					pOptions.add(pos);
					gMap.addMarker(options);
				}
				
				gMap.addPolyline(pOptions);
				
				SupportMapFragment mapFragment02 = (SupportMapFragment)manager.findFragmentByTag("map02");
				gMap = mapFragment02.getMap();
				
				pos = new LatLng(-33.87365, 151.20689);
				options = new MarkerOptions();
				options.position(pos);
				options.title("みっしょん");
				gMap.addMarker(options);
				
				cameraPos = new CameraPosition.Builder()
					.target(pos).zoom(15.5f)
					.bearing(0).tilt(25).build();
				gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPos));
			}			
		});

		FragmentManager manager = getSupportFragmentManager();
		SupportMapFragment mapFragment01 = (SupportMapFragment)manager.findFragmentByTag("map01");
		SupportMapFragment mapFragment02 = (SupportMapFragment)manager.findFragmentByTag("map02");
		
		if(mapFragment01 == null){
			mapFragment01 = SupportMapFragment.newInstance();
			mapFragment02 = SupportMapFragment.newInstance();
			
    		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
    		fragmentTransaction.add(R.id.map01, mapFragment01, "map01");
    		fragmentTransaction.add(R.id.map02, mapFragment02, "map02");
			fragmentTransaction.commit();
		}*/
	}

	@Override
	public void onStart(){
		super.onStart();
		
		UserLogic userLogic = new UserLogic(this);
		
		if(!userLogic.checkRegister()){
			Intent intent = new Intent();
			intent.setClass(this, SignupActivity.class);
			startActivity(intent);
		}
		
		locationLogic.start();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		locationLogic.stop();
	}
}
