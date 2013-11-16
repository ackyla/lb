package com.lb.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.lb.DaoMaster;
import com.lb.DaoMaster.OpenHelper;
import com.lb.DaoSession;

public class LbDbOpenHelper extends OpenHelper {

	private static final String DB_NAME = "lb.db";
	
	private SQLiteDatabase db = null;
	private DaoMaster daoMaster = null;
	private DaoSession daoSession = null;
	
	public LbDbOpenHelper(Context context, CursorFactory factory) {
		super(context, DB_NAME, factory);
		
		this.getDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		super.onCreate(db);
		
		DaoMaster daoMaster = new DaoMaster(db);
		DaoSession daoSession = daoMaster.newSession();
		
		
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}
	
	public SQLiteDatabase getDatabase() {
		if(this.db == null) {
			this.db = this.getWritableDatabase();
		}
		
		return this.db;
	}
	
	public DaoMaster getDaoMaster() {
		if(this.daoMaster == null) {
			this.daoMaster = new DaoMaster(this.getDatabase());
		}
		
		return this.daoMaster;
	}
	
	public DaoSession getDaoSession() {
		if(this.daoSession == null) {
			this.daoSession = this.getDaoMaster().newSession();
		}
		
		return this.daoSession;
	}
}
