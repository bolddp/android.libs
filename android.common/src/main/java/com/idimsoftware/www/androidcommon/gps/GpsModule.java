package com.idimsoftware.www.androidcommon.gps;

import android.app.Activity;

/**
 * Created by Daniel on 2015-11-29.
 */
public interface GpsModule {

    // Methods

    void start(Activity activity, GpsStatusListener listener);

    void stop(Activity activity);

    void startAfterPermissionRequest(Activity activity);

    void stopAfterPermissionRequest(Activity activity);
}
