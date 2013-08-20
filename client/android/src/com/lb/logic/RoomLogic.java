package com.lb.logic;

import java.util.List;

import org.json.JSONObject;

import com.lb.api.API;
import com.lb.dao.RoomEntity;
import com.lb.dao.UserEntity;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.content.Context;
import android.util.Log;

public class RoomLogic extends LogicBase {

	public RoomLogic(Context context) {
		super(context);
	}

	public List<RoomEntity> get() {
		return null;
	}
	
	public int create(String title, int timeLimit) {
		UserLogic userLogic = new UserLogic(context);
		UserEntity userEntity = userLogic.getUser();
		API.createRoom(userEntity, title, timeLimit, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject json) {

				try	{
					Log.v("main", "roomId=" + json.getInt("id"));
				} catch(Exception e){
					
				}
			}
		});
		
		return 0;
	}
	
	public boolean delete(int roomId) {
		
		return true;
	}
	
	
}
