/*
 *  Airline Manager
 *  
 *  Copyright (C) 2010-2018 by Christian Lins <christian@lins.me>
 *  All rights reserved.
 */
package me.lins.airman.io;

import java.awt.Image;

/**
 * 
 * @author Christian Lins
 */
public interface TileLoadingObserver {

    /**
     * Called after the image is completely loaded.
     * 
     * @param img
     * @param zoom
     * @param x
     * @param y
     * @param mapSource
     */
    void tileLoaded(Image img, int zoom, int x, int y, int mapSource, byte[] raw);

}
