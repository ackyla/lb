package com.example.lb;

import org.json.JSONArray;
import org.json.JSONException;
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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
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
    	final LinearLayout userList = (LinearLayout)v.findViewById(R.id.userList);
    	
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
    				
    		    	API.getRoomUsers(roomEntity.getId(), new JsonHttpResponseHandler() {
    		    		
    		    		@Override
    		    		public void onSuccess(JSONArray jsonArray) {
    		    			for(int i = 0; i < jsonArray.length(); i++){
    		    				try {
    								JSONObject json = jsonArray.getJSONObject(i);
    								addUser(userList, json);
    							} catch (JSONException e) {
    							}
    		    			}
    		    		}
    		    		
    		    		@Override
    		    		public void onFailure(Throwable e) {
    		    			Toast.makeText(getActivity(), "ユーザの取得に失敗しました！", Toast.LENGTH_SHORT).show();
    		    		}
    		    	});
    				
    			}
    		});
    		
    	}
    	
    	return v;
	}
	
	private void addUser(LinearLayout userList, JSONObject json) {
		final UserEntity userEntity = new UserEntity(json);
		View v = getActivity().getLayoutInflater().inflate(R.layout.layout_user, null);
		TextView textView = (TextView)v.findViewById(R.id.textView1);
		textView.setText(userEntity.getUserId() + ": " + userEntity.getName());
		userList.addView(v);
	}
}
