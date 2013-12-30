package com.lb.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.lb.R;

import android.app.ProgressDialog;
import android.content.Context;
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
}
