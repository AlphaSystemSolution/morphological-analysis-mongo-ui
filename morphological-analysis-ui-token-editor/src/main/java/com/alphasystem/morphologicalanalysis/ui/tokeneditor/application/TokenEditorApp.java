package com.alphasystem.morphologicalanalysis.ui.tokeneditor.application;

import com.alphasystem.morphologicalanalysis.ui.application.AbstractJavaFxApplicationSupport;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.spring.TokenEditorConfiguration;
import javafx.application.Preloader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;

/**
 * @author sali
 */
@Lazy
@SpringBootApplication
@Import(TokenEditorConfiguration.class)
public class TokenEditorApp extends AbstractJavaFxApplicationSupport {

    @Value("${app.ui.title}") private String windowTitle;

    @Override
    public void start(Stage primaryStage) throws Exception {
        notifyPreloader(new Preloader.StateChangeNotification(Preloader.StateChangeNotification.Type.BEFORE_START));

        primaryStage.setTitle(windowTitle);
        primaryStage.setScene(new Scene(new Pane()));
        primaryStage.setResizable(true);
        primaryStage.centerOnScreen();
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launchApp(TokenEditorApp.class, args);
    }
}
