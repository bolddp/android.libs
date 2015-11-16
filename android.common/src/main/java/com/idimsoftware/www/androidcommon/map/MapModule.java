package com.idimsoftware.www.androidcommon.map;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * Helper module for managing Google Maps API.
 */
public class MapModule implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private FragmentActivity _context;
    private GoogleMap _map;
    private GoogleApiClient _googleApiClient;
    private int _mapResId;
    private MapModuleListener _listener;


    protected synchronized void buildGoogleApiClient() {
        _googleApiClient = new GoogleApiClient.Builder(_context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public MapModule(FragmentActivity _context) {
        _context = _context;
    }

    public void start(int mapResId, MapModuleListener listener){
        _mapResId = mapResId;
        _listener = listener;

        // First check if Google Play is available
        if (isGooglePlayAvailable()){
            // Build the API client which will hook up to the Maps API
            buildGoogleApiClient();
        }
    }

    /*
    Checking the google play services is available
     */
    public boolean isGooglePlayAvailable() {
        int connectionResult = GooglePlayServicesUtil.isGooglePlayServicesAvailable(_context);
        boolean result = (connectionResult == ConnectionResult.SUCCESS);
        if (!result)
            _listener.onFailed(connectionResult);

        return result;
    }

    public GoogleMap getMap(){
        return _map;
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (_map == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) _context.getSupportFragmentManager().findFragmentById(_mapResId);
            _map = mapFragment.getMap();
            // Configure the map
            _map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            _map.setMyLocationEnabled(true);
        }

        _listener.onSucceeded();
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Indicate that the API seems to be malfunctioning
        _listener.onFailed(ConnectionResult.API_UNAVAILABLE);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        _listener.onFailed(connectionResult.getErrorCode());
    }
}
