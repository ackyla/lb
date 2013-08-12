package dao.room;

import org.json.JSONObject;

public class RoomEntity {
	private int id;
	private String title;
	
	public RoomEntity() {
	}
	public RoomEntity(JSONObject json) {
		try	{
			setId(json.getInt("id"));
			setTitle(json.getString("title"));
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
}
