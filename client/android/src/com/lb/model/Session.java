package com.lb.model;

import android.app.Application;

public class Session extends Application {

    private static boolean towerEnabled;
    private static boolean gpsEnabled;
    private static boolean isStarted;
    private static boolean isUsingGps;
    private static boolean isBound;
	
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
