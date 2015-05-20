package com.alphasystem.morphologicalanalysis.treebank.ui.util;

import org.apache.batik.svggen.SVGGraphics2D;

import java.awt.*;
import java.awt.geom.Path2D;

import static com.alphasystem.ui.UIUtilities.hex2Rgb;

/**
 * @author sali
 */
public final class SVGHelper {

    public static final Color COLOR_GRAY_CLOUD = hex2Rgb("#B6B6B4");

    public static void drawGridLines(int width, int height, SVGGraphics2D g2d,
                                     boolean drawGridLines){
        drawGridLines(width, height, 20, g2d, drawGridLines);
    }

    public static void drawGridLines(int width, int height, int step, SVGGraphics2D g2d,
                                     boolean drawGridLines){
        g2d.setColor(COLOR_GRAY_CLOUD);
        g2d.setStroke(new BasicStroke(0.5F));

        Path2D.Double path = new Path2D.Double();

        // draw outlines first
        int x1 = 0;
        int y1 = 0;
        int x2 = 0;
        int y2 = height;
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);

        x1 = 0;
        y1 = 0;
        x2 = width;
        y2 = 0;
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);

        x1 = 0;
        y1 = height;
        x2 = width;
        y2 = height;
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);

        x1 = width;
        y1 = 0;
        x2 = width;
        y2 = height;
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);

        if(drawGridLines){
            // horizontal lines
            x1 = 0;
            x2 = width;
            for (int i = step; i < height; i += step) {
                y1 = i;
                y2 = i;
                path.moveTo(x1, y1);
                path.lineTo(x2, y2);
                //g2d.draw(new Line2D.Double(x1, x2, y1, y2));
            }

            // vertical lines
            y1 = 0;
            y2 = height;
            for (int i = step; i < width; i += step) {
                x1 = i;
                x2 = i;
                path.moveTo(x1, y1);
                path.lineTo(x2, y2);
                //g2d.draw(new Line2D.Double(x1, x2, y1, y2));
            }
        }

        g2d.draw(path);
    }

}
