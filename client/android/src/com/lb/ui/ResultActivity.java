package com.lb.ui;

import com.lb.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ResultActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);

		Button goToHomeButton = (Button)findViewById(R.id.goToHomeButton);
		goToHomeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ResultActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
			
		});
	}
}
