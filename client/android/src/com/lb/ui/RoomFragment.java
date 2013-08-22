package com.lb.ui;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lb.R;
import com.lb.api.API;
import com.lb.dao.RoomEntity;
import com.lb.dao.UserEntity;
import com.lb.logic.UserLogic;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RoomFragment extends Fragment {
	
	private UserEntity userEntity;
	private UserLogic userLogic;
	
	public RoomFragment() {
		setRetainInstance(true);
	}
	
	private void enterRoom(int roomId) {
		Log.v("room", "user="+userEntity.getUserId()+", roomId="+roomId);
		API.enterRoom(userEntity, roomId, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONObject json) {
				RoomEntity roomEntity = new RoomEntity(json);
				
				// TODO いずれ殺す
				userLogic.enterRoom(userEntity, roomEntity.getId());
				
				FragmentManager manager = getFragmentManager();
				FragmentTransaction fragmentTransaction = manager.beginTransaction();
				Fragment fragment = manager.findFragmentByTag("home");
				if(fragment==null){
					fragment = new HomeFragment();
					fragmentTransaction.replace(R.id.mainContent, fragment, "home");
					fragmentTransaction.commit();
				}
			}
		});
	}
	
	private void addRoom(LinearLayout roomList, JSONObject json) {
		final RoomEntity roomEntity = new RoomEntity(json);
		View v = getActivity().getLayoutInflater().inflate(R.layout.layout_room, null);
		TextView roomTitle = (TextView)v.findViewById(R.id.title);
		roomTitle.setText(roomEntity.getId() + ": " + roomEntity.getTitle());
		Button button = (Button)v.findViewById(R.id.button);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				enterRoom(roomEntity.getId());
			}
			
		});
		roomList.addView(v);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v("life", "room create");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View v = inflater.inflate(R.layout.fragment_room, container, false);
    	Log.v("life", "room createView");
    	
    	
    	final LinearLayout roomListLayout = (LinearLayout)v.findViewById(R.id.roomListLayout);
    	final EditText titleInput = (EditText)v.findViewById(R.id.titlInput);
    	final EditText timeLimitInput = (EditText)v.findViewById(R.id.timeLimitInput);
    	
    	Button createButton = (Button)v.findViewById(R.id.createButton);
       	userLogic = new UserLogic(getActivity());
    	userEntity = userLogic.getUser();
    	
    	// 部屋一覧を取得
    	API.getRoomList(userEntity, new JsonHttpResponseHandler() {
    		
    		@Override
    		public void onSuccess(JSONArray jsonArray) {
    			for(int i = 0; i < jsonArray.length(); i++){
    				try {
						JSONObject json = jsonArray.getJSONObject(i);
						addRoom(roomListLayout, json);
					} catch (JSONException e) {
					}
    			}
    		}
    		
    		@Override
    		public void onFailure(Throwable e) {
    			
    		}
    	});
    	
    	// 新しく部屋を作成
    	createButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				String title = titleInput.getText().toString();
				// TODO 開発用
				int timeLimit = timeLimitInput.getText().toString().length() > 0 ? Integer.valueOf(timeLimitInput.getText().toString()) : 1;
				if(timeLimit < 1) timeLimit = 1;
				//
				API.createRoom(userEntity, title, timeLimit, new JsonHttpResponseHandler() {
					
					private ProgressDialog progress = new ProgressDialog(getActivity());
					
					@Override
					public void onStart() {
						Log.v("room", "start");
						progress.setMessage("通信中…");
						progress.show();
					}
					
					@Override
					public void onSuccess(JSONObject json) {
						Log.v("room", "success");
						RoomEntity roomEntity = new RoomEntity(json);
						addRoom(roomListLayout, json);
						enterRoom(roomEntity.getId());
						progress.dismiss();
					}
					
					public void onFailure(Throwable e) {
						progress.dismiss();
						Log.v("room", "fail");
					}
					
				});
			}
    		
    	});
    	
    	return v;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.v("life", "room attach");
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		Log.v("life", "room detach");
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.v("life", "room destroy");
	}
}
