/*
 *  Airline Manager
 *  
 *  Copyright (C) 2010-2018 by Christian Lins <christian@lins.me>
 *  All rights reserved.
 */
package me.lins.airman.io;

import java.awt.Image;
import java.util.HashMap;
import java.util.Random;

/**
 * Cache working on volatile memory.
 * 
 * @author Christian Lins
 */
class MemoryTileCache implements TileCache {

    private final int       cacheSize = 256;
    private final HashMap<String, Image> tiles     = new HashMap<>(cacheSize);
    private final TileCache successor;

    public MemoryTileCache(TileCache successor) {
        this.successor = successor;
    }

    private void addToMemoryCache(String url, Image img) {
        if (img != null && url != null) {
            tiles.put(url, img);
            if (tiles.size() > cacheSize)
                trimCache();
        }
    }

    void freeCache() {
        tiles.clear();
    }

    private void trimCache() {
        Random rnd = new Random();
        tiles.entrySet().stream()
                .filter((e) -> (rnd.nextBoolean()))
                .forEach(tiles.entrySet()::remove);
    }

    @Override
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
    public Image loadImage(int zoom, int x, int y, int mapSource, boolean goDown) {
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
            System.out.println("Memory cache hit: " + key);
            return tiles.get(key);
        } else if (goDown) {
            Image img = this.successor.loadImage(zoom, x, y, mapSource, goDown);
            if (img != null) {
                addToMemoryCache(key, img);
            }
            return img;
        } else {
            return null;
        }
    }

    @Override
    public void shutdown() {
        this.successor.shutdown();
    }

}
