package com.lb.logic;

import android.app.Activity;
import android.location.Location;

public interface ILocationUpdateServiceClient
{
    /**
     * A new location fix has been obtained.
     *
     * @param loc
     */
    public void onLocationUpdate(Location loc);

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
