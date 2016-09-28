package com.idimsoftware.www.androidcommon.gps;

import android.app.Activity;

/**
 * Created by Daniel on 2015-11-29.
 */
public class MockGpsModule implements GpsModule {
    // Private variables

    private final float initialLatitude = 57.732308F;
    private final float initialLongitude = 14.114135F;
    private final float fixedAccuracy = 8.0F;

    private GpsStatusListener _listener;
    private GpsModuleStatus _gpsModuleStatus;

    public MockGpsModule() {
        // Initialize the status
        _gpsModuleStatus = new GpsModuleStatus();
        _gpsModuleStatus.setLatitude(initialLatitude);
        _gpsModuleStatus.setLongitude(initialLongitude);
        _gpsModuleStatus.setAccuracy(fixedAccuracy);
        _gpsModuleStatus.setStatus(GpsModuleStatus.FIXED);
    }

    // Methods (GpsModule)

    @Override
    public void start(Activity activity, GpsStatusListener listener) {
        _listener = listener;
        // Send out position
        _listener.onGpsStatus(_gpsModuleStatus);
    }

    @Override
    public void stop(Activity activity) {
    }

    @Override
    public void startAfterPermissionRequest(Activity activity) {
    }

    @Override
    public void stopAfterPermissionRequest(Activity activity) {
    }

    /*
    Sets the location of the mocked GPS module.
     */
    public void setLocation(float latitude, float longitude) {
        _gpsModuleStatus.setLatitude(latitude);
        _gpsModuleStatus.setLongitude(longitude);
        _gpsModuleStatus.setAccuracy(fixedAccuracy);

        _listener.onGpsStatus(_gpsModuleStatus);
    }
}
