package com.alphasystem.morphologicalanalysis.ui.wordbyword;

import com.alphasystem.app.sarfengine.conjugation.builder.ConjugationBuilder;
import com.alphasystem.app.sarfengine.conjugation.model.SarfChart;
import com.alphasystem.app.sarfengine.guice.GuiceSupport;
import com.alphasystem.arabic.model.NamedTemplate;
import com.alphasystem.arabic.ui.Browser;
import com.alphasystem.morphologicalanalysis.util.SarChartBuilder;
import com.alphasystem.morphologicalanalysis.util.SpringContextHelper;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;

import static com.alphasystem.arabic.model.ArabicLetterType.*;
import static com.alphasystem.morphologicalanalysis.morphology.model.support.NounOfPlaceAndTime.NOUN_OF_PLACE_AND_TIME_FORM_IV;
import static com.alphasystem.morphologicalanalysis.morphology.model.support.VerbalNoun.VERBAL_NOUN_FORM_IV;
import static java.util.Collections.singletonList;

/**
 * @author sali
 */
public class ViewTestApp extends Application {

    static {
        SpringContextHelper.getInstance();
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

        VBox vBox = new VBox();
        vBox.setSpacing(10);

        ConjugationBuilder conjugationBuilder = GuiceSupport.getInstance().getConjugationBuilderFactory()
                .getConjugationBuilder();
        SarfChart sarfChart = conjugationBuilder.doConjugation(NamedTemplate.FORM_IV_TEMPLATE, "To submit",
                false, false, SEEN, LAM, MEEM, singletonList(VERBAL_NOUN_FORM_IV),
                singletonList(NOUN_OF_PLACE_AND_TIME_FORM_IV));
        File file = SarChartBuilder.createChart(sarfChart);
        Browser browser = new Browser();
        browser.loadUrl(file.toURI().toURL().toString());
        vBox.getChildren().addAll(browser);

        Scene scene = new Scene(vBox);
        primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
