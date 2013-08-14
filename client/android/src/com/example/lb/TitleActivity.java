package com.example.lb;

import logic.user.UserLogic;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class TitleActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_title);
		
		// 端末にユーザ情報あるかをチェック
		UserLogic userLogic = new UserLogic(this);
		Intent intent = new Intent();
		if (!userLogic.checkRegister()) {
			// サインアップ画面へ
			intent.setClass(this, SignupActivity.class);
		} else {
			// ゲーム or ホーム
			// TODO ゲームかホームかの分岐
			intent.setClass(this, MainActivity.class);
		}
		startActivity(intent);
		finish();
	}
}
