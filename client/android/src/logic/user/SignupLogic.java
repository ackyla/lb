package logic.user;

import logic.base.LogicBase;
import logic.db.InitLogic;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SignupLogic extends LogicBase {
	
	public SignupLogic(Context context) {
		super(context);
	}
	public boolean register(String name) {
		int userId;
		String token;
		// TODO nameで登録APIをコールする
		userId = 0;
		token = "hoge";
    
		InitLogic logic = new InitLogic(super.context);
		SQLiteDatabase db = logic.open();

        ContentValues val = new ContentValues();
        val.put("name", name);
        val.put("user_id", userId);
        val.put("token", token);
        Log.v("main", "insert="+db.insert("user", null, val));
        return true;
	}
}
