package com.example.lb;

import org.json.JSONException;
import org.json.JSONObject;

import logic.location.LocationLogic;
import logic.map.MapLogic;
import logic.user.UserLogic;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.loopj.android.http.JsonHttpResponseHandler;

import dao.room.RoomEntity;
import dao.user.UserEntity;

import android.location.Location;
import android.os.Bundle;
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
import api.API;

public class GameActivity extends FragmentActivity {

	LocationLogic locationLogic;
	MapLogic mapLogic;
	UserEntity userEntity;
	RoomEntity roomEntity;
	
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
		
		// ユーザ情報と部屋の情報を取得
		UserLogic userLogic = new UserLogic(this);
		userEntity = userLogic.getUser();
		API.getUserInfo(userEntity.getUserId(), new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONObject object) {
				// TODO †phelrineがuserEntityを殺した†
				try {
					JSONObject roomObject = object.getJSONObject("room");
					roomEntity = new RoomEntity(roomObject);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
				}
			}
		});
		
		// マップを表示
    	FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = manager.beginTransaction();
		SupportMapFragment mapFragment = (SupportMapFragment)manager.findFragmentByTag("map");
		if(mapFragment == null){
			mapFragment = SupportMapFragment.newInstance();
			mapFragment.setRetainInstance(true);
			fragmentTransaction.replace(R.id.mapLayout, mapFragment, "map");
			fragmentTransaction.commit();
		}
		
		
		// チャットを表示
		final EditText editText = (EditText)findViewById(R.id.editText1);
		Button button1 = (Button)findViewById(R.id.button1);
		
		button1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				displayChat(editText.getText().toString());
			}
		});
		
		// ミッションを表示
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
		
		// ロケーション送信
		locationLogic = new LocationLogic(this);
		locationLogic.setLocationListener(new LocationListener(){

			@Override
			public void onLocationChanged(Location location) {
				Log.v("game", "user="+userEntity.getUserId()+", "+userEntity.getToken());
				API.postLocation(userEntity, location, new JsonHttpResponseHandler(){ 
					@Override
					public void onSuccess(JSONObject json) {
						Log.v("game", "json="+json.toString());
					}	
				});
			}
			
		});
	}
	
	@Override
	public void onStart() {
		super.onStart();
		// 位置取り開始
		locationLogic.start();
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

		// 位置取り終了
		locationLogic.stop();
	}
	
	private void displayChat(String text) {
		View v = getLayoutInflater().inflate(R.layout.layout_chat, null);
		TextView textView = (TextView)v.findViewById(R.id.textView1);
		textView.setText(text);
		
		LinearLayout chatList = (LinearLayout)findViewById(R.id.chatList);
		chatList.addView(v);
	}
}
