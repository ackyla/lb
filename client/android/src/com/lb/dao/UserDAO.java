package com.lb.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.lb.logic.DBLogic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class UserDAO {
	private static final String TABLE_NAME = "user";
	private static final String COLUMN_NAME = "name";
	private static final String COLUMN_USER_ID = "user_id";
	private static final String COLUMN_TOKEN = "token";
	private static final String COLUMN_ROOM_ID = "room_id";
	private static final String[] COLUMNS = { COLUMN_NAME, COLUMN_USER_ID,
			COLUMN_TOKEN, COLUMN_ROOM_ID };

	private DBLogic dbLogic;

	/**
	 * コンストラクタ
	 */
	public UserDAO(Context context) {
		this.dbLogic = new DBLogic(context);
	}

	/**
	 * 全データの取得
	 * 
	 * @return List<UserEntity>
	 */
	public List<UserEntity> get() {
		List<UserEntity> entityList = new ArrayList<UserEntity>();

		List<ContentValues> res = dbLogic.query(TABLE_NAME, COLUMNS, null, null, null,
				null, null);

		Iterator<ContentValues> i = res.iterator();
		while (i.hasNext()) {
			ContentValues values = i.next();
			UserEntity entity = new UserEntity();
			entity.setName(values.getAsString("name"));
			entity.setUserId(values.getAsInteger("user_id"));
			entity.setToken(values.getAsString("token"));
			entity.setRoomId(values.getAsInteger("room_id"));
			entityList.add(entity);
		}

		return entityList;
	}

	/**
	 * データの登録
	 * 
	 * @param data
	 * @return
	 */
	public long insert(UserEntity entity) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME, entity.getName());
		values.put(COLUMN_USER_ID, entity.getUserId());
		values.put(COLUMN_TOKEN, entity.getToken());
		values.put(COLUMN_ROOM_ID, entity.getRoomId());
		return dbLogic.insert(TABLE_NAME, null, values);
	}

	/**
	 * データの更新
	 * 
	 * @param rowid
	 * @param date
	 * @return
	 */
	public int update(UserEntity entity) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME, entity.getName());
		values.put(COLUMN_USER_ID, entity.getUserId());
		values.put(COLUMN_TOKEN, entity.getToken());
		values.put(COLUMN_ROOM_ID, entity.getRoomId());
		// String whereClause = COLUMN_ID + "=" + entity.getRowId();
		return dbLogic.update(TABLE_NAME, values, null, null);
	}

	/**
	 * データの削除
	 * 
	 * @return int
	 */
	public int delete() {
		// String whereClause = COLUMN_ID + "=" + rowId;
		return dbLogic.delete(TABLE_NAME, null, null);
	}
}
