package logic.db;

import logic.base.LogicBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class InitLogic extends LogicBase{
	
	public InitLogic(Context context) {
		super(context);
	}

	public SQLiteDatabase create() {
		
		SQLiteDatabase db;
		db = super.context.openOrCreateDatabase("db01_01", 1, null);
		
		return db;
	}
}
