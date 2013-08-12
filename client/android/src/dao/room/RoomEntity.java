package dao.room;

public class RoomEntity {
	private int id;
	private String title;
	
	public RoomEntity() {
		
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
