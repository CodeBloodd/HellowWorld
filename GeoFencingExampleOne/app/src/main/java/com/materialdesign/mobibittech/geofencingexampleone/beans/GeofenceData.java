package com.materialdesign.mobibittech.geofencingexampleone.beans;

import android.provider.SyncStateContract;


import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.model.LatLng;

import java.util.concurrent.TimeUnit;


/**
 * Created by rajendra on 8/11/2015.
 */
public class GeofenceData {
    private String id;
    private String name;
    private LatLng latLong;
    private int radius;
    private int duration;
    private int time;
    private int expiretime;
    private String description;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getExpiretime() {
        return expiretime;
    }

    public void setExpiretime(int expiretime) {
        this.expiretime = expiretime;
    }

    Geofence builder;

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Geofence getBuilder() {
        return builder;
    }

    public void setBuilder(Geofence builder) {
        this.builder = builder;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getLatLong() {
        return latLong;
    }

    public void setLatLong(LatLng latLong) {
        this.latLong = latLong;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }


    public void setFencing() {

        builder =  new Geofence.Builder()
            // Set the request ID of the geofence. This is a string to identify this
            // geofence.
            .setRequestId(id)

            .setCircularRegion(
                   latLong.latitude,
                    latLong.longitude,radius

            )
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                    Geofence.GEOFENCE_TRANSITION_EXIT)
            .build();
}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Geofence getFencing()
{
    return builder;
}

    @Override
    public String toString() {
        return name;
    }


}
