package logic.db;

import logic.base.LogicBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DBLogic extends LogicBase{
	
	private DBHelper helper;
	
	private static final String DB_NAME = "lb.db";
	private static final int DB_VERSION = 2;
	
	public DBLogic(Context context) {
		super(context);
		
		helper = new DBHelper(context, DB_NAME, null, DB_VERSION);
	}
	
	public SQLiteDatabase connect() {
		return helper.getWritableDatabase();
	}
}