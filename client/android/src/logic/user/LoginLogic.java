package logic.user;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import logic.base.LogicBase;
import logic.db.InitLogic;

public class LoginLogic extends LogicBase {

	public LoginLogic(Context context) {
		super(context);
	}

	public void login(){
		InitLogic logic = new InitLogic(super.context);
		SQLiteDatabase db = logic.open();
		
        Cursor cursor = null;
        try{
        	cursor = db.query( "user", new String[]{ "name", "user_id", "token" }, "", null, null, null, null );
        	int indexName = cursor.getColumnIndex("name");
        	int indexUserId = cursor.getColumnIndex("user_id");
        	int indexToken = cursor.getColumnIndex("token");
        	
        	while(cursor.moveToNext()){
        		String name = cursor.getString(indexName);
        		int userId = cursor.getInt(indexUserId);
        		String token = cursor.getString(indexToken);
        		
        		Log.v("main", "name="+name+", user_id="+userId+", token="+token);
        	}
        }finally{
        	if( cursor != null ){
        		cursor.close();
            }
        }
	}
}
