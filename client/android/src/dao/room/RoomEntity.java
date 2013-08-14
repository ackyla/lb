package dao.room;

import org.json.JSONObject;

public class RoomEntity {
	private int id;
	private String title;
	private int numUser;
	
	public RoomEntity() {
	}
	public RoomEntity(JSONObject json) {
		try	{
			setId(json.getInt("id"));
			setTitle(json.getString("title"));
			setNumUser(json.getInt("num_user"));
		} catch(Exception e){
			
		}
	}
	
	public void setId(int id){
		this.id = id;
	}
	public int getId(){
		return this.id;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	public String getTitle(){
		return this.title;
	}
	
	public void setNumUser(int numUser){
		this.numUser = numUser;
	}
	public int getNumUser(){
		return this.numUser;
	}
}
