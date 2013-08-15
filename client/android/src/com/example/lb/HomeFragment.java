package com.example.lb;

import logic.user.UserLogic;

import org.json.JSONException;
import org.json.JSONObject;
import com.loopj.android.http.JsonHttpResponseHandler;

import dao.room.RoomEntity;
import dao.user.UserEntity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import api.API;

public class HomeFragment extends Fragment {
	
	private UserEntity userEntity;
	private RoomEntity roomEntity;
	
    public HomeFragment(){  
        setRetainInstance(true);  
    }  
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v("life", "home create");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View v = inflater.inflate(R.layout.fragment_home, container, false);
    	Log.v("life", "home createView");

    	final LinearLayout userInfoLayout = (LinearLayout)v.findViewById(R.id.userInfoLayout);
    	final LinearLayout roomInfoLayout = (LinearLayout)v.findViewById(R.id.roomInfoLayout);
    	
    	UserLogic userLogic = new UserLogic(getActivity());
    	userEntity = userLogic.getUser();
    	
    	// ユーザ情報と入室中の部屋を表示
    	API.getUserInfo(userEntity.getUserId(), new JsonHttpResponseHandler(){
    		@Override
    		public void onSuccess(JSONObject object) {
    		   	View userInfo = getActivity().getLayoutInflater().inflate(R.layout.layout_user, null);
    	    	TextView userName = (TextView)userInfo.findViewById(R.id.name);
    	    	userName.setText(userEntity.getName());
    	    	userInfoLayout.addView(userInfo);
    	    	
    	    	try {
    	    		JSONObject roomObject = object.getJSONObject("room");
    	    		roomEntity = new RoomEntity(roomObject);
        			View roomInfo = getActivity().getLayoutInflater().inflate(R.layout.layout_room, null);
        			TextView roomTitle = (TextView)roomInfo.findViewById(R.id.title);
        			roomTitle.setText(roomEntity.getTitle());
        			Button exitButton = (Button)roomInfo.findViewById(R.id.button);
        			exitButton.setText("殺す");
        			// TODO 殺すボタン
        			
        			// 開始ボタン
        			Button startButton = (Button)roomInfo.findViewById(R.id.startButton);
        			// ルームオーナーだったら表示する
        			if(userEntity.getRoomId() == roomEntity.getId()){
        				startButton.setVisibility(View.VISIBLE);
        				startButton.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								API.startGame(userEntity, new JsonHttpResponseHandler(){
									@Override
									public void onSuccess(JSONObject json){
										RoomEntity roomEntity = new RoomEntity(json);
										if(!roomEntity.getActive()) return;
										Intent intent = new Intent();
										intent.setClass(getActivity(), GameActivity.class);
										startActivity(intent);
										getActivity().finish();
									}
								});
							}
						});
        			}else{
        				startButton.setVisibility(View.GONE);
        			}
        			
        			roomInfoLayout.addView(roomInfo);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}    	    	
    		}
    	});
		
		return v;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.v("life", "home attach");
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.v("life", "home activityCreated");
	}
	
	@Override
	public void onStart() {
		super.onStart();
		Log.v("life", "home Start");
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		Log.v("life", "home detach");
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.v("life", "home destroy");
	}
}
