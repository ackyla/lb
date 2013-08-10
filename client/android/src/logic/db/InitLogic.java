package logic.db;

import logic.base.LogicBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class InitLogic extends LogicBase{
	
	private DBHelper helper;
	
	private static final String DB_NAME = "lb.db";
	private static final int DB_VERSION = 1;
	
	public InitLogic(Context context) {
		super(context);
		
		helper = new DBHelper(context, DB_NAME, null, DB_VERSION);
	}
	
	public SQLiteDatabase open() {
		return helper.getWritableDatabase();
	}
}