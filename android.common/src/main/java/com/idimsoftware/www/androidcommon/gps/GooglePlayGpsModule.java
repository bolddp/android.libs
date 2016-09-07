package com.idimsoftware.www.androidcommon.gps;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

/**
 * GPS Module that uses the device's GPS through Google Play Services
 * instead of through android.location.
 */
public class GooglePlayGpsModule implements GpsModule, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 101;

    // Private variables

    private final GoogleApiClient _googleApiClient;
    private LocationRequest _locationRequest;

    private Activity _activity;
    private GpsStatusListener _listener;
    private GpsModuleStatus _gpsModuleStatus;
    private boolean _isRequestingPermission;

    /**
     * Callback for location settings, containing info on how the request for location
     * info went.
     */
    private final ResultCallback<LocationSettingsResult> locationSettingsResultCallback = new ResultCallback<LocationSettingsResult>() {
        @Override
        public void onResult(@NonNull LocationSettingsResult result) {
            final Status status = result.getStatus();
            final LocationSettingsStates states = result.getLocationSettingsStates();

            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    // All location settings are satisfied. The client can
                    // initialize location requests here.
                    startLocationUpdates();
                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        status.startResolutionForResult(_activity,
                                MY_PERMISSIONS_ACCESS_FINE_LOCATION);
                    } catch (IntentSender.SendIntentException e) {
                        // Ignore the error.
                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    // Location settings are not satisfied. However, we have no way
                    // to fix the settings so we won't show the dialog.
                    _gpsModuleStatus.setStatus(GpsModuleStatus.DISABLED);
                    onStatusUpdated();

                    break;
            }

        }
    };

    private void startLocationUpdates() {
        // Make sure we have sufficient permissions
        if (ContextCompat.checkSelfPermission(_activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            _isRequestingPermission = true;
            ActivityCompat.requestPermissions(_activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_ACCESS_FINE_LOCATION);
            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(
                _googleApiClient, _locationRequest, this);
    }

    private void stopLocationUpdates(){
        LocationServices.FusedLocationApi.removeLocationUpdates(_googleApiClient, this);
    }

    /*
    Calls the GpsStatusListener callback, if it has been defined.
     */
    private void onStatusUpdated(){
        if (_listener != null)
            _listener.onGpsStatus(_gpsModuleStatus);
    }

    // Constructor

    public GooglePlayGpsModule(Context ctx) {
        // Create the API client
        _googleApiClient = new GoogleApiClient.Builder(ctx)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        _gpsModuleStatus = new GpsModuleStatus();
    }

    // Public methods

    @Override
    public void start(Activity activity, GpsStatusListener listener) {
        _activity = activity;
        _listener = listener;

        if (!_googleApiClient.isConnected())
            _googleApiClient.connect();
        else
            startLocationUpdates();
    }

    @Override
    public void stop(Activity activity) {
        stopLocationUpdates();

//        int status = _gpsModuleStatus.getStatus();
//        if ((status != GpsModuleStatus.FIXED) && (status != GpsModuleStatus.SEARCHING))
//            return;

        _gpsModuleStatus.setStatus(GpsModuleStatus.PAUSED);
        onStatusUpdated();
    }

    @Override
    public void startAfterPermissionRequest(Activity activity) {
        _isRequestingPermission = false;
        start(activity, _listener);
    }

    @Override
    public void stopAfterPermissionRequest(Activity activity) {
        // Reset this flag in case the activity is reactivated without the app starting over
        _isRequestingPermission = false;
    }

    @Override
    public void onLocationChanged(Location location) {
        _gpsModuleStatus.set(location);
        onStatusUpdated();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (_locationRequest == null){
            // Setup a location request with appropriate parameters
            _locationRequest = new LocationRequest();
            _locationRequest.setInterval(3000);
            _locationRequest.setFastestInterval(2000);
            _locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(_locationRequest);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(_googleApiClient,
                            builder.build());
            result.setResultCallback(locationSettingsResultCallback);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        // No code, onConnected will be called again hopefully and location updates will resume
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
