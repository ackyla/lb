package com.lb.model;

import com.lb.DaoSession;
import com.lb.db.LbDbOpenHelper;

import android.app.Application;

public class Session extends Application {

	private static LbDbOpenHelper dbHelper;
	private static User user;
    private static boolean towerEnabled;
    private static boolean gpsEnabled;
    private static boolean isStarted;
    private static boolean isUsingGps;
    private static boolean isBound;
	
    @Override
    public void onCreate() {
    	super.onCreate();
    	
    	this.dbHelper = new LbDbOpenHelper(this, null);
    }
    
    public static User getUser() {
    	return user;
    }
    public static void setUser(User user) {
    	Session.user = user;
    }
    
    public DaoSession getDaoSession() {
    	return this.dbHelper.getDaoSession();
    }
    
    public static void setIsStarted(boolean isStarted) {
    	Session.isStarted = isStarted;
    }
    public static boolean getIsStarted() {
    	return isStarted;
    }
    
    public static void setIsBound(boolean isBound) {
        Session.isBound = isBound;
    }
    public static boolean getIsBound() {
        return isBound;
    }
}
