/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.lins.airman.gui;

import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

/**
 *
 * @author chris
 */
@Component
public class DebugWindow extends GameWindow {
    @PostConstruct
    protected void initLayout() {
        setTitle("Debug");
        setSize(400, 400);
    }
}
