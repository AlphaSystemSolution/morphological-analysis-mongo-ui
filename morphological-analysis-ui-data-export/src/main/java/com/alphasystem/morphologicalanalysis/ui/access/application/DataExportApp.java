package com.alphasystem.morphologicalanalysis.ui.access.application;

import com.alphasystem.morphologicalanalysis.ui.access.control.DataExportView;
import com.alphasystem.morphologicalanalysis.ui.access.spring.MainConfiguration;
import com.alphasystem.morphologicalanalysis.ui.application.AbstractJavaFxApplicationSupport;
import com.alphasystem.morphologicalanalysis.ui.spring.support.CommonConfiguration;
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
@Import({CommonConfiguration.class, MainConfiguration.class})
public class DataExportApp extends AbstractJavaFxApplicationSupport {

    @Value("${app.ui.title}") private String windowTitle;
    @Autowired private DataExportView view;

    public static void main(String[] args) {
        launchApp(DataExportApp.class, args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        notifyPreloader(new Preloader.StateChangeNotification(Preloader.StateChangeNotification.Type.BEFORE_START));

        primaryStage.setTitle(windowTitle);
        primaryStage.setScene(new Scene(view));
        primaryStage.setResizable(true);
        primaryStage.centerOnScreen();
        primaryStage.setMaximized(true);
        primaryStage.show();
    }
}
