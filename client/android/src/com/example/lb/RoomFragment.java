package com.example.lb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;

import dao.room.RoomEntity;
import dao.user.UserEntity;

import logic.room.RoomLogic;
import logic.user.UserLogic;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import api.API;

public class RoomFragment extends Fragment {
	
	private UserEntity userEntity;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View v = inflater.inflate(R.layout.fragment_room, container, false);
    	
       	UserLogic userLogic = new UserLogic(getActivity());
    	userEntity = userLogic.getUser();
    	final LinearLayout linearLayout = (LinearLayout)v.findViewById(R.id.roomList);
    	
    	API.getRoomList(userEntity, new JsonHttpResponseHandler() {
    		
    		@Override
    		public void onSuccess(JSONArray jsonArray) {
    			for(int i = 0; i < jsonArray.length(); i++){
    				try {
						JSONObject json = jsonArray.getJSONObject(i);
						RoomEntity roomEntity = new RoomEntity(json);
						TextView textView = new TextView(getActivity());
						textView.setText(roomEntity.getId()+": "+roomEntity.getTitle());
						linearLayout.addView(textView);
					} catch (JSONException e) {
					}
    			}
    		}
    		
    		@Override
    		public void onFailure(Throwable e) {
    			Toast.makeText(getActivity(), "部屋の取得に失敗しました！", Toast.LENGTH_SHORT).show();
    		}
    	});
    	
    	final RoomLogic roomLogic = new RoomLogic(getActivity());
    	final EditText editText = (EditText)v.findViewById(R.id.editText1);
    	Button button = (Button)v.findViewById(R.id.buttonCreate);
    	
    	button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				String title = editText.getText().toString();
				API.createRoom(userEntity, title, new JsonHttpResponseHandler() {
					
					private ProgressDialog progress = new ProgressDialog(getActivity());
					
					@Override
					public void onStart() {
						progress.setMessage("通信中…");
						progress.show();
					}
					
					@Override
					public void onSuccess(JSONObject json) {
						RoomEntity roomEntity = new RoomEntity(json);
						TextView textView = new TextView(getActivity());
						textView.setText(roomEntity.getId()+": "+roomEntity.getTitle());
						linearLayout.addView(textView);
						progress.dismiss();
						Toast.makeText(getActivity(), "新しい部屋を作成しました！", Toast.LENGTH_SHORT).show();
					}
					
					public void onFailure(Throwable e) {
						progress.dismiss();
						Toast.makeText(getActivity(), "部屋の作成に失敗しました！", Toast.LENGTH_SHORT).show();
					}
					
				});
			}
    		
    	});
    	
    	return v;
	}
}
