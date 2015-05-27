package com.alphasystem.morphologicalanalysis.treebank.jfx.ui;

import com.alphasystem.morphologicalanalysis.util.SpringContextHelper;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * @author sali
 */
public class TreeBankApp extends Application {

    static {
        SpringContextHelper.getInstance();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Quran Morphological Tree Bank");
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());

        Scene scene = new Scene(new TreeBankPane());
        Global.getInstance().setGlobalScene(scene);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
