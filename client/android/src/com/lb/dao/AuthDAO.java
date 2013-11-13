package com.lb.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.lb.logic.DBLogic;

import android.content.ContentValues;
import android.content.Context;

public class AuthDAO {
	private static final String TABLE_NAME = "auth";
	private static final String COLUMN_USER_ID = "user_id";
	private static final String COLUMN_TOKEN = "token";
	private static final String[] COLUMNS = { COLUMN_USER_ID, COLUMN_TOKEN };

	private DBLogic dbLogic;

	/**
	 * コンストラクタ
	 */
	public AuthDAO(Context context) {
		this.dbLogic = new DBLogic(context);
	}

	/**
	 * 認証情報の取得
	 * @return AuthEntity
	 */
	public AuthEntity get() {
		List<AuthEntity> entityList = new ArrayList<AuthEntity>();

		List<ContentValues> res = dbLogic.query(TABLE_NAME, COLUMNS, null, null, null,
				null, null);

		Iterator<ContentValues> i = res.iterator();
		while (i.hasNext()) {
			ContentValues values = i.next();
			AuthEntity entity = new AuthEntity();
			entity.setUserId(values.getAsInteger("user_id"));
			entity.setToken(values.getAsString("token"));
			entityList.add(entity);
		}

		return entityList.get(0);
	}

	/**
	 * データの登録
	 * 
	 * @param data
	 * @return
	 */
	public long insert(AuthEntity entity) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_USER_ID, entity.getUserId());
		values.put(COLUMN_TOKEN, entity.getToken());
		return dbLogic.insert(TABLE_NAME, null, values);
	}

	/**
	 * データの更新
	 * 
	 * @param rowid
	 * @param date
	 * @return
	 */
	public int update(AuthEntity entity) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_USER_ID, entity.getUserId());
		values.put(COLUMN_TOKEN, entity.getToken());
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
