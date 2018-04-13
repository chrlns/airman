/*
 *  Airline Manager
 *  
 *  Copyright (C) 2010-2018 by Christian Lins <christian@lins.me>
 *  All rights reserved.
 */
package me.lins.airman.io;

import java.awt.Image;
import java.util.Vector;
import javax.imageio.ImageIO;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;


/**
 * Load tiles from a tile source.
 * 
 * @author Christian Lins
 */
class OnlineFileSource implements TileCache {

    public static final String OSM_URL = "http://tile.openstreetmap.org/";

    public OnlineFileSource() {
    }

    @Override
    public boolean initialize() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Image loadImage(int zoom, int x, int y, int mapSource, boolean goDown, Vector obs) {
        if (y < 0) {
            return null;
        }

        String url = OSM_URL;
        url += zoom + "/" + x + "/" + y + ".png";

        try {
            String agent = "Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:57.0) Gecko/20100101 Firefox/57.0";
            Content content = Request.Get(url).userAgent(agent).execute().returnContent();
            Image img = ImageIO.read(content.asStream());

            // Notify observer
            if (obs != null) {
                for (int n = 0, os = obs.size(); n < os; n++) {
                    TileLoadingObserver observer = (TileLoadingObserver) obs.elementAt(n);
                    observer.tileLoaded(img, zoom, x, y, mapSource, content.asBytes());
                }
            }

            return img;
        } catch (Exception ex) {
            //midlet.getDebugDialog().addMessage("Excp", ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public void shutdown() {
    }

}
