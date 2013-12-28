package com.lb.model;

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
	
}
