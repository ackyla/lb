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
	
	private UserEntity userEntity;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View v = inflater.inflate(R.layout.fragment_home, container, false);
    	
    	TextView tv1 = (TextView)v.findViewById(R.id.textView1);
    	TextView tv2 = (TextView)v.findViewById(R.id.textView2);
    	
    	UserLogic userLogic = new UserLogic(getActivity());
    	userEntity = userLogic.getUser();
    	
    	if(userEntity != null){
    		tv1.setText(userEntity.getName());
    		tv2.setText("部屋ID:"+userEntity.getRoomId()+" に入室中．");
    	}
    	
    	return v;
	}
}
