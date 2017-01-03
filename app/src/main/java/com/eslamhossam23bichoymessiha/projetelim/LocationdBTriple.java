/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eslamhossam23bichoymessiha.projetelim;

import java.io.Serializable;

/**
 *
 * @author bichoymessiha
 */
public class LocationdBTriple implements Serializable {
    private float latitude;
    private float longitude;
    private float dB;

    public LocationdBTriple(float latitude, float longitude, float dB) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.dB = dB;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getdB() {
        return dB;
    }

    public void setdB(float dB) {
        this.dB = dB;
    }

    @Override
    public String toString() {
        return "lat: " + latitude + " , long: " + longitude + " , dB:" + dB + '}';
    }

}
