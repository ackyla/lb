package com.lb.logic;

import java.util.List;

import org.json.JSONObject;

import com.lb.dao.AuthDAO;
import com.lb.dao.AuthEntity;

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
	public AuthEntity getAuth() {
		AuthDAO authDAO = this.getAuthDAO();
		try	{
			List<AuthEntity> authList = authDAO.get();
			if(authList.size() > 0) {
				AuthEntity authEntity = authList.get(0);
				return authEntity;
			}else{
				return null;
			}
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
		AuthEntity auth = this.getAuth();
		
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
		AuthDAO authDAO = getAuthDAO();
		AuthEntity authEntity = new AuthEntity(json);
		
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
	
	private AuthDAO getAuthDAO() {
		return new AuthDAO(context);
	}
}
