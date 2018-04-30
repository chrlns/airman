/*
 *  Airline Manager
 *  
 *  Copyright (C) 2018 by Christian Lins <christian@lins.me>
 *  All rights reserved.
 */
package me.lins.airman;

import me.lins.airman.sim.Simulation;
import me.lins.airman.sim.SimulationFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author chris
 */
@Configuration
@ComponentScan
public class App {    
    public static void main(String[] args) {
        ApplicationContext context = 
            new AnnotationConfigApplicationContext(App.class);
        
        MainFrame mf = context.getBean(MainFrame.class);
        mf.setVisible(true);
        
        SimulationFactory simFac = context.getBean(SimulationFactory.class);
        Simulation sim = simFac.create(1950);
        sim.fillWithTestData();
        sim.run();
    }
}
