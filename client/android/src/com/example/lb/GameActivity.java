package com.example.lb;

import java.util.HashMap;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import logic.location.LocationLogic;
import logic.map.MapLogic;
import logic.map.MapLogic.HitMarkerController;
import logic.timer.TimerLogic;
import logic.user.UserLogic;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.JsonHttpResponseHandler;

import dao.room.RoomEntity;
import dao.user.UserEntity;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import api.API;

public class GameActivity extends FragmentActivity {

	LocationLogic locationLogic;
	TimerLogic timerLogic;
	MapLogic mapLogic;
	UserEntity userEntity;
	RoomEntity roomEntity;
	TimerTask getLocationTask;
	TimerTask countLeftTimeTask;
	TimerTask getLeftTimeTask;
	Integer leftTime;
	
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
					// TODO 殺す
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
		mapLogic = new MapLogic(this, mapFragment);
		
		// チャットを表示
		final EditText editText = (EditText)findViewById(R.id.chatInput);
		Button button1 = (Button)findViewById(R.id.commentButton);
		
		button1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				displayChat(editText.getText().toString());
			}
		});
		
		// ミッションを表示
		final LinearLayout missionView = (LinearLayout)findViewById(R.id.missionView);
		Button button2 = (Button)findViewById(R.id.missionViewButton);
		
		button2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				missionView.setVisibility(View.VISIBLE);
			}
			
		});
		
		Button button3 = (Button)findViewById(R.id.missionCloseButton);
		
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
				API.postLocation(userEntity, location, new JsonHttpResponseHandler(){ 
					@Override
					public void onSuccess(JSONObject json) {
						
					}	
				});
			}
			
		});
		
		// 一定間隔で位置情報を取得
		timerLogic = new TimerLogic(this);
		getLocationTask = timerLogic.create(new Runnable() {
			@Override
			public void run() {
				API.getRoomLocations(userEntity.getRoomId(), new JsonHttpResponseHandler(){
					@Override
					public void onSuccess(JSONObject ret) {
						try {
							JSONArray users = ret.getJSONArray("members");
							SparseArray<JSONObject> userMap = new SparseArray<JSONObject>();
							for (int i = 0; i < users.length(); i++) {
								JSONObject userObj = users.getJSONObject(i);
								userMap.put(userObj.getInt("id"), userObj);								
							
							}
							
							JSONArray locations = ret.getJSONArray("locations");
							SparseArray<JSONObject> prevLocations = new SparseArray<JSONObject>();
							for(int i = 0; i < locations.length(); i++){
								JSONObject json = locations.getJSONObject(i);
								double lat = json.getDouble("latitude");
								double lng = json.getDouble("longitude");
								UserEntity roomUserEntity = new UserEntity(userMap.get(json.getInt("user_id")));
								mapLogic.addMarker(lat, lng, roomUserEntity.getName());
								if(prevLocations.get(roomUserEntity.getUserId()) != null){
									JSONObject preJson = prevLocations.get(roomUserEntity.getUserId());
									double preLat = preJson.getDouble("latitude");
									double preLng = preJson.getDouble("longitude");
									mapLogic.drawLine(preLat, preLng, lat, lng);
								}
								prevLocations.put(roomUserEntity.getUserId(), json);
							}
						} catch (JSONException e) {
							Log.e("game", e.toString());
						}
					}
				});
			}
		});
		timerLogic.start(getLocationTask, 60000);
		
		// 残り時間をカウントダウン
		final TextView leftTimeView = (TextView)findViewById(R.id.leftTimeView);
		countLeftTimeTask = timerLogic.create(new Runnable() {
			@Override
			public void run() {
				if(leftTime == null){
					// 残り時間が取れてない時はAPIで取りに行く
					API.getTimeLeft(userEntity.getRoomId(), new JsonHttpResponseHandler(){
						@Override
						public void onSuccess(JSONObject json) {
							try {
								leftTime = json.getInt("second");
							} catch (JSONException e) {
							}
						}
					});
				} else if (leftTime > 0){
					int HH = leftTime / 3600;
					int mm = leftTime % 3600 / 60;
					int ss = leftTime % 60;
					leftTimeView.setText("終了まで "+HH+":"+mm+":"+ss);
					leftTime --;
				} else {
					onGameEnd();
				}
			}
		});
		timerLogic.start(countLeftTimeTask, 1000);
		
		// 一定間隔で残り時間を取りに行く		
		getLeftTimeTask = timerLogic.create(new Runnable() {
			
			@Override
			public void run() {
				API.getTimeLeft(userEntity.getRoomId(), new JsonHttpResponseHandler(){
					@Override
					public void onSuccess(JSONObject json) {
						try {
							leftTime = json.getInt("second");
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
			}
			
		});
		timerLogic.start(getLeftTimeTask, 30000);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		// マップ初期化
		mapLogic.init();
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

		// ロケーション殺す
		locationLogic.stop();
		
		// タイマー殺す
		timerLogic.cancel(getLocationTask);
		timerLogic.cancel(countLeftTimeTask);
		timerLogic.cancel(getLeftTimeTask);
	}
	
	// ゲーム終了時の処理
	private void onGameEnd() {
		TextView leftTimeView = (TextView)findViewById(R.id.leftTimeView);
		SeekBar hitAreaSlider = (SeekBar)findViewById(R.id.hitAreaSlider);
		Button hitButton = (Button)findViewById(R.id.hitButton);
		//final HitMarkerController hitMarkerController = null;
		
		leftTimeView.setText("終了");
		
		mapLogic.setOnClickListener(new OnMapClickListener(){						
			@Override
			public void onMapClick(LatLng latlng) {
				//if(hitMarkerController != null) hitMarkerController.remove();
				//hitMarkerController = mapLogic.addHitMarker(latlng, 10000);
			}
		});
		
		// ロケーション殺す
		locationLogic.stop();
		
		// タイマー殺す
		timerLogic.cancel(getLocationTask);
		timerLogic.cancel(countLeftTimeTask);
		timerLogic.cancel(getLeftTimeTask);
	}
	
	private void displayChat(String text) {
		View v = getLayoutInflater().inflate(R.layout.layout_chat, null);
		TextView textView = (TextView)v.findViewById(R.id.textView1);
		textView.setText(text);
		
		LinearLayout chatList = (LinearLayout)findViewById(R.id.chatList);
		chatList.addView(v);
	}
}
