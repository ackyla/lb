package com.lb.dao;

import org.json.JSONException;
import org.json.JSONObject;

public class OldAuthEntity {
	private int userId;
	private String token;
	
	public OldAuthEntity() {
	}
	public OldAuthEntity(JSONObject json) {
		try {
			setUserId(json.getInt("id"));
		} catch (JSONException e) {

		}
		try {
			setToken(json.getString("token"));
		} catch (JSONException e) {

		}
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
}
