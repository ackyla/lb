package com.lb.ui;

import java.util.TimerTask;

import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GlobalPosition;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lb.R;
import com.lb.api.API;
import com.lb.dao.LocationEntity;
import com.lb.dao.RoomEntity;
import com.lb.dao.UserEntity;
import com.lb.logic.HitMarker;
import com.lb.logic.LocationLogic;
import com.lb.logic.LocationMarker;
import com.lb.logic.MapLogic;
import com.lb.logic.TimerLogic;
import com.lb.logic.UserLogic;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.content.Intent;
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
import android.widget.TextView;

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
	Integer leftTerritory;

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

		/*GeodeticCalculator geoCalc = new GeodeticCalculator();
		Ellipsoid ellipsoid = Ellipsoid.WGS84;
		GlobalPosition point = new GlobalPosition(lat,lng,0.0);
		GlobalPosition territoryPos = new GlobalPosition(lat,lng,0.0);
		double distance = geoCalc.calculateGeodeticCurve(ellipsoid, territoryPos, point).getEllipsoidalDistance();*/
		
		// ユーザ情報と部屋の情報を取得
		UserLogic userLogic = new UserLogic(this);
		userEntity = userLogic.getUser();
		API.getUserInfo(userEntity.getUserId(), new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject object) {
				try {
					// ユーザ情報をサーバから取得したもので上書き
					userEntity = new UserEntity(object);
					JSONObject roomObject = object.getJSONObject("room");
					roomEntity = new RoomEntity(roomObject);
				} catch (JSONException e) {
					Log.v("game", "getUserInfoError=" + e);
				}
			}

			@Override
			public void onFailure(Throwable throwable) {
				Log.v("game", "getUserInfoOnFailure=" + throwable);
			}
		});

		// マップを表示
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = manager.beginTransaction();
		SupportMapFragment mapFragment = (SupportMapFragment) manager
				.findFragmentByTag("map");
		if (mapFragment == null) {
			mapFragment = SupportMapFragment.newInstance();
			mapFragment.setRetainInstance(true);
			fragmentTransaction.replace(R.id.mapLayout, mapFragment, "map");
			fragmentTransaction.commit();
		}
		mapLogic = new MapLogic(this, mapFragment);

		// チャットを表示
		final EditText editText = (EditText) findViewById(R.id.chatInput);
		Button button1 = (Button) findViewById(R.id.commentButton);

		button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				displayChat(editText.getText().toString());
			}
		});

		// ミッションを表示
		final LinearLayout missionView = (LinearLayout) findViewById(R.id.missionView);
		Button button2 = (Button) findViewById(R.id.missionViewButton);

		button2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				missionView.setVisibility(View.VISIBLE);
			}

		});

		Button button3 = (Button) findViewById(R.id.missionCloseButton);

		button3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				missionView.setVisibility(View.GONE);
			}

		});

		// ロケーション送信
		locationLogic = new LocationLogic(this);
		locationLogic.setLocationListener(new LocationListener() {
			@Override
			public void onLocationChanged(Location location) {

				if (userEntity.getRoomId() > 0) {
					API.postLocation(userEntity, location,
							new JsonHttpResponseHandler() {
								@Override
								public void onSuccess(JSONObject json) {
									Log.v("game", "location=" + json.toString());
								}

								@Override
								public void onFailure(Throwable throwable) {
									Log.v("game", "postLocationOnFailure="
											+ throwable);
								}
							});
				}
			}

		});

		// 一定間隔で位置情報を取得
		timerLogic = new TimerLogic(this);
		getLocationTask = timerLogic.create(new Runnable() {
			SparseArray<LocationMarker> locationMarkers = new SparseArray<LocationMarker>();

			@Override
			public void run() {
				if (userEntity.getRoomId() > 0) {
					API.getRoomLocations(userEntity.getRoomId(),
							new JsonHttpResponseHandler() {
								@Override
								public void onSuccess(JSONObject ret) {
									try {
										JSONArray users = ret
												.getJSONArray("members");
										SparseArray<JSONObject> userMap = new SparseArray<JSONObject>();
										for (int i = 0; i < users.length(); i++) {
											JSONObject userObj = users
													.getJSONObject(i);

											userMap.put(userObj.getInt("id"),
													userObj);
											LocationMarker locationMarker = locationMarkers
													.get(userObj.getInt("id"));
											if (locationMarker != null) {
												locationMarker.remove();
											}
										}

										JSONArray locations = ret
												.getJSONArray("locations");
										for (int i = 0; i < locations.length(); i++) {
											LocationEntity locationEntity = new LocationEntity(
													locations.getJSONObject(i));
											double lat = locationEntity
													.getLatitude();
											double lng = locationEntity
													.getLongitude();

											UserEntity roomUserEntity = new UserEntity(
													userMap.get(locationEntity
															.getUserId()));
											LocationMarker locationMarker = locationMarkers
													.get(roomUserEntity
															.getUserId());
											if (locationMarker == null) {
												locationMarker = new LocationMarker();
												locationMarkers.put(
														roomUserEntity
																.getUserId(),
														locationMarker);
											}
											Marker marker = mapLogic.addLocationMarker(
													lat, lng, roomUserEntity
															.getName(),
													locationEntity
															.getCreatedAt());
											locationMarker.addMarker(marker);
										}

										for (int i = 0; i < users.length(); i++) {
											JSONObject userObj = users
													.getJSONObject(i);
											LocationMarker locationMarker = locationMarkers
													.get(userObj.getInt("id"));
											if (locationMarker != null) {
												locationMarker.addLine(mapLogic
														.drawLine(locationMarker
																.getMarkers()));
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
		timerLogic.start(getLocationTask, 5000);

		// 残り時間をカウントダウン
		final TextView leftTimeView = (TextView) findViewById(R.id.leftTimeView);
		countLeftTimeTask = timerLogic.create(new Runnable() {
			@Override
			public void run() {
				if (leftTime == null && userEntity.getRoomId() > 0) {
					// 残り時間が取れてない時はAPIで取りに行く
					API.getTimeLeft(userEntity.getRoomId(),
							new JsonHttpResponseHandler() {
								@Override
								public void onSuccess(JSONObject json) {
									try {
										leftTime = json.getInt("second");
									} catch (JSONException e) {
									}
								}

								@Override
								public void onFailure(Throwable throwable) {
									Log.v("game", "getTimeLeftOnFailure="
											+ throwable);
								}
							});
				} else if (leftTime != null){
					if (leftTime > 0) {
						int HH = leftTime / 3600;
						int mm = leftTime % 3600 / 60;
						int ss = leftTime % 60;
						leftTimeView
								.setText("終了まで " + HH + ":" + mm + ":" + ss);
						leftTime--;
					} else {
						onGameEnd();
					}
				}
			}
		});
		timerLogic.start(countLeftTimeTask, 1000);

		// 一定間隔で残り時間を取りに行く
		getLeftTimeTask = timerLogic.create(new Runnable() {

			@Override
			public void run() {
				if (userEntity.getRoomId() > 0) {
					API.getTimeLeft(userEntity.getRoomId(),
							new JsonHttpResponseHandler() {
								@Override
								public void onSuccess(JSONObject json) {
									try {
										leftTime = json.getInt("second");
									} catch (JSONException e) {
										e.printStackTrace();
									}
								}

								@Override
								public void onFailure(Throwable throwable) {
									Log.v("game", "getTimeLeftOnFailure="
											+ throwable);
								}
							});
				}
			}

		});
		timerLogic.start(getLeftTimeTask, 30000);

		// 残り設置可能テリトリー数表示
		leftTerritory = 5; // TODO 残りテリトリ数取得
		TextView leftTerritoryView = (TextView) findViewById(R.id.leftTerritoryView);
		leftTerritoryView.setText("残り " + leftTerritory);
	}

	@Override
	public void onStart() {
		super.onStart();
		// マップ初期化
		mapLogic.init();
		// 位置取り開始
		locationLogic.start();

		// テリトリー作成
		mapLogic.setOnLongClickListener(new OnMapLongClickListener() {
			@Override
			public void onMapLongClick(LatLng latlng) {
				if (leftTerritory > 0) {
					mapLogic.addTerritory(latlng, 10000);
					leftTerritory--;
					TextView leftTerritoryView = (TextView) findViewById(R.id.leftTerritoryView);
					leftTerritoryView.setText("残り " + leftTerritory);
				}

				// TODO API
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

		// ロケーション殺す
		locationLogic.stop();

		// タイマー殺す
		timerLogic.cancel(getLocationTask);
		timerLogic.cancel(countLeftTimeTask);
		timerLogic.cancel(getLeftTimeTask);
	}

	// ゲーム終了時の処理
	private void onGameEnd() {
		TextView leftTimeView = (TextView) findViewById(R.id.leftTimeView);
		final Button hitButton = (Button) findViewById(R.id.hitButton);

		leftTimeView.setText("終了");

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
						if (userEntity.getRoomId() > 0) {
							API.postHitLocation(userEntity, 0, latlng.latitude,
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

	private void displayChat(String text) {
		View v = getLayoutInflater().inflate(R.layout.layout_chat, null);
		TextView textView = (TextView) v.findViewById(R.id.textView1);
		textView.setText(text);

		LinearLayout chatList = (LinearLayout) findViewById(R.id.chatList);
		chatList.addView(v);
	}
}
