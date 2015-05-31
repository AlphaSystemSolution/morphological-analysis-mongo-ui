package com.alphasystem.morphologicalanalysis.treebank.jfx.ui.util;

import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import static java.lang.Math.pow;
import static javafx.scene.paint.Color.*;

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

    private static double calculateC(double x0, double x1) {
        return 3 * (x1 - x0);
    }

    private static double calculateB(double x1, double x2, double c) {
        return (3 * (x2 - x1)) - c;
    }

    private static double calculateA(double x0, double x3, double b, double c) {
        return x3 - x0 - c - b;
    }

    private static double calculateCoordinate(double t, double x, double a, double b, double c) {
        return (a * pow(t, 3)) + (b * pow(t, 2)) + (c * t) + x;
    }

    private static Point2D calculateCurvePoint(double t, double startX, double startY, double controlX1,
                                               double controlY1, double controlX2, double controlY2,
                                               double endX, double endY) {
        double cx = calculateC(startX, controlX1);
        double bx = calculateB(controlX1, controlX2, cx);
        double ax = calculateA(startX, endX, bx, cx);

        double cy = calculateC(startY, controlY1);
        double by = calculateB(controlY1, controlY2, cy);
        double ay = calculateA(startY, endY, by, cy);

        double x = calculateCoordinate(t, startX, ax, bx, cx);
        double y = calculateCoordinate(t, startY, ay, by, cy);
        return new Point2D(x, y);
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

    /**
     *
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
     *
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
                         double x, double y, Font font){
        Text text = new Text();
        text.setId(id);
        text.setText(value);
        text.setTextAlignment(alignment);
        text.setFill(fillColor);
        text.setX(x);
        text.setY(y);
        text.setFont(font);
        return text;
    }

    /**
     *
     * @param id
     * @param color
     * @param cx
     * @param cy
     * @param r
     * @return
     */
    public Circle drawCircle(String id, Color color, double cx, double cy, double r){
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
                                     double endX, double endY, Color color){
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
     * @param t1
     * @param t2
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
    public Polyline drawTriangleOnCubicCurve(double t1, double t2, double startX, double startY, double controlX1,
                                             double controlY1, double controlX2, double controlY2, double endX,
                                             double endY, Color color) {
        Point2D curvePoint1 = calculateCurvePoint(t1, startX, startY, controlX1, controlY1, controlX2,
                controlY2, endX, endY);
        Point2D curvePoint2 = calculateCurvePoint(t2, startX, startY, controlX1, controlY1, controlX2,
                controlY2, endX, endY);
        Point2D point1 = new Point2D(curvePoint1.getX(), curvePoint1.getY() + 5);
        Point2D point2 = new Point2D(curvePoint1.getX(), curvePoint1.getY() - 5);

        Polyline polyline = new Polyline(curvePoint2.getX(), curvePoint2.getY(), point1.getX(), point1.getY(),
                point2.getX(), point2.getY(), curvePoint2.getX(), curvePoint2.getY());
        polyline.setStroke(BLACK);
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
}
