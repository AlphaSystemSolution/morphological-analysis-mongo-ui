package com.alphasystem.morphologicalanalysis.ui.wordbyword;

import com.alphasystem.morphologicalanalysis.util.SpringContextHelper;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.alphasystem.morphologicalanalysis.ui.common.Global.DEFAULT_DICTIONARY_DIRECTORY;
import static com.alphasystem.util.nio.NIOFileUtils.copyDir;

/**
 * @author sali
 */
public class TokenEditorApp extends Application {

    static {
        if (!DEFAULT_DICTIONARY_DIRECTORY.exists()) {
            DEFAULT_DICTIONARY_DIRECTORY.mkdirs();

            try {
                copyDir(DEFAULT_DICTIONARY_DIRECTORY.toPath(), "asciidoctor", TokenEditorApp.class);
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }

        SpringContextHelper.getInstance();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Quranic Morphological Word By Word Token Editor");
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());

        Scene scene = new Scene(new TokenEditorPane());
        scene.getStylesheets().addAll("/styles/glyphs_custom.css");
        primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


}
