package com.example.lb;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

public class MainActivity extends FragmentActivity implements LocationListener{
	
	GoogleMap mMap;
	LocationManager mLocationManager;
	double mLatitude;
	double mLongitude;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE); // 低精度
		criteria.setPowerRequirement(Criteria.POWER_LOW); // 低消費電力
		String provider = mLocationManager.getBestProvider(criteria, true);
		mLocationManager.requestLocationUpdates(provider, 0, 0, this);
		TextView tv = (TextView) findViewById(R.id.textView2);
		tv.setText(provider);
		
		Button bt = (Button)findViewById(R.id.button1);
		bt.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.setAction(Intent.ACTION_VIEW);
				i.setClassName("com.google.android.apps.maps", "com.google.android.maps.driveabout.app.NavigationActivity");

				Uri uri = Uri.parse("google.navigation:///?ll="+mLatitude+","+mLongitude);
				i.setData(uri);
				startActivity(i);
			}			
		});
	
		SupportMapFragment mapFragment = SupportMapFragment.newInstance(); 
		//map = ((SupportMapFragment)getSupportFragmentManager().).getMap();
		//mMap = ((SupportMapFragment) map01).getMap();
		//getSupportFragmentManager().beginTransaction().add(android.R.id.content, mMapFragment, TAG_MAP_FRAGMENT)
        //.commit();
		
		Fragment map01 = new MapFragment();
		Fragment map02 = new MapFragment();
    	FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.map01, map01);
		fragmentTransaction.replace(R.id.map02, map02);
		fragmentTransaction.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onLocationChanged(Location location) {
		TextView tv = (TextView) findViewById(R.id.text_view_01);
		mLatitude = location.getLatitude();
		mLongitude = location.getLongitude();
        tv.setText("("+location.getLatitude() + ","+location.getLongitude()+")");
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

}
