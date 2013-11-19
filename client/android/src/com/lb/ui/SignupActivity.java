package com.lb.ui;

import org.json.JSONException;
import org.json.JSONObject;

import com.lb.Auth;
import com.lb.AuthDao;
import com.lb.DaoSession;
import com.lb.R;
import com.lb.api.API;
import com.lb.model.Session;
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

public class SignupActivity extends Activity implements OnClickListener {

	private ProgressDialog progress;
	private EditText userNameInput;
	private AuthDao authDao;
	private Auth auth;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		
		Session session = (Session) getApplication();
		DaoSession daoSession = session.getDaoSession();
		authDao = daoSession.getAuthDao();
		
		if(authDao.count() > 0) {
			auth = authDao.loadAll().get(0);
			startGame();
		}

		//Log.v("game", "query="+authDao.queryBuilder().where(Properties.User_id.eq(9)).build().list());
		
		progress = new ProgressDialog(this);
		userNameInput = (EditText)findViewById(R.id.editText1);
		Button button = (Button)findViewById(R.id.buttonRegister);		
		button.setOnClickListener(this);
	}
	
	private void startGame() {
		Intent intent = new Intent();
		intent.setClass(this, GameActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	public void onClick(View v) {
		API.register(userNameInput.getText().toString(), new JsonHttpResponseHandler() {					
			
			@Override
			public void onStart() {
				progress.setMessage("通信中…");
				progress.show();
			}
			
			@Override
			public void onSuccess(JSONObject json) {
				int userId;
				String token;
				try {
					userId = json.getInt("id");
					token = json.getString("token");
				} catch (JSONException e) {
					userId = 0;
					token = "";
				}
				
				auth = new Auth(null, userId, token);
				authDao.insert(auth);

				progress.dismiss();

				startGame();
			}

			@Override
			public void onFailure(Throwable e) {
				progress.dismiss();
				Toast.makeText(getApplicationContext(), "登録に失敗しました!", Toast.LENGTH_LONG).show();
			}
		});
	}
}
