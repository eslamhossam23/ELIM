/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.bichoymessiha.myapplication.backend.models;

import com.sun.jndi.toolkit.dir.SearchFilter;
import java.io.Serializable;

/**
 *
 * @author bichoymessiha
 */
public class LocationdBTriple implements Serializable{
    private double latitude;
    private double longitude;
    private float dB;

    public LocationdBTriple() {
    }

    public LocationdBTriple(double latitude, double longitude, float dB) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.dB = dB;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
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
