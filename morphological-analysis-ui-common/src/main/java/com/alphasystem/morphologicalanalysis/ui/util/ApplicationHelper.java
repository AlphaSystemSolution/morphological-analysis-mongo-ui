package com.alphasystem.morphologicalanalysis.ui.util;

import com.alphasystem.util.AppUtil;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

/**
 * @author sali
 */
public final class ApplicationHelper {

    public static final Border BORDER = new Border(new BorderStroke(Color.LIGHTGREY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.THIN));
    public static final String STYLE_SHEET_PATH = AppUtil.getResource("styles/application.css").toExternalForm();
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
