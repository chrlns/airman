
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
