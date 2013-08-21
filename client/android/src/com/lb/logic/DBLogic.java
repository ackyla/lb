package com.lb.logic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBLogic extends LogicBase{
	
	private DBHelper helper;
	
	private static final String DB_NAME = "lb.db";
	private static final int DB_VERSION = 2;
	
	public DBLogic(Context context) {
		super(context);
	}
	
	private DBHelper getHelper() {
		return new DBHelper(context, DB_NAME, null, DB_VERSION);
	}
	
	public List<ContentValues> query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
		SQLiteDatabase db = getHelper().getReadableDatabase();
		Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
		List<ContentValues> res = new ArrayList<ContentValues>();
		while(cursor.moveToNext()){
			ContentValues values = new ContentValues();
			for(int i = 0; i < cursor.getColumnCount(); i ++){				
				DatabaseUtils.cursorStringToContentValues(cursor, cursor.getColumnName(i), values);
			}
			res.add(values);
		}
		cursor.close();
		db.close();
		return res;
	}
	
	public long insert(String table, String nullColumnHack, ContentValues values) {
		long ret = -1;
		SQLiteDatabase db = getHelper().getWritableDatabase();
		db.beginTransaction();		
		try {
			ret = db.insert(table, nullColumnHack, values);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}
		
		return ret;
	}
	
	public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
		int ret = -1;
		SQLiteDatabase db = getHelper().getWritableDatabase();
		db.beginTransaction();		
		try {
			ret = db.update(table, values, whereClause, whereArgs);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}
		
		return ret;
	}
	
	public int delete(String table, String whereClause, String[] whereArgs) {
		int ret = -1;
		SQLiteDatabase db = getHelper().getWritableDatabase();
		db.beginTransaction();		
		try {
			ret = db.delete(table, whereClause, whereArgs);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}
		
		return ret;
	}
}