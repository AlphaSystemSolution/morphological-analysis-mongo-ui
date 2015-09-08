package com.alphasystem.morphologicalanalysis.ui.dependencygraph.util;

import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import static javafx.scene.paint.Color.RED;
import static javafx.scene.paint.Color.TRANSPARENT;

/**
 * @author sali
 */
public final class DependencyGraphGraphicTool {

    public static final Color DARK_GRAY_CLOUD = Color.web("#B6B6B4");
    public static final String GRID_LINES = "gridLines";

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
        gridLines.setId(GRID_LINES);
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
    public Line drawDashedLine(String id, double x1, double y1, double x2, double y2,
                               Color strokeColor, double strokeWidth) {
        Line line = new Line();
        line.getStrokeDashArray().add(2d);
        line.setStartX(x1);
        line.setStartY(y1);
        line.setEndX(x2);
        line.setEndY(y2);
        line.setId(id);
        line.setStroke(strokeColor);
        line.setStrokeWidth(strokeWidth);
        line.setFill(strokeColor);
        return line;
    }

    /**
     * @param id
     * @param value
     * @param alignment
     * @param fillColor
     * @param x
     * @param y
     * @param font
     * @return
     */
    public Text drawText(String id, String value, TextAlignment alignment, Color fillColor,
                         double x, double y, Font font) {
        Text text = new Text();
        text.setId(id);
        text.setText(value);
        text.setTextAlignment(alignment);
        text.setFill(fillColor);
        if (x > 0) {
            text.setX(x);
        }
        if (y > 0) {
            text.setY(y);
        }
        text.setFont(font);
        return text;
    }

    /**
     * @param id
     * @param color
     * @param cx
     * @param cy
     * @param r
     * @return
     */
    public Circle drawCircle(String id, Color color, double cx, double cy, double r) {
        Circle circle = new Circle();
        circle.setId(id);
        circle.setStroke(color);
        circle.setFill(color);
        circle.setCenterX(cx);
        circle.setCenterY(cy);
        circle.setRadius(r);
        return circle;
    }

    /**
     * @param id
     * @param startX
     * @param startY
     * @param controlX1
     * @param controlY1
     * @param controlX2
     * @param controlY2
     * @param endX
     * @param endY
     * @param color
     * @return
     */
    public CubicCurve drawCubicCurve(String id, double startX, double startY, double controlX1,
                                     double controlY1, double controlX2, double controlY2,
                                     double endX, double endY, Color color) {
        CubicCurve cubicCurve = new CubicCurve(startX, startY, controlX1, controlY1,
                controlX2, controlY2, endX, endY);
        cubicCurve.setId(id);
        cubicCurve.setStroke(color);
        cubicCurve.setStrokeWidth(1.0);
        cubicCurve.setFill(TRANSPARENT);
        return cubicCurve;
    }

    /**
     *
     * @param color
     * @param points
     * @return
     */
    public Polyline drawPolyline(Color color, double... points) {
        Polyline polyline = new Polyline(points);
        polyline.setStroke(color);
        polyline.setStrokeWidth(1.0);
        polyline.setFill(color);

        return polyline;
    }

    /**
     * @param startY
     * @param controlX1
     * @param controlY1
     * @param controlX2
     * @param controlY2
     * @param endX
     * @param endY
     * @return
     */
    public Path drawCubicCurveBounds(double startX, double startY, double controlX1, double controlY1,
                                     double controlX2, double controlY2, double endX, double endY) {
        Path path = new Path(new MoveTo(startX, startY), new LineTo(controlX1, controlY1),
                new LineTo(controlX2, controlY2), new LineTo(endX, endY));

        path.setStroke(RED);
        path.setStrokeWidth(0.50);
        path.getStrokeDashArray().add(2d);

        return path;
    }

    /**
     * @param shape
     * @return
     */
    public Rectangle drawBounds(Shape shape) {
        Bounds bounds = shape.getBoundsInLocal();
        Rectangle rectangle = new Rectangle(bounds.getMinX(), bounds.getMinY(),
                bounds.getWidth(), bounds.getHeight());
        rectangle.setFill(TRANSPARENT);
        rectangle.setStroke(RED);
        rectangle.setStrokeWidth(0.5);
        rectangle.getStrokeDashArray().add(2d);
        return rectangle;
    }
}
