/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 *  Airline Manager
 *  
 *  Copyright (C) 2010-2018 by Christian Lins <christian@lins.me>
 *  All rights reserved.
 */
package me.lins.airman.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.swing.JComponent;
import me.lins.airman.io.TileCacheManager;
import me.lins.airman.io.TileLoadingObserver;
import me.lins.airman.util.Math2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author chris
 */
@Component
public class FlightsMapView extends JComponent implements TileLoadingObserver {
    @Autowired
    private TileCacheManager tcm;
    
    private int[] centerTileNumbers = new int[]{1,1,1,1};
    private Image loadingImg;
    private int zoom = 2;

    @PostConstruct
    protected void initLayout() {
        setSize(640, 480);
    }
    
    /**
     * Draws a tile image.
     * 
     * @param g
     * @param x
     * @param y
     * @param offX
     * @param offY
     * @throws IOException
     */
    private void drawImage(Graphics g, int x, int y, int offX, int offY) throws IOException {
        Image img = tcm.loadImage(zoom, x, y, 1 /* mapSource */, null);
        if (img != null) {
            g.drawImage(img, offX, offY, null);
        } else {
            // Draw placeholder image
            g.drawImage(loadingImg, offX, offY, null);

            tcm.loadImage(zoom, x, y, 1 /* mapSource */, this);
        }
    }

    /**
     * Draw the map.
     * @param g
     */
    @Override
    public void paintComponent(Graphics g) {
        g.setColor(new Color(255, 255, 255));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        try {
            int[] tileNumbers = centerTileNumbers;
            int offX = -tileNumbers[2] + getWidth() / 2; // We want to transform
                                                         // the origin to the
                                                         // center
            int offY = -tileNumbers[3] + getHeight() / 2; // of the screen

            System.out.println(offX + " " + offY);

            // Draw center image
            drawImage(g, tileNumbers[0], tileNumbers[1], offX, offY);

            // Draw image above
            drawImage(g, tileNumbers[0], tileNumbers[1] - 1, offX, offY - 256);

            // Draw image below
            drawImage(g, tileNumbers[0], tileNumbers[1] + 1, offX, offY + 256);

            // Draw left center image
            drawImage(g, tileNumbers[0] - 1, tileNumbers[1], offX - 256, offY);

            // Draw left image above
            drawImage(g, tileNumbers[0] - 1, tileNumbers[1] - 1, offX - 256, offY - 256);

            // Draw left image below
            drawImage(g, tileNumbers[0] - 1, tileNumbers[1] + 1, offX - 256, offY + 256);

            // Draw right center image
            drawImage(g, tileNumbers[0] + 1, tileNumbers[1], offX + 256, offY);

            // Draw right image above
            drawImage(g, tileNumbers[0] + 1, tileNumbers[1] - 1, offX + 256, offY - 256);

            // Draw right image below
            drawImage(g, tileNumbers[0] + 1, tileNumbers[1] + 1, offX + 256, offY + 256);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private int[] posOnScreen(float lon, float lat) {
        int[] tileNumbers = centerTileNumbers;
        int offX = -tileNumbers[2] + getWidth() / 2; // We want to transform the
                                                     // origin to the center
        int offY = -tileNumbers[3] + getHeight() / 2; // of the screen
        int[] pos = Math2.tileNumbers(lon, lat, zoom);
        int x = (pos[0] - tileNumbers[0]) * 256 + (pos[2] + offX);
        int y = (pos[1] - tileNumbers[1]) * 256 + (pos[3] + offY);
        return new int[] { x, y };
    }

    @Override
    public void tileLoaded(Image img, int zoom, int x, int y, int mapSource, byte[] raw) {
        if (img != null) {
            repaint();
        }
    }

    public void zoomIn(int x, int y) {
        int dx = getWidth() / 2 - x;
        int dy = getHeight() / 2 - y;

        if (zoom < 18) {
            shiftPixel(dx, dy, false);

            zoom++;
            //centerTileNumbers = Math2.tileNumbers(scrollPos.getX(), scrollPos.getY(), zoom);
            repaint();
        } else if (dx != 0 || dy != 0) {
            shiftPixel(dx, dy);
        }
    }

    public void zoomOut(int x, int y) {
        int dx = getWidth() / 2 - x;
        int dy = getHeight() / 2 - y;

        if (zoom > 1) {
            shiftPixel(dx, dy, false);

            zoom--;
            //centerTileNumbers = Math2.tileNumbers(scrollPos.getX(), scrollPos.getY(), zoom);
            repaint();
        } else if (dx != 0 || dy != 0) {
            shiftPixel(dx, dy);
        }
    }

    public void shiftPixel(int dx, int dy) {
        shiftPixel(dx, dy, true);
    }

    /**
     * Shift scroll position with given pixel values.
     * 
     * @param dx
     * @param dy
     */
    protected void shiftPixel(int dx, int dy, boolean repaint) {
        if (dx == 0 && dy == 0) {
            return;
        }

        /*float[] rpp = Math2.radPerPixel(zoom);
        this.scrollPos.shift(rpp[0] * -dx, rpp[1] * dy);
        centerTileNumbers = Math2.tileNumbers(scrollPos.getX(), scrollPos.getY(), zoom);
        if (repaint) {
            repaint();
        }*/
    }
}
