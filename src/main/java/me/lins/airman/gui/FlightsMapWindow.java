/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.lins.airman.gui;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author chris
 */
@Component
public class FlightsMapWindow extends GameWindow {
        
    @Autowired
    private FlightsMapView flightsMapView;
    
    @PostConstruct
    protected void initLayout() {
        setTitle("Flights Map");
        setSize(800, 600);
        setContentPane(flightsMapView);
    }
}
