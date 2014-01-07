package com.lb.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TitleActivity extends Activity {
	
	private ProgressDialog mProgressDialog; 
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (getResources().getBoolean(R.bool.debug_mode)) {
			getMenuInflater().inflate(R.menu.title, menu);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
		default:
			Intent intent = new Intent();
			intent.setClass(TitleActivity.this,
					PreferenceScreenActivity.class);
			startActivity(intent);
			break;
		}
		return true;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_title);
		
		Session session = (Session) getApplication();
		DaoSession daoSession = session.getDaoSession();
		final AuthDao authDao = daoSession.getAuthDao();
		
		Button bt2 = (Button)findViewById(R.id.bt_debug_user);
		Button bt3 = (Button)findViewById(R.id.bt_signup);
		
		if (getResources().getBoolean(R.bool.debug_mode)) {
			if (authDao.count() > 0) {
				List<Auth> authList = authDao.loadAll();
				List<String> users = new ArrayList<String>();
				for (int i = 0; i < authList.size(); i ++) {
					Auth auth = authList.get(i);
					users.add(auth.getUrl()+", "+auth.getName());
				}
				
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("使用するユーザを選択して下さい");
				builder.setSingleChoiceItems(users.toArray(new String[users.size()]), 0, new AlertDialog.OnClickListener() {
	
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Auth auth = authDao.loadAll().get(which);
						startGame(auth);
						dialog.dismiss();						
					}
					
				});
				final AlertDialog userDialog = builder.create();
				
				bt2.setVisibility(View.VISIBLE);
				bt2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						userDialog.show();
					}
					
				});
			}

			bt3.setVisibility(View.VISIBLE);
			bt3.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					startSignup();
				}
				
			});

		}else{
			// ユーザ登録してる時(データベースにユーザ情報がある時)はゲーム画面に移動，ないときだけ登録画面へのボタン表示
			if(authDao.count() > 0) {
				Auth auth = authDao.loadAll().get((int)authDao.count()-1);
				startGame(auth);
			}else{
				bt3.setVisibility(Button.VISIBLE);
				bt3.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						startSignup();
					}
				
				});
			}
		}
	}
	
	private void startSignup() {
		Intent intent = new Intent();
		intent.setClass(TitleActivity.this, SignupActivity.class);
		startActivity(intent);
		finish();
	}
	
	private void startGame(final Auth auth) {
		API.getUserInfo(auth.getUser_id(), new JsonHttpResponseHandler() {
			
			@Override
			public void onStart() {
				mProgressDialog = Utils.createProgressDialog(TitleActivity.this);
			}
			
			@Override
			public void onSuccess(JSONObject json) {
				try {
					User user = UserGen.get(json.toString());
					
					user.setId(auth.getUser_id());
					user.setToken(auth.getToken());
					Session.setUser(user);
					
					Intent intent = new Intent();
					intent.setClass(TitleActivity.this, GameActivity.class);
					startActivity(intent);
					overridePendingTransition(0,0);
					finish();
				} catch (IOException e1) {
					
				} catch (JsonFormatException e1) {

				}
			}

			@Override
			public void onFailure(Throwable e) {				
				Toast.makeText(getApplicationContext(), "プレイヤー情報を取得できませんでした", Toast.LENGTH_LONG).show();
			}
			
			@Override
			public void onFinish() {
				mProgressDialog.dismiss();
			}
		});
	}
}
