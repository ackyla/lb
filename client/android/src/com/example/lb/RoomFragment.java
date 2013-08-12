package com.example.lb;

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
    	
    	final RoomLogic roomLogic = new RoomLogic(getActivity());
    	UserLogic userLogic = new UserLogic(getActivity());
    	userEntity = userLogic.getUser();
    	
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
						progress.dismiss();
						Toast.makeText(getActivity(), "新しい部屋を作成しました！", Toast.LENGTH_LONG).show();
					}
					
					public void onFailure(Throwable e) {
						progress.dismiss();
						Toast.makeText(getActivity(), "部屋の作成に失敗しました！", Toast.LENGTH_LONG).show();
					}
					
				});
			}
    		
    	});
    	
    	return v;
	}
}
