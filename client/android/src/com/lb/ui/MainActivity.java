package com.lb.ui;

import org.json.JSONException;
import org.json.JSONObject;

import com.lb.R;
import com.lb.api.API;
import com.lb.dao.AuthEntity;
import com.lb.dao.RoomEntity;
import com.lb.logic.AuthLogic;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.content.Intent;
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

public class MainActivity extends FragmentActivity {

	private static final int FRAGMENT_HOME = 100;
	private static final int FRAGMENT_ROOM = 101;
	private static final int FRAGMENT_CONFIG = 102;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v("life", "main create");
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			replaceFragment(FRAGMENT_HOME);
		}
		

		Button buttonHome = (Button) findViewById(R.id.buttonHome);
		Button buttonRoom = (Button) findViewById(R.id.buttonRoom);
		Button buttonConfig = (Button) findViewById(R.id.buttonConfig);

		buttonHome.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				replaceFragment(FRAGMENT_HOME);
			}
		});

		buttonRoom.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				replaceFragment(FRAGMENT_ROOM);
			}
		});

		buttonConfig.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				replaceFragment(FRAGMENT_CONFIG);
			}
		});
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.v("life", "main start");

		AuthLogic authLogic = new AuthLogic(this);
		AuthEntity authEntity = authLogic.getAuth();
		API.getUserInfo(authEntity.getUserId(), new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject object) {
				try {
					JSONObject roomObject = object.getJSONObject("room");
					RoomEntity roomEntity = new RoomEntity(roomObject);
					if (roomEntity.getId() != 0 && roomEntity.getActive()) {
						Intent intent = new Intent();
						intent.setClass(MainActivity.this, GameActivity.class);
						startActivity(intent);
						finish();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	@Override
	public void onResume() {
		super.onResume();
		Log.v("life", "main resume");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.v("life", "main destroy");
	}

	/**
	 * フラグメントを切り替える
	 * 
	 * @param fragmentName
	 */
	private final void replaceFragment(int fragmentType) {
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = manager.beginTransaction();
		Fragment fragment;
		String tag;
		switch (fragmentType) {
		case FRAGMENT_HOME:
		default:
			tag = "home";
			fragment = manager.findFragmentByTag(tag);
			if (fragment == null) {
				fragment = new HomeFragment();
				fragmentTransaction.replace(R.id.mainContent, fragment, tag);
				fragmentTransaction.commit();
			}
			break;
		case FRAGMENT_ROOM:
			tag = "room";
			fragment = manager.findFragmentByTag(tag);
			if (fragment == null) {
				fragment = new RoomFragment();
				fragmentTransaction.replace(R.id.mainContent, fragment, tag);
				fragmentTransaction.commit();
			}
			break;
		case FRAGMENT_CONFIG:
			tag = "config";
			fragment = manager.findFragmentByTag(tag);
			if (fragment == null) {
				fragment = new ConfigFragment();
				fragmentTransaction.replace(R.id.mainContent, fragment, tag);
				fragmentTransaction.commit();
			}
			break;
		}
	}
}