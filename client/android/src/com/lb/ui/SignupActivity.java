package com.lb.ui;

import java.io.IOException;

import net.vvakame.util.jsonpullparser.JsonFormatException;

import org.json.JSONObject;

import com.lb.Auth;
import com.lb.AuthDao;
import com.lb.DaoSession;
import com.lb.R;
import com.lb.api.API;
import com.lb.model.Session;
import com.lb.model.User;
import com.lb.model.UserGen;
import com.lb.model.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignupActivity extends Activity implements OnClickListener {

	private ProgressDialog mProgressDialog;
	private EditText userNameInput;
	private AuthDao authDao;
	private Auth auth;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		
		userNameInput = (EditText)findViewById(R.id.editText1);
		Button button = (Button)findViewById(R.id.buttonRegister);
		button.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {		
		API.register(userNameInput.getText().toString(), new JsonHttpResponseHandler() {
			
			@Override
			public void onStart() {
				mProgressDialog = Utils.createProgressDialog(SignupActivity.this);
			}
			
			@Override
			public void onSuccess(JSONObject json) {
				mProgressDialog.dismiss();
				try {
					User user = UserGen.get(json.toString());
					String url = PreferenceManager.getDefaultSharedPreferences(SignupActivity.this).getString(PreferenceScreenActivity.PREF_KEY_DEBUG_MODE_URL, getResources().getString(R.string.server_url));
					Session session = (Session) getApplication();
					DaoSession daoSession = session.getDaoSession();
					authDao = daoSession.getAuthDao();
					auth = new Auth(null, user.getId(), user.getToken(), user.getName(), url);
					authDao.insert(auth);
					
					Session.setUser(user);
					
					Intent intent = new Intent();
					intent.setClass(SignupActivity.this, GameActivity.class);
					startActivity(intent);
					//overridePendingTransition(0,0);
					finish();
				} catch (IOException e1) {
					
				} catch (JsonFormatException e1) {

				}
			}

			@Override
			public void onFailure(Throwable e) {
				mProgressDialog.dismiss();
				Toast.makeText(getApplicationContext(), "登録に失敗しました!", Toast.LENGTH_LONG).show();
			}
		});
	}
}
