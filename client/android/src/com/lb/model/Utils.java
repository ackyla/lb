package com.lb.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.google.android.gms.maps.model.LatLng;
import com.lb.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.format.DateUtils;
import android.view.WindowManager.BadTokenException;

public class Utils {

    public static ProgressDialog createProgressDialog(Context mContext) {
        ProgressDialog dialog = new ProgressDialog(mContext);
        try {
                dialog.show();
        } catch (BadTokenException e) {

        }
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.progress_dialog);
        // dialog.setMessage(Message);
        return dialog;
    }
	
    public static Date parseStringToDate(String dateString) {
    	Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss");
        
        try {
			date = sdf.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        return date;
    }
    
    /**
     * 現在時刻との相対時間を返す
     * @param date
     * @return
     */
    public static String getRelativeTimeSpanString(Date date) {
    	Date now = Calendar.getInstance().getTime();
    	return DateUtils.getRelativeTimeSpanString(date.getTime(), now.getTime(), DateUtils.FORMAT_ABBREV_RELATIVE).toString();
    }
    
    public static User updateSessionUserInfo(User user) {
    	User sessionUser = Session.getUser();
    	user.setId(sessionUser.getId());
    	user.setToken(sessionUser.getToken());
    	Session.setUser(user);
    	return user;
    }
    
    /**
     * 富士山のLatLngを返す
     * @return
     */
    public static LatLng getDefaultLatLng() {
    	return new LatLng(35.360549, 138.727786);
    }
}
