/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.lins.airman.sim;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * A simulated world.
 * @author chris
 */
@EnableAsync
public class Simulation {
    private List<Aircraft> aircrafts = new ArrayList<>();
    
    private boolean isRunning = true;
    
    // How many (simulation) seconds will elapse per tick (= realtime second)?
    private int secondsPerTick = 1;
    
    private ZonedDateTime dateTime;
    
    public Simulation(int startYear) {
        dateTime = ZonedDateTime.of(startYear, 1, 1, 0, 0, 0, 0, ZoneId.of("GMT"));
    }
    
    public ZonedDateTime getDateTime() {
        return dateTime;
    }
    
    public void increaseTimeSpeed() {
        secondsPerTick *= 2;
    }
    
    public void decreaseTimeSpeed() {
        if (secondsPerTick > 1) {
            secondsPerTick /= 2;
        }
    }
    
    public void fillWithTestData() {
        Aircraft ac = new Aircraft();
        ac.setSpeed(800);
        
    }
    
    @Async
    public void run() {
        try {
            while (isRunning) {
                long then = System.currentTimeMillis();

                for (int n = 0; n < secondsPerTick; n++) {
                    tick();
                }
                
                dateTime = dateTime.plusSeconds(secondsPerTick);

                long now = System.currentTimeMillis();

                Thread.sleep(1000 - (now - then));
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            isRunning = false;
        }
    }
    
    /**
     * Do all the simulation work here.
     */
    protected void tick() {
        // Let all the aircrafts start, fly, and land
        
        // Let the passengers do some booking
        
        // Make company decisions (airlines, airports, manufacturer)
        
        // Make investments (stock exchange)
        
        // Roll the dice (events)
    }
}
