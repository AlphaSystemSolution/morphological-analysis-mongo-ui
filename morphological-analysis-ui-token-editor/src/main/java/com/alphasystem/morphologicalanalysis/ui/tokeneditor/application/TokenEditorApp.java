package com.alphasystem.morphologicalanalysis.ui.tokeneditor.application;

import com.alphasystem.morphologicalanalysis.ui.application.AbstractJavaFxApplicationSupport;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.TokenEditorPane;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.spring.TokenEditorConfiguration;
import javafx.application.Preloader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired private TokenEditorPane mainPane;

    @Override
    public void start(Stage primaryStage) throws Exception {
        notifyPreloader(new Preloader.StateChangeNotification(Preloader.StateChangeNotification.Type.BEFORE_START));

        primaryStage.setTitle(windowTitle);
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setResizable(true);
        primaryStage.centerOnScreen();
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launchApp(TokenEditorApp.class, args);
    }
}
