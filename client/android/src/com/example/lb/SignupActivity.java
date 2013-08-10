package com.example.lb;

import logic.user.UserLogic;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SignupActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		
		final UserLogic userLogic = new UserLogic(this);
		
		Button button = (Button)findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				EditText editText = (EditText)findViewById(R.id.editText1);
				editText.getText();
				userLogic.register(editText.getText().toString());
			}
		
		});
	}
}
