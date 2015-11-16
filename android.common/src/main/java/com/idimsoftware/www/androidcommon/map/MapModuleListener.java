package com.idimsoftware.www.androidcommon.map;

import com.google.android.gms.common.ConnectionResult;

/**
 * Listener for events of the map module.
 */
public interface MapModuleListener {
    void onSucceeded();

    void onFailed(int connectionResult);
}
