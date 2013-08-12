package com.example.lb;

import org.json.JSONObject;


import com.loopj.android.http.JsonHttpResponseHandler;

import dao.room.RoomEntity;
import dao.user.UserEntity;
import logic.user.UserLogic;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import api.API;

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
    	final TextView tv2 = (TextView)v.findViewById(R.id.textView2);
    	
    	UserLogic userLogic = new UserLogic(getActivity());
    	userEntity = userLogic.getUser();
    	
    	if(userEntity != null){
    		tv1.setText(userEntity.getName());
    		API.getRoomInfo(userEntity.getRoomId(), new JsonHttpResponseHandler(){
    			@Override
    			public void onSuccess(JSONObject object) {
    				Log.v("home", ""+object.toString());
    				RoomEntity roomEntity = new RoomEntity(object);
    				tv2.setText("入室中の部屋: "+roomEntity.getTitle());
    			}
    		});
    		
    	}
    	
    	return v;
	}
}
