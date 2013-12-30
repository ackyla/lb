package com.lb.ui;

import com.lb.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class NotificationDetailActivity extends FragmentActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification_detail);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	  @Override
	  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    //Bundle bundle = data.getExtras();

	    Log.i("game", "requestCode=" + requestCode);
	  }

}
