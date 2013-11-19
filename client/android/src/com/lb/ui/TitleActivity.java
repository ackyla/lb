package com.lb.ui;

import com.lb.AuthDao;
import com.lb.DaoSession;
import com.lb.R;
import com.lb.logic.AuthLogic;
import com.lb.model.Session;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class TitleActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_title);
		
		Intent intent = new Intent();
		
		// 端末にユーザ情報あるかをチェック
		Session session = (Session) getApplication();
		DaoSession daoSession = session.getDaoSession();
		AuthDao authDao = daoSession.getAuthDao();
		if(authDao.count() > 0) {
			intent.setClass(this, GameActivity.class);
		} else {
			intent.setClass(this, SignupActivity.class);
		}
		
		startActivity(intent);
		finish();
	}
}
