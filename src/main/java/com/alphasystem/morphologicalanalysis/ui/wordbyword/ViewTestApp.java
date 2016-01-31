package com.alphasystem.morphologicalanalysis.ui.wordbyword;

import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.NounPropertiesView;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.ParticlePropertiesView;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.ProNounPropertiesView;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.VerbPropertiesView;
import com.alphasystem.morphologicalanalysis.wordbyword.model.NounProperties;
import com.alphasystem.morphologicalanalysis.wordbyword.model.ParticleProperties;
import com.alphasystem.morphologicalanalysis.wordbyword.model.ProNounProperties;
import com.alphasystem.morphologicalanalysis.wordbyword.model.VerbProperties;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * @author sali
 */
public class ViewTestApp extends Application {

    static {
        //SpringContextHelper.getInstance();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("View Test");
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());

        GridPane gridPane = new GridPane();
        gridPane.setVgap(5);
        gridPane.setHgap(5);

        NounPropertiesView nounPropertiesView = new NounPropertiesView();
        nounPropertiesView.setLocationProperties(new NounProperties());
        gridPane.add(new TitledPane("Noun Properties", nounPropertiesView), 0, 0);

        ProNounPropertiesView proNounPropertiesView = new ProNounPropertiesView();
        proNounPropertiesView.setLocationProperties(new ProNounProperties());
        gridPane.add(new TitledPane("ProNoun Properties", proNounPropertiesView), 1, 0);

        VerbPropertiesView verbPropertiesView = new VerbPropertiesView();
        verbPropertiesView.setLocationProperties(new VerbProperties());
        gridPane.add(new TitledPane("Verb Properties", verbPropertiesView), 0, 1);

        ParticlePropertiesView particlePropertiesView = new ParticlePropertiesView();
        particlePropertiesView.setLocationProperties(new ParticleProperties());
        gridPane.add(new TitledPane("Particle Properties", particlePropertiesView), 1, 1);

        BorderPane borderPane = new BorderPane();
        Scene scene = new Scene(borderPane);
        scene.getStylesheets().addAll("/styles/glyphs_custom.css");
        primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
