package com.alphasystem.morphologicalanalysis.ui.wordbyword;

import com.alphasystem.arabic.model.ArabicLetterType;
import com.alphasystem.arabic.model.NamedTemplate;
import com.alphasystem.morphologicalanalysis.morphology.model.MorphologicalEntry;
import com.alphasystem.morphologicalanalysis.morphology.model.RootLetters;
import com.alphasystem.morphologicalanalysis.morphology.model.support.VerbalNoun;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.MorphologicalEntryView;
import com.alphasystem.morphologicalanalysis.util.SpringContextHelper;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

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

        MorphologicalEntryView root = new MorphologicalEntryView();
        MorphologicalEntry morphologicalEntry = new MorphologicalEntry();
        morphologicalEntry.setRootLetters(new RootLetters(ArabicLetterType.SEEN, ArabicLetterType.LAM, ArabicLetterType.MEEM));
        morphologicalEntry.setForm(NamedTemplate.FORM_IV_TEMPLATE);
        morphologicalEntry.getVerbalNouns().add(VerbalNoun.VERBAL_NOUN_FORM_II);
        root.setMorphologicalEntry(morphologicalEntry);
        vBox.getChildren().addAll(root);

        Scene scene = new Scene(vBox);
        primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
