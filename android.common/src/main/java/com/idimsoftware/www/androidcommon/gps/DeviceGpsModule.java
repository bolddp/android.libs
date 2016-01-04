package com.idimsoftware.www.androidcommon.gps;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Handles the real device GPS module, reporting changes of the status and
 * position through a GpsStatusListener.
 */
public class DeviceGpsModule implements GpsModule, LocationListener {

    public static final int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 101;

    // Private variables

    private final LocationManager _manager;

    private GpsStatusListener _listener;
    private GpsModuleStatus _gpsModuleStatus;
    private Activity _activity;
    private boolean _isRequestingPermission;

    // Protected methods

    /*
    Calls the GpsStatusListener callback, if it has been defined.
     */
    protected void onStatusUpdated(){
        if (_listener != null)
            _listener.onGpsStatus(_gpsModuleStatus);

    }

    public DeviceGpsModule(Context context) {
        _gpsModuleStatus = new GpsModuleStatus();
        _manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        // Check if the GPS service is disabled
        if (!_manager.isProviderEnabled( LocationManager.GPS_PROVIDER ))
            _gpsModuleStatus.setStatus(GpsModuleStatus.DISABLED);
    }

    // Public methods

    public void start(Activity activity, GpsStatusListener listener){
        _activity = activity;
        _listener = listener;

        // Are we disabled? Then report that and do nothing more
        if (_gpsModuleStatus.getStatus() == GpsModuleStatus.DISABLED)
            onStatusUpdated();

        // Make sure we only ask permission once
        if (_isRequestingPermission)
            return;

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            _isRequestingPermission = true;
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_ACCESS_FINE_LOCATION);
            return;
        }

        _manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        // Update and report status
        _gpsModuleStatus.setStatus(GpsModuleStatus.SEARCHING);
        onStatusUpdated();
    }

    /*
    Should be called when a new start attempt is performed after GPS permission
    has been requested from the user.
     */
    public void startAfterPermissionRequest(Activity activity){
        _isRequestingPermission = false;
        start(activity, _listener);
    }

    public void stop(Activity activity){
        // Is the GPS module running at the moment? Otherwise perform no further action
        int status = _gpsModuleStatus.getStatus();
        if ((status != GpsModuleStatus.FIXED) && (status != GpsModuleStatus.SEARCHING))
            return;

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_ACCESS_FINE_LOCATION);
            return;
        }

        _manager.removeUpdates(this);
        _gpsModuleStatus.setStatus(GpsModuleStatus.PAUSED);
        onStatusUpdated();
    }

    @Override
    public void onLocationChanged(Location location) {
        _gpsModuleStatus.set(location);
        onStatusUpdated();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        if (provider.equals(LocationManager.GPS_PROVIDER)){
            // Go to the IDLE status to signal the change
            if (_gpsModuleStatus.getStatus() == GpsModuleStatus.DISABLED){
                _gpsModuleStatus.setStatus(GpsModuleStatus.IDLE);
                onStatusUpdated();
            }
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        if (provider.equals(LocationManager.GPS_PROVIDER)) {
            // No matter what the current status is, we set DISABLED
            _gpsModuleStatus.setStatus(GpsModuleStatus.DISABLED);
            onStatusUpdated();
        }
    }

    /*
    This method should be called from an activity where the user has denied GPS access.
     */
    public void stopAfterPermissionRequest(Activity activity) {
        // Reset this flag in case the activity is reactivated without the app starting over
        _isRequestingPermission = false;
    }
}
