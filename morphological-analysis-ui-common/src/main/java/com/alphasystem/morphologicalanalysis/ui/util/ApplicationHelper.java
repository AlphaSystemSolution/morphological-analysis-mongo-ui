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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author sali
 */
public final class ApplicationHelper {

    public static final MorphologicalAnalysisPreferences PREFERENCES = GenericPreferences.getInstance(MorphologicalAnalysisPreferences.class);
    public static final Border BORDER = new Border(new BorderStroke(Color.LIGHTGREY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.THIN));
    private static final String MAWRID_READER_URL = "http://ejtaal.net/";
    private static final String MAWRID_READER_URL_SUFFIX = "aa/index.html#bwq=";
    public static final String STYLE_SHEET_PATH = AppUtil.getResource("styles/application.css").toExternalForm();
    public static final double ROW_SIZE = 40.0;
    private static String mawridReaderUrl = "";

    static {
        String urlPrefix = MAWRID_READER_URL;
        File file = new File(AppUtil.USER_HOME_DIR, "Mawrid_Reader");
        if (file.exists()) {
            try {
                urlPrefix = file.toURI().toURL().toExternalForm();
            } catch (MalformedURLException e) {
                // ignore
            }
        }
        mawridReaderUrl = String.format("%s%s", urlPrefix, MAWRID_READER_URL_SUFFIX);
    }

    public static double calculateTableHeight(int size) {
        return roundTo100(((size + 7) * ROW_SIZE) + ROW_SIZE) + 100;
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

    public static String getMawridReaderUrl(String query) {
        return String.format("%s%s", mawridReaderUrl, query);
    }

    private static double roundTo100(double srcValue) {
        return (double) ((((int) srcValue) + 99) / 100) * 100;
    }
}
