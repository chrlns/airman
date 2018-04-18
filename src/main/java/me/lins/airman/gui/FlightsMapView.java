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
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
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
    private int zoom = 5;
    
    private Point lastMouseDragPoint;
    private float scrollPosX = 53.0f, scrollPosY = 8.0f;

    @PostConstruct
    protected void initLayout() {
        setSize(640, 480);
        
        // Add listener for mouse drag listeners
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 1) {
                    zoomIn(e.getX(), e.getY());
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                lastMouseDragPoint = e.getPoint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int dx = e.getPoint().x - lastMouseDragPoint.x;
                int dy = e.getPoint().y - lastMouseDragPoint.y;
                shiftPixel(dx, dy, true);
                lastMouseDragPoint = e.getPoint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                
            }
        });
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() < 0) {
                    zoomIn(e.getX(), e.getY());
                } else {
                    zoomOut(e.getX(), e.getY());
                }
            }
        });
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

            int numTilesX = getWidth() / 256;
            int numTilesY = getHeight() / 256;
            
            for (int x = tileNumbers[0] - numTilesX; x <= tileNumbers[0] + numTilesX; x++) {
                if (x < 0)
                    continue;
                for (int y = tileNumbers[1] - numTilesY; y <= tileNumbers[1] + numTilesY; y++) {
                    if (y < 0)
                        continue;
                    
                    int dox = (x - tileNumbers[0]) * 256;
                    int doy = (y - tileNumbers[1]) * 256;
                    drawImage(g, x, y, offX + dox, offY + doy);
                }
            }
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
            repaint(); // If there is a bug with the loading/saving into MemoryTileCache
            // this causes flickering
        }
    }

    public void zoomIn(int x, int y) {
        int dx = getWidth() / 2 - x;
        int dy = getHeight() / 2 - y;

        if (zoom < 18) {
            shiftPixel(dx, dy, false);

            zoom++;
            centerTileNumbers = Math2.tileNumbers(scrollPosX, scrollPosY, zoom);
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
            centerTileNumbers = Math2.tileNumbers(scrollPosX, scrollPosY, zoom);
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
     * @param repaint
     */
    protected void shiftPixel(int dx, int dy, boolean repaint) {
        if (dx == 0 && dy == 0) {
            return;
        }

        float[] rpp = Math2.radPerPixel(zoom);
        scrollPosX += rpp[0] * -dx;
        scrollPosY += rpp[1] * dy;
        centerTileNumbers = Math2.tileNumbers(scrollPosX, scrollPosY, zoom);
        if (repaint) {
            repaint();
        }
    }
}
