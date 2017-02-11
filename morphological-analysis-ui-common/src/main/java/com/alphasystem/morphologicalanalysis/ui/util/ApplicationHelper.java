package com.alphasystem.morphologicalanalysis.ui.util;

/**
 * @author sali
 */
public final class ApplicationHelper {

    public static final double ROW_SIZE = 55.0;

    private static final double DEFAULT_MIN_HEIGHT = 500.0;

    public static double calculateTableHeight(int size) {
        int numOfRows = Math.max(20, size);
        double height = (numOfRows * ROW_SIZE) + ROW_SIZE;
        height = roundTo100(height);
        return Math.max(height, DEFAULT_MIN_HEIGHT) + 100;
    }

    private static double roundTo100(double srcValue) {
        return (double) ((((int) srcValue) + 99) / 100) * 100;
    }
}
