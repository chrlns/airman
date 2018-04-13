/*
 *  Airline Manager
 *  
 *  Copyright (C) 2010-2018 by Christian Lins <christian@lins.me>
 *  All rights reserved.
 */
package me.lins.airman.io;

import java.awt.Image;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Cache working on volatile memory.
 * 
 * @author Christian Lins
 */
class MemoryTileCache implements TileCache {

    private final int       cacheSize = 54;
    private final Hashtable tiles     = new Hashtable(cacheSize);
    private Vector          keys      = new Vector(cacheSize);
    private final TileCache successor;

    public MemoryTileCache(TileCache successor) {
        this.successor = successor;
    }

    private void addToMemoryCache(String url, Image img) {
        if (img != null && url != null) {
            tiles.put(url, img);
            keys.addElement(url);
            trimCache();
        }
    }

    void freeCache() {
        tiles.clear();
        keys = new Vector(cacheSize);
    }

    private void trimCache() {
        while (tiles.size() >= cacheSize) {
            tiles.remove(keys.firstElement());
            keys.removeElementAt(0);
        }
    }

    public boolean initialize() {
        this.successor.initialize();
        return true;
    }

    /**
     * @return Always true as the Memory Cache is always enabled.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Image loadImage(int zoom, int x, int y, int mapSource, boolean goDown, Vector obs) {
        // Hopefully efficiently build the key string
        StringBuilder str = new StringBuilder();
        str.append(mapSource);
        str.append('/');
        str.append(zoom);
        str.append('/');
        str.append(x);
        str.append('/');
        str.append(y);
        String key = str.toString();

        if (tiles.containsKey(key)) {
            return (Image) tiles.get(key);
        } else if (goDown) {
            Image img = this.successor.loadImage(zoom, x, y, mapSource, goDown, obs);
            if (img != null) {
                addToMemoryCache(key, img);
            }
            return img;
        } else {
            return null;
        }
    }

    public void shutdown() {
        this.successor.shutdown();
    }

}
