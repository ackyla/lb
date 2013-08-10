package logic.user;

import logic.base.LogicBase;
import logic.db.DBLogic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import api.API;

public class UserLogic extends LogicBase {
	
	public UserLogic(Context context) {
		super(context);
	}
	
	/**
	 * ユーザ登録しているかチェックする
	 * @return boolean
	 */
	public boolean checkRegister() {
		DBLogic dbLogic = new DBLogic(super.context);
		SQLiteDatabase db = dbLogic.connect();
		
		Cursor cursor = null;
        try{
        	cursor = db.query( "user", null, null, null, null, null, null );
        	if(cursor.getCount() == 1){
        		return true;
        	}
        }finally{
        	if( cursor != null ){
        		cursor.close();
            }
        }

		return false;
	}
	
	/**
	 * 名前をサーバに送って返ってきたuser_idとtokenを登録する
	 * @param name
	 * @return boolean
	 */
	public boolean register(String name) {
		API api = new API(name);

		int userId = api.getUserId();
		String token = api.getToken();
    
		DBLogic logic = new DBLogic(super.context);
		SQLiteDatabase db = logic.connect();

        ContentValues val = new ContentValues();
        val.put("name", name);
        val.put("user_id", userId);
        val.put("token", token);
        
        if(db.insert("user", null, val) > 0){
        	return true;
        }
        return false;
	}
}
