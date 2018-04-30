/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.lins.airman.sim;

/**
 *
 * @author chris
 */
public class EarthPosition {
    private float lat, lon;
    
    public EarthPosition(float lat, float lon) {
        this.lat = lat;
        this.lon = lon;
    }
    
    public float getLatitude() {
        return lat;
    }
    
    public float getLongitude() {
        return lon;
    }
}
