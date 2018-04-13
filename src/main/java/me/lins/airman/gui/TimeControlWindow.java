/*
 *  Airline Manager
 *  
 *  Copyright (C) 2010-2018 by Christian Lins <christian@lins.me>
 *  All rights reserved.
 */
package me.lins.airman.gui;

import java.awt.FlowLayout;
import javax.annotation.PostConstruct;
import javax.swing.JButton;
import javax.swing.JLabel;
import org.springframework.stereotype.Component;

/**
 *
 * @author chris
 */
@Component
public class TimeControlWindow extends GameWindow {
    @PostConstruct
    protected void init() {
        setTitle("Time");
        setClosable(false);
        setMaximizable(false);
        
        initLayout();
    }
    
    private void initLayout() {
        getContentPane().setLayout(new FlowLayout());
        getContentPane().add(new JLabel("0:00:00"));
        getContentPane().add(new JButton("||"));
        getContentPane().add(new JButton(">"));
        getContentPane().add(new JButton(">>"));
        setSize(300, 70);
    }
}
