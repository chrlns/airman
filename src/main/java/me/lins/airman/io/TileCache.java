/*
 *  Airline Manager
 *  
 *  Copyright (C) 2010-2018 by Christian Lins <christian@lins.me>
 *  All rights reserved.
 */
package me.lins.airman.io;

import java.awt.Image;
import java.util.List;
import java.util.Vector;

/**
 * Interface representing a TileCache.
 * 
 * @author Christian Lins
 */
public interface TileCache {

    public static final int SOURCE_OPENSTREETMAP = 1;
    public static final int SOURCE_OPENCYCLEMAP  = 2;

    boolean initialize();

    boolean isEnabled();

    /**
     * Loads the tile identified through the given parameter. If obs is null the
     * image is loaded synchronously.
     * 
     * @param zoom
     * @param x
     * @param y
     * @param mapSource
     * @param goDown
     * @param observer
     * @return 
     */
    Image loadImage(int zoom, int x, int y, int mapSource, boolean goDown);

    void shutdown();
}
