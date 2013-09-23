package com.lb.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Player {
	private String name;
	private int userId;
	private String token;
	private int roomId;
	
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
			setToken(json.getString("token"));
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
	
	public String getToken() {
		return this.token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	public int getRoomId() {
		return this.roomId;
	}
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}
}
