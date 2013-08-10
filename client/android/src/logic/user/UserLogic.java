package logic.user;

import java.util.List;

import dao.user.UserDAO;
import dao.user.UserEntity;
import logic.base.LogicBase;
import logic.db.DBLogic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import api.API;

public class UserLogic extends LogicBase {
	
	private static SQLiteDatabase db;
	
	public UserLogic(Context context) {
		super(context);
		
		DBLogic dbLogic = new DBLogic(super.context);
		this.db = dbLogic.connect();
	}
	
	/**
	 * ユーザ登録しているかチェックする
	 * @return boolean
	 */
	public boolean checkRegister() {
		UserDAO userDAO = this.getDAO();
		List<UserEntity> userList = userDAO.get();
		Log.v("main", "userList="+userList);
		if(userList.size() == 1){
			return true;
		}
		return false;
	}
	
	/**
	 * 名前をサーバに送って返ってきたuser_idとtokenを登録する
	 * @param name
	 * @return boolean
	 */
	public boolean register(String name) {
		//API api = new API(name);

		int userId = 1;//api.getUserId();
		String token = "hoge";//api.getToken();
    
		UserDAO userDAO = this.getDAO();
		UserEntity userEntity = new UserEntity();
		userEntity.setName(name);
		userEntity.setUserId(userId);
		userEntity.setToken(token);
		
		try	{
			userDAO.insert(userEntity);
		} catch(Exception e){
			return false;
		}
		
		return true;
	}
	
	private UserDAO getDAO() {
		return new UserDAO(this.db);
	}
}
