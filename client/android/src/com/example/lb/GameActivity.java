package com.example.lb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.loopj.android.http.JsonHttpResponseHandler;

import dao.user.UserEntity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import api.API;

public class GameActivity extends FragmentActivity {
	
	GoogleMap map;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v("life", "game create");
		setContentView(R.layout.activity_game);
		
    	FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = manager.beginTransaction();
		SupportMapFragment mapFragment = (SupportMapFragment)manager.findFragmentByTag("map");
		if(mapFragment == null){
			mapFragment = SupportMapFragment.newInstance();
			mapFragment.setRetainInstance(true);
			fragmentTransaction.replace(R.id.mapLayout, mapFragment, "map");
			fragmentTransaction.commit();
		}
		
		final EditText editText = (EditText)findViewById(R.id.editText1);
		Button button1 = (Button)findViewById(R.id.button1);
		
		button1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				displayChat(editText.getText().toString());
			}
		});
		
		final LinearLayout missionView = (LinearLayout)findViewById(R.id.missionView);
		Button button2 = (Button)findViewById(R.id.button2);
		
		button2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				missionView.setVisibility(View.VISIBLE);
			}
			
		});
		
		Button button3 = (Button)findViewById(R.id.button3);
		
		button3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				missionView.setVisibility(View.GONE);
			}
			
		});
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.v("life", "game resume");
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.v("life", "game destroy");
	}
	
	private void displayChat(String text) {
		View v = getLayoutInflater().inflate(R.layout.layout_chat, null);
		TextView textView = (TextView)v.findViewById(R.id.textView1);
		textView.setText(text);
		
		LinearLayout chatList = (LinearLayout)findViewById(R.id.chatList);
		chatList.addView(v);
	}
}
