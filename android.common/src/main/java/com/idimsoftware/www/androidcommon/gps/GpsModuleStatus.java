package com.idimsoftware.www.androidcommon.gps;

import android.location.Location;

/**
 * Holds information about the current status of the Gps module.
 */
public class GpsModuleStatus {

    /*
    The GPS module is stopped and has not yet determined any position.
     */
    public static final int IDLE = 0;
    /*
    The GPS module is disabled in the operating system and cannot be used.
     */
    public static final int DISABLED = 1;
    /*
    The GPS module is trying to get a fix.
     */
    public static final int SEARCHING = 2;
    /*
    The GPS module tried to get a fix but wasn't able to within the set timeout interval.
     */
    public static final int TIMED_OUT = 3;
    /*
    The GPS module has been able to fix and is reporting updated positions.
     */
    public static final int FIXED = 4;
    /*
    The GPS module is not running but has a fix that is not determined to be too old.
     */
    public static final int PAUSED = 5;

    private int _status;
    private float _latitude;
    private float _longitude;
    private float _accuracy;
    private float _bearing;

    // Methods

    /*
    Updates the status and other data based on individual values.
     */
    public void set(int status, float latitude, float longitude, float accuracy, float bearing){
        _status = status;
        _latitude = latitude;
        _longitude = longitude;
        _accuracy = accuracy;
        _bearing = bearing;
    }

    /*
    Updates the status and other data based on a Location.
     */
    public void set(Location location){
        // Too low accuracy is treated as no fix
        int status = GpsModuleStatus.FIXED;
        if (location.getAccuracy() < 1.0F)
            status = GpsModuleStatus.SEARCHING;

        this.set(status, (float) location.getLatitude(), (float) location.getLongitude(),
                location.getAccuracy(), location.getBearing());
    }

    // Properties

    public int getStatus(){
        return _status;
    }

    public void setStatus(int value){
        _status = value;
    }

    public float getLatitude(){
        return _latitude;
    }

    public float getLongitude(){
        return _longitude;
    }

    public float getAccuracy() { return _accuracy; }

    public float getBearing() { return _bearing; }
}
