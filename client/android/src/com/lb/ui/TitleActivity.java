package com.lb.ui;

import com.lb.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class TitleActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_title);
		
		Intent intent = new Intent();
		intent.setClass(this, SignupActivity.class);
		startActivity(intent);
		finish();
	}
}
