/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.lins.airman.io;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author chris
 */
public class FileTileCache implements TileCache {
    private final TileCache successor;
    private String cacheBase = "./tilecache/";
    
    public FileTileCache(TileCache successor) {
        this.successor = successor;
    }
    
    @Override
    public boolean initialize() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    protected void storeToCache(String path, Image img) {
        if (img != null) {
            if (img instanceof RenderedImage) {
                try {
                    File pathFile = new File(path);
                    pathFile.mkdirs();
                    ImageIO.write((RenderedImage)img, "PNG", pathFile);
                } catch (IOException ex) {
                    Logger.getLogger(FileTileCache.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                BufferedImage buf = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
                buf.createGraphics().drawImage(img, 0, 0, null);
                storeToCache(path, buf);
            }
        }
    }
    
    @Override
    public Image loadImage(int zoom, int x, int y, int mapSource, boolean goDown) {
        String dir = cacheBase + mapSource + "/" + zoom + "/" + x + "/" + y + ".png";
        File dirFile = new File(dir);
        if (dirFile.exists()) {
            try {
                // Cache hit
                return ImageIO.read(new FileInputStream(dirFile));
            } catch (IOException ex) {
                Logger.getLogger(FileTileCache.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        } else if (goDown) {
            Image img = successor.loadImage(zoom, x, y, mapSource, goDown);
            storeToCache(dir, img);
            return img;
        } else {
            return null;
        }
    }

    @Override
    public void shutdown() {

    }
    
}
