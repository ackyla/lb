package com.lb.logic;

import java.util.List;

import com.lb.dao.UserDAO;
import com.lb.dao.UserEntity;

import android.content.Context;
import android.util.Log;

public class UserLogic extends LogicBase {
	
	public UserLogic(Context context) {
		super(context);
	}
	
	/**
	 * ユーザを取得する
	 * @return UserEntity
	 */
	public UserEntity getUser() {
		UserDAO userDAO = this.getDAO();
		try	{
			List<UserEntity> userList = userDAO.get();
			Log.v("home", "userList="+userList.toString());
			if(userList.size() == 1) {
				UserEntity userEntity = userList.get(0);
				return userEntity;
			}else{
				return null;
			}
		} catch(Exception e) {
			Log.v("home", "error="+e.toString());
			return null;
		}
	}
	
	/**
	 * ユーザ登録しているかチェックする
	 * @return boolean
	 */
	public boolean checkRegister() {
		if(getUser() != null){
			return true;
		}
		return false;
	}
	
	/**
	 * 名前をサーバに送って返ってきたuser_idとtokenを登録する
	 * @param name
	 * @return boolean
	 */
	public boolean register(UserEntity userEntity) {
		UserDAO userDAO = getDAO();

		try {
			userDAO.insert(userEntity);
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
		
		// TODO 通信
		
		UserDAO userDAO = this.getDAO();
		UserEntity userEntity = getUser();
		
		if(userEntity == null){
			return false;
		}
		
		try	{
			userEntity.setName(name);
			userDAO.update(userEntity);
		} catch(Exception e) {
			return false;
		}

		return true;
	}
	
	/**
	 * ユーザを削除する
	 * @return boolean
	 */
	public boolean delete() {
		
		// TODO 通信
		
		UserDAO userDAO = this.getDAO();
		
		try	{
			userDAO.delete();
		} catch(Exception e){
			return false;
		}
		return true;
	}
	
	/**
	 * 入室状況をクライアント側に保存する
	 * @param roomId
	 * @return boolean
	 */
	public boolean enterRoom(UserEntity userEntity, int roomId) {
		Log.v("room", ""+userEntity.getName()+", "+roomId);
		UserDAO userDAO = this.getDAO();

		try {
			userEntity.setRoomId(roomId);
			userDAO.update(userEntity);
		} catch(Exception e){
			return false;
		}
		
		return true;
	}
	
	private UserDAO getDAO() {
		return new UserDAO(context);
	}
}
