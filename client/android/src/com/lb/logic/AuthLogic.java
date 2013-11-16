package com.lb.logic;

import java.util.List;

import org.json.JSONObject;

import com.lb.dao.OldAuthDAO;
import com.lb.dao.OldAuthEntity;

import android.content.Context;
import android.util.Log;

public class AuthLogic extends LogicBase {
	
	public AuthLogic(Context context) {
		super(context);
	}
	
	/**
	 * ユーザを取得する
	 * @return AuthEntity
	 */
	public OldAuthEntity getAuth() {
		OldAuthDAO authDAO = this.getAuthDAO();
		try	{
			return authDAO.get();
		} catch(Exception e) {
			Log.v("home", "error="+e.toString());
			return null;
		}
	}
	
	/**
	 * ユーザIDを取得する
	 * @return
	 */
	public int getUserId() {
		OldAuthEntity auth = this.getAuth();
		
		if(auth == null){
			return 0;
		}
		
		return auth.getUserId();
	}
	
	/**
	 * ユーザ登録しているかチェックする
	 * @return boolean
	 */
	public boolean checkRegister() {
		if(getAuth() != null){
			return true;
		}
		return false;
	}
	
	/**
	 * user_idとtokenを登録する
	 * @param name
	 * @return boolean
	 */
	public boolean register(JSONObject json) {
		OldAuthDAO authDAO = getAuthDAO();
		OldAuthEntity authEntity = new OldAuthEntity(json);
		
		try {
			authDAO.insert(authEntity);
		} catch (Exception e) {
			return false;
		}

		return true;
	}
	
	/**
	 * 名前変更
	 * @param name
	 * @return
	 */
	public boolean rename(String name) {
		
		// TODO
		
		return true;
	}
	
	/**
	 * ユーザを削除する
	 * @return boolean
	 */
	public boolean delete() {
		
		// TODO 通信

		return true;
	}
	
	private OldAuthDAO getAuthDAO() {
		return new OldAuthDAO(context);
	}
}
