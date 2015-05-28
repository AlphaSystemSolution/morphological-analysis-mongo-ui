package com.alphasystem.morphologicalanalysis.treebank.jfx.ui.util;

import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

/**
 * @author sali
 */
public final class DependencyGraphGraphicTool {

    public static final Color DARK_GRAY_CLOUD = Color.web("#B6B6B4");

    private static DependencyGraphGraphicTool instance;

    private DependencyGraphGraphicTool() {
    }

    public static synchronized DependencyGraphGraphicTool getInstance() {
        if (instance == null) {
            instance = new DependencyGraphGraphicTool();
        }
        return instance;
    }

    /**
     * Draw grid lines with given <code>width</code>, <code>height</code> and default step of "20". This method will
     * always draw the out line of the box, If the given parameter <code>drawOutlinesOnly</code> is true then
     * only outer outlines will be drawn otherwise entire grid lines will be drawn.
     *
     * @param showGridLines boolean to indicate whether to draw outlines only
     * @param width         width of the canvas
     * @param height        height of the canvas
     */
    public Path drawGridLines(boolean showGridLines, int width, int height) {
        return drawGridLines(showGridLines, width, height, 20);
    }

    /**
     * Draw grid lines with given <code>width</code>, <code>height</code>, and <code>step</code>. This method will
     * always draw the out line of the box, If the given parameter <code>drawOutlinesOnly</code> is true then
     * only outer outlines will be drawn otherwise entire grid lines will be drawn.
     *
     * @param showGridLines boolean to indicate whether to draw outlines only
     * @param width         width of the canvas
     * @param height        height of the canvas
     * @param step          gap between lines
     */
    public Path drawGridLines(boolean showGridLines, int width, int height, int step) {
        Path gridLines = new Path();
        gridLines.setId("gridLines");
        gridLines.setStroke(DARK_GRAY_CLOUD);
        gridLines.setStrokeWidth(0.5);

        ObservableList<PathElement> elements = gridLines.getElements();

        // start drawing outlines first
        int x1 = 0;
        int y1 = 0;
        int x2 = 0;
        int y2 = height;
        elements.add(new MoveTo(x1, y1));
        elements.add(new LineTo(x2, y2));

        x1 = 0;
        y1 = 0;
        x2 = width;
        y2 = 0;
        elements.add(new MoveTo(x1, y1));
        elements.add(new LineTo(x2, y2));

        x1 = 0;
        y1 = height;
        x2 = width;
        y2 = height;
        elements.add(new MoveTo(x1, y1));
        elements.add(new LineTo(x2, y2));

        x1 = width;
        y1 = 0;
        x2 = width;
        y2 = height;
        elements.add(new MoveTo(x1, y1));
        elements.add(new LineTo(x2, y2));

        if (showGridLines) {
            // if we are to draw entire grid lines as well

            // horizontal lines
            x1 = 0;
            x2 = width;
            for (int i = step; i < height; i += step) {
                y1 = i;
                y2 = i;
                elements.add(new MoveTo(x1, y1));
                elements.add(new LineTo(x2, y2));
            }

            // vertical lines
            y1 = 0;
            y2 = height;
            for (int i = step; i < width; i += step) {
                x1 = i;
                x2 = i;
                elements.add(new MoveTo(x1, y1));
                elements.add(new LineTo(x2, y2));
            }
        }

        return gridLines;
    }

    /**
     * @param id
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param strokeColor
     * @param strokeWidth
     * @return
     */
    public Line drawLine(String id, double x1, double y1, double x2, double y2,
                         Color strokeColor, double strokeWidth) {
        Line line = new Line();
        line.setStartX(x1);
        line.setStartY(y1);
        line.setEndX(x2);
        line.setEndY(y2);
        line.setId(id);
        line.setStroke(strokeColor);
        line.setStrokeWidth(strokeWidth);
        return line;
    }
}
