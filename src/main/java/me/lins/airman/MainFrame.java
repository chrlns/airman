/*
 *  Airline Manager
 *  
 *  Copyright (C) 2010-2018 by Christian Lins <christian@lins.me>
 *  All rights reserved.
 */
package me.lins.airman;

import javax.annotation.PostConstruct;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import me.lins.airman.gui.DebugWindow;
import me.lins.airman.gui.FlightsMapWindow;
import me.lins.airman.gui.TimeControlWindow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author chris
 */
@Component
public class MainFrame extends JFrame {
    @Autowired
    private TimeControlWindow timeControlWindow;
    
    @Autowired
    private FlightsMapWindow flightsMapWindow;
    
    @Autowired
    private DebugWindow debugWindow;
    
    @PostConstruct
    protected void init() {
        setTitle("Airline Manager");
        setSize(1600, 900);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        JDesktopPane desktop = new JDesktopPane();
        setContentPane(desktop);
        desktop.add(timeControlWindow);
        desktop.add(flightsMapWindow);
        desktop.add(debugWindow);
    }
}
