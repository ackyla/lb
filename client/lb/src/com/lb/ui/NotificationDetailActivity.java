package com.lb.ui;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;
import com.lb.R;
import com.lb.api.API;
import com.lb.model.Session;
import com.lb.model.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

public class NotificationDetailActivity extends FragmentActivity {

	private ProgressDialog mProgressDialog;
	private Integer mId;
	private String mType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification_detail);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Intent intent = getIntent();
		mId = intent.getIntExtra("notification_id", 0);
		mType = intent.getStringExtra("notification_type");

	}
	
	  @Override
	  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);

	    Log.i("game", "requestCode=" + requestCode);
	  }

}
