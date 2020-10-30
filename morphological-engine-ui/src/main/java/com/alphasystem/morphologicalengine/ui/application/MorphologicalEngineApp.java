package com.alphasystem.morphologicalengine.ui.application;

import com.alphasystem.app.morphologicalengine.docx.MorphologicalChartConfiguration;
import com.alphasystem.morphologicalanalysis.ui.application.AbstractJavaFxApplicationSupport;
import com.alphasystem.morphologicalengine.ui.control.MorphologicalEngineView;
import javafx.application.Preloader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author sali
 */
@Lazy
@SpringBootApplication
@Import({MorphologicalEngineUIConfiguration.class, MorphologicalChartConfiguration.class})
public class MorphologicalEngineApp extends AbstractJavaFxApplicationSupport {

    private static final String OPEN_ARG = "open";
    @Value("${app.ui.title}") private String windowTitle;
    @Autowired private MorphologicalEngineView morphologicalEngineView;

    @Override
    public void start(Stage primaryStage) throws Exception {
        notifyPreloader(new Preloader.StateChangeNotification(Preloader.StateChangeNotification.Type.BEFORE_START));

        primaryStage.setTitle(windowTitle);

        final Parameters parameters = getParameters();
        Map<String, String> namedParameters = (parameters == null) ? new HashMap<>() : parameters.getNamed();
        boolean noArg = namedParameters.isEmpty();
        if (noArg) {
            showStage(primaryStage, null);
        } else {
            Set<Map.Entry<String, String>> entries = namedParameters.entrySet();
            for (Map.Entry<String, String> parameter : entries) {
                String name = parameter.getKey();
                String value = parameter.getValue();
                if (isBlank(name) || isBlank(value)) {
                    continue;
                }
                if(OPEN_ARG.equals(name)){
                    open(primaryStage, value);
                }
            }
        }
    }

    private void showStage(Stage primaryStage, File file) {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());

        primaryStage.setWidth(bounds.getWidth() / 4);
        primaryStage.setHeight(bounds.getHeight() / 4);

        morphologicalEngineView.setFileProperty(file);
        Scene scene = new Scene(morphologicalEngineView);
        scene.getStylesheets().addAll("/styles/glyphs_custom.css");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setMaximized(true);

        // primaryStage.setOnShowing(event -> morphologicalEngineView.initDependencies(primaryStage));
    }

    private void open(Stage primaryStage, String fileName) {
        showStage(primaryStage, new File(fileName));
    }

    public static void main(String[] args) {
        launchApp(MorphologicalEngineApp.class, args);
    }
}
