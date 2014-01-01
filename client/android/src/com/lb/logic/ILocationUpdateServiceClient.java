package com.lb.logic;

import org.json.JSONObject;

import android.app.Activity;
import android.location.Location;

public interface ILocationUpdateServiceClient
{
    /**
     * A new location fix has been obtained.
     *
     * @param json
     */
    public void onLocationUpdate(JSONObject json);

    /**
     * Asking the calling activity form to indicate that logging has stopped
     */
    public void onStopLogging();

    /**
     * Returns the activity
     *
     * @return
     */
    public Activity getActivity();
}
