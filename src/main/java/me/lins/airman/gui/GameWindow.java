/*
 *  Airline Manager
 *  
 *  Copyright (C) 2010-2018 by Christian Lins <christian@lins.me>
 *  All rights reserved.
 */
package me.lins.airman.gui;

import java.awt.Component;
import javax.swing.JInternalFrame;

/**
 *
 * @author chris
 */
public class GameWindow extends JInternalFrame {
    public GameWindow() {
        super("Unnamed", true, true, true, true);

        setSize(400, 300);
        setVisible(true);
    }
}
