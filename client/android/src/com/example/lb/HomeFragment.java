package com.example.lb;

import dao.user.UserEntity;
import logic.user.UserLogic;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HomeFragment extends Fragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View v = inflater.inflate(R.layout.fragment_home, container, false);
    	
    	TextView tv = (TextView)v.findViewById(R.id.textView1);
    	UserLogic userLogic = new UserLogic(getActivity());
    	UserEntity userEntity = userLogic.getUser();
    	
    	if(userEntity != null){
    		tv.setText(userEntity.getName());
    	}
    	
    	return v;
	}
}
