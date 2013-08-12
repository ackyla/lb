package com.example.lb;

import logic.user.UserLogic;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
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
		setContentView(R.layout.activity_main);
		
		replaceFragment(FRAGMENT_HOME);
		
		Button buttonHome = (Button)findViewById(R.id.buttonHome);
		Button buttonRoom = (Button)findViewById(R.id.buttonRoom);
		Button buttonConfig = (Button)findViewById(R.id.buttonConfig);
		
		buttonHome.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				replaceFragment(FRAGMENT_HOME);
			}
		});
		
		buttonRoom.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				replaceFragment(FRAGMENT_ROOM);
			}
		});
		
		buttonConfig.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				replaceFragment(FRAGMENT_CONFIG);
			}
		});
	}

	@Override
	public void onStart() {
		super.onStart();
		UserLogic userLogic = new UserLogic(this);
		if (!userLogic.checkRegister()) {
			Intent intent = new Intent();
			intent.setClass(this, SignupActivity.class);
			startActivity(intent);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	/**
	 * フラグメントを切り替える
	 * @param fragmentName
	 */
	private final void replaceFragment(int fragmentType) {
		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();
		Fragment fragment;
		switch(fragmentType){
			case FRAGMENT_HOME:
			default:
				fragment = new HomeFragment();
				break;
			case FRAGMENT_ROOM:
				fragment = new RoomFragment();
				break;
			case FRAGMENT_CONFIG:
				fragment = new ConfigFragment();
				break;
		}
		fragmentTransaction.replace(R.id.mainContent, fragment);
		fragmentTransaction.commit();
	}
}