package com.lb.dao;

import org.json.JSONException;
import org.json.JSONObject;

public class OldRoomEntity {
	private int id;
	private String title;
	private int numUser;
	private boolean active;
	private int ownerId;

	public int getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(int i) {
		this.ownerId = i;
	}

	public OldRoomEntity() {
	}

	public OldRoomEntity(JSONObject json) {
		try {
			setId(json.getInt("id"));
		} catch (Exception e) {

		}
		try {
			setTitle(json.getString("title"));
		} catch (JSONException e) {

		}
		try {
			setNumUser(json.getInt("num_user"));
		} catch (JSONException e) {

		}
		try {
			setActive(json.getBoolean("active"));
		} catch (JSONException e) {

		}
		try {
			setOwnerId(json.getInt("owner_id"));
		} catch (JSONException e) {

		}
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	public void setNumUser(int numUser) {
		this.numUser = numUser;
	}

	public int getNumUser() {
		return this.numUser;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean getActive() {
		return this.active;
	}
}
