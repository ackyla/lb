package logic.room;

import java.util.List;

import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;

import dao.room.RoomEntity;
import dao.user.UserEntity;

import android.content.Context;
import android.util.Log;
import api.API;
import logic.base.LogicBase;
import logic.user.UserLogic;

public class RoomLogic extends LogicBase {

	public RoomLogic(Context context) {
		super(context);
	}

	public List<RoomEntity> get() {
		return null;
	}
	
	public int create(String title) {
		UserLogic userLogic = new UserLogic(context);
		UserEntity userEntity = userLogic.getUser();
		API.createRoom(userEntity, title, new JsonHttpResponseHandler() {
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
