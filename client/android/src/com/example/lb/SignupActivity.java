package com.example.lb;

import dao.user.UserEntity;
import logic.user.UserLogic;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SignupActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		
		final Context context = this;
		final UserLogic userLogic = new UserLogic(this);
		
		LinearLayout ll1 = (LinearLayout)findViewById(R.id.linearLayout1);
		LinearLayout ll2 = (LinearLayout)findViewById(R.id.linearLayout2);
		
		UserEntity userEntity = userLogic.getUser();
		
		if(userEntity == null){
			ll1.setVisibility(LinearLayout.VISIBLE);
			ll2.setVisibility(LinearLayout.GONE);
		}else{
			ll1.setVisibility(LinearLayout.GONE);
			ll2.setVisibility(LinearLayout.VISIBLE);
			TextView tv = (TextView)findViewById(R.id.textView1);
			tv.setText("name="+userEntity.getName()+", userId="+userEntity.getUserId()+", token="+userEntity.getToken());
		}
		
		Button button1 = (Button)findViewById(R.id.button1);
		button1.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				EditText editText = (EditText)findViewById(R.id.editText1);
				editText.getText();
				userLogic.register(editText.getText().toString());
				
				Intent intent = new Intent();
				intent.setClass(context, MainActivity.class);
				startActivity(intent);
			}
		
		});
	}
}
