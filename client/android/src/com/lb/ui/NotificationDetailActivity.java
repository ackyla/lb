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
		
		if (mId > 0) {
			API.readNotification(Session.getUser(), mId, new JsonHttpResponseHandler() {
				
				@Override
				public void onStart() {
					if (mProgressDialog == null) mProgressDialog = Utils.createProgressDialog(NotificationDetailActivity.this);
					mProgressDialog.show();
				}
				
				@Override
				public void onSuccess(JSONObject json) {
					Toast.makeText(NotificationDetailActivity.this, "既読", Toast.LENGTH_LONG).show();
					
					NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);	
					manager.cancel(mId);
				}

				@Override
				public void onFailure(Throwable throwable) {
					Log.i("game","getUserTerritoryListOnFailure="+ throwable);
				}
				
				@Override
				public void onFinish() {
					mProgressDialog.dismiss();
				}
			});
		}
		
	}
	
	  @Override
	  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);

	    Log.i("game", "requestCode=" + requestCode);
	  }

}
