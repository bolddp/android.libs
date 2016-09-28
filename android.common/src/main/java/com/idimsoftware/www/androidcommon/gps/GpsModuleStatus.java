package com.idimsoftware.www.androidcommon.gps;

import android.location.Location;
import android.location.LocationManager;

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
    private Location _location;

    public GpsModuleStatus() {
        // Create an initial location object
        _location = new Location(LocationManager.GPS_PROVIDER);
    }

    // Methods

    /*
    Updates the status and other data based on a Location.
     */
    public void set(Location location){
        // Too low accuracy is treated as no fix
        int status = GpsModuleStatus.FIXED;
        if (location.getAccuracy() < 1.0F)
            status = GpsModuleStatus.SEARCHING;

        _status = status;
        _location = location;
    }

    // Properties

    public int getStatus(){
        return _status;
    }

    public void setStatus(int value){
        _status = value;
    }

    public float getLatitude(){
        return (float) _location.getLatitude();
    }

    public void setLatitude(float latitude) {
        _location.setLatitude(latitude);
    }

    public float getLongitude(){
        return (float) _location.getLongitude();
    }

    public void setLongitude(float longitude){
        _location.setLongitude(longitude);
    }

    public float getAccuracy() { return _location.getAccuracy(); }

    public void setAccuracy(float accuracy) {
        _location.setAccuracy(accuracy);
    }

    public float getBearing() { return _location.getBearing(); }

    public Location getLocation() { return _location; }

}
