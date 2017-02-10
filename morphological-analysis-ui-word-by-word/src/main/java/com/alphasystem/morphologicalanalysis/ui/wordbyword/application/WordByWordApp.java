package com.alphasystem.morphologicalanalysis.ui.wordbyword.application;

import com.alphasystem.morphologicalanalysis.ui.application.AbstractJavaFxApplicationSupport;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.WordByWordPane;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.spring.MainConfiguration;
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
@Import({MainConfiguration.class})
public class WordByWordApp extends AbstractJavaFxApplicationSupport {

    @Value("${app.ui.title}")     private String windowTitle;
    @Autowired private WordByWordPane mainPane;

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
        launchApp(WordByWordApp.class, args);
    }
}
