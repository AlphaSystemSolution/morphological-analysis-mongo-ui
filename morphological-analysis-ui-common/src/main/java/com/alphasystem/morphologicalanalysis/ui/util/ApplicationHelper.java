package com.alphasystem.morphologicalanalysis.ui.util;

import com.alphasystem.util.AppUtil;
import com.alphasystem.util.GenericPreferences;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Control;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author sali
 */
public final class ApplicationHelper {

    public static final MorphologicalAnalysisPreferences PREFERENCES = GenericPreferences.getInstance(MorphologicalAnalysisPreferences.class);
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

    public static <T extends Control, P extends Pane> void loadFxml(T control, P pane) throws IOException, URISyntaxException {
        final String resourcePrefix = control.getClass().getSimpleName();
        final URL url = AppUtil.getPath(String.format("fxml/%s.fxml", resourcePrefix)).toUri().toURL();
        final ResourceBundle resourceBundle = ResourceBundle.getBundle(String.format("fxml.%s", resourcePrefix));
        FXMLLoader fxmlLoader = new FXMLLoader(url, resourceBundle);
        fxmlLoader.setControllerFactory(ApplicationContextProvider::getBean);
        fxmlLoader.setRoot(pane);
        fxmlLoader.load();
    }

    private static double roundTo100(double srcValue) {
        return (double) ((((int) srcValue) + 99) / 100) * 100;
    }
}
