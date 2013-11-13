package com.lb.ui;

import com.lb.R;
import com.lb.logic.AuthLogic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class TitleActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_title);
		
		// 端末にユーザ情報あるかをチェック
		AuthLogic authLogic = new AuthLogic(this);
		Intent intent = new Intent();
		if (!authLogic.checkRegister()) {
			// サインアップ画面へ
			intent.setClass(this, SignupActivity.class);
		} else {
			// ゲーム画面へ
			intent.setClass(this, GameActivity.class);
		}
		startActivity(intent);
		finish();
	}
}
