/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.lins.airman.sim;

import org.springframework.stereotype.Component;

/**
 *
 * @author chris
 */
@Component
public class SimulationFactory {
    private Simulation current;
    
    public Simulation create(int startYear) {
        current = new Simulation(startYear);
        return current;
    }
    
    public Simulation current(){
        return current;
    }
}
