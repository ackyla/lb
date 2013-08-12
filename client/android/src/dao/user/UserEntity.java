package dao.user;

import org.json.JSONObject;

public class UserEntity {
	private String name;
	private int userId;
	private String token;
	private int roomId;
	
	public UserEntity() {
	}
	public UserEntity(JSONObject json) {
		try	{			
			setName(json.getString("name"));
			setUserId(json.getInt("id"));
			setToken(json.getString("token"));
			setRoomId(json.getInt("roomId"));
		} catch(Exception e){
			
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
