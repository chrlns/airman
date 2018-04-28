/*
 *  Airline Manager
 *  
 *  Copyright (C) 2010-2018 by Christian Lins <christian@lins.me>
 *  All rights reserved.
 */
package me.lins.airman.io;

import java.awt.Image;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;


/**
 * Controls the caching. This class has only static methods for performance
 * reasons.
 * 
 * @author Christian Lins
 */
@Component
public class TileCacheManager {

    private MemoryTileCache memoryTileCache;
    private TileLoader      loader;

    public void clearVolatileCache() {
        memoryTileCache.freeCache();
    }

    @PostConstruct
    public void initialize() {
        memoryTileCache = new MemoryTileCache(
                new JarTileCache(
                        new FileTileCache(
                                new OnlineFileSource())));
        memoryTileCache.initialize();
        loader = new TileLoader();
        loader.start();
    }

    public Image loadImage(int zoom, int x, int y, int mapSource, TileLoadingObserver obs) {
        Image img = memoryTileCache.loadImage(zoom, x, y, mapSource, false);
        if (img == null && obs != null) {
            TileLoadingTask task = new TileLoadingTask(zoom, x, y, mapSource, memoryTileCache, obs);
            loader.addTask(task);
            return null;
        } else {
            return img;
        }
    }

    public void shutdown() {
        try {
            loader.interrupt();
            loader = null;
            memoryTileCache.shutdown();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
