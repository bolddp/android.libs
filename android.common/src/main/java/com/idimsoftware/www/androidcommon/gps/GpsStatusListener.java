package com.idimsoftware.www.androidcommon.gps;

/**
 * Listener interfaces for receiving GPS module updates.
 */
public interface GpsStatusListener {
    void onGpsStatus(GpsModuleStatus status);
}
