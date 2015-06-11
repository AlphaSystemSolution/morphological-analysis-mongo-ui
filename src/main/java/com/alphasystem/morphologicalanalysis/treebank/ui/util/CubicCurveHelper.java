package com.alphasystem.morphologicalanalysis.treebank.ui.util;

import javafx.geometry.Point2D;

import static java.lang.Math.pow;

/**
 * @author sali
 */
public final class CubicCurveHelper {

    /**
     * Do not let any one instantiate this class.
     */
    private CubicCurveHelper() {
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

    /**
     * @param t
     * @param startX
     * @param startY
     * @param controlX1
     * @param controlY1
     * @param controlX2
     * @param controlY2
     * @param endX
     * @param endY
     * @return
     */
    public static Point2D calculateCurvePoint(double t, double startX, double startY, double controlX1,
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
}
