/*
 *  Airline Manager
 *  
 *  Copyright (C) 2010-2018 by Christian Lins <christian@lins.me>
 *  All rights reserved.
 */
package me.lins.airman.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import javax.annotation.PostConstruct;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.Timer;
import me.lins.airman.sim.SimulationFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author chris
 */
@Component
public class TimeControlWindow extends GameWindow {
    @Autowired
    private SimulationFactory simFactory;
    
    private final JLabel lblTime = new JLabel();
    private final JButton btnFaster = new JButton(">>");
    private final JButton btnSlower = new JButton("<");
    
    @PostConstruct
    protected void init() {       
        setTitle("Time");
        setClosable(false);
        setMaximizable(false);
        
        initLayout();
        
        ActionListener updater = (ActionEvent e) -> {
            lblTime.setText(simFactory.current().getDateTime()
                    .format(DateTimeFormatter.RFC_1123_DATE_TIME));
        };
        Timer timer = new Timer(1000, updater);
        timer.start();
    }
    
    private void initLayout() {
        getContentPane().setLayout(new FlowLayout());
        getContentPane().add(lblTime);
        getContentPane().add(btnSlower);
        getContentPane().add(btnFaster);
        setSize(350, 70);
        
        btnSlower.addActionListener((ActionEvent e) -> {
            simFactory.current().decreaseTimeSpeed();
        });
        
        btnFaster.addActionListener((ActionEvent e) -> {
            simFactory.current().increaseTimeSpeed();
        });
    }
}
