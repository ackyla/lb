package com.lb.ui;

import org.json.JSONObject;

import com.lb.R;
import com.lb.api.API;
import com.lb.dao.UserEntity;
import com.lb.logic.UserLogic;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignupActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		
		final UserLogic userLogic = new UserLogic(this);
		final ProgressDialog progress = new ProgressDialog(this);
		final EditText editText = (EditText)findViewById(R.id.editText1);
		Button button = (Button)findViewById(R.id.buttonRegister);		
		button.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				API.register(editText.getText().toString(), new JsonHttpResponseHandler() {					
					
					@Override
					public void onStart() {
						progress.setMessage("通信中…");
						progress.show();
					}
					
					@Override
					public void onSuccess(JSONObject json) {
						UserEntity userEntity = new UserEntity(json);
						userLogic.register(userEntity);
						progress.dismiss();
						
						Intent intent = new Intent();
						intent.setClass(SignupActivity.this, MainActivity.class);
						startActivity(intent);
						finish();
					}

					@Override
					public void onFailure(Throwable e) {
						progress.dismiss();
						Toast.makeText(getApplicationContext(), "登録に失敗しました!", Toast.LENGTH_LONG).show();
					}
				});
			}
		
		});
	}
}
