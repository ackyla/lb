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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TitleActivity extends Activity {
	
	private ProgressDialog mProgressDialog; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_title);
		
		Session session = (Session) getApplication();
		DaoSession daoSession = session.getDaoSession();
		AuthDao authDao = daoSession.getAuthDao();
		
		// ユーザ登録してる時(データベースにユーザ情報がある時)はゲーム画面に移動，ないときだけ登録画面へのボタン表示
		if(authDao.count() > 0) {
			Auth auth = authDao.loadAll().get(0);
			startGame(auth);
		}else{
			Button bt = (Button)findViewById(R.id.bt_signup);
			bt.setVisibility(Button.VISIBLE);
			bt.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(TitleActivity.this, SignupActivity.class);
					startActivity(intent);
					finish();
				}
				
			});
		}		
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
