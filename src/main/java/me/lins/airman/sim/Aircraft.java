/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.lins.airman.sim;

import java.util.List;

/**
 *
 * @author chris
 */
public class Aircraft {
    private EarthPosition position;
    private List<EarthPosition> route;
    
    // Speed in km/h
    private float speed;
    
    public EarthPosition getPosition() {
        return position;
    }
    
    public void setSpeed(float speed) {
        this.speed = speed;
    }
    
    public void setPosition(EarthPosition position) {
        this.position = position;
    }
}
