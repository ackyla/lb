package com.lb.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.util.SparseArray;

public class Player {
	private String name;
	private int userId;
	private int roomId;
	
	private SparseArray<PlayerLocation> locationList;
	
	public Player() {
	}
	public Player(JSONObject json) {
		try	{			
			setName(json.getString("name"));
		} catch(Exception e){
			
		}
		try {
			setUserId(json.getInt("id"));
		} catch (JSONException e) {

		}
		try {
			setRoomId(json.getInt("room_id"));
		} catch (JSONException e) {

		}
	}
	
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public int getUserId() {
		return this.userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public int getRoomId() {
		return this.roomId;
	}
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}
	
	public void addLocation(PlayerLocation location) {
		
		// 初期化されてない時は初期化
		if(locationList == null){
			locationList = new SparseArray<PlayerLocation>();
		}
		
		// 新しい位置を登録
		if(locationList.get(location.getId()) == null) {
			locationList.append(location.getId(), location);
		}		
	}
}
