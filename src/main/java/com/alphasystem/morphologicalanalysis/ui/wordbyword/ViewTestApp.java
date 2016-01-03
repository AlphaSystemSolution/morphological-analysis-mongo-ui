package com.alphasystem.morphologicalanalysis.ui.wordbyword;

import com.alphasystem.arabic.model.ArabicLetterType;
import com.alphasystem.morphologicalanalysis.morphology.model.DictionaryNotes;
import com.alphasystem.morphologicalanalysis.morphology.model.RootLetters;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.DictionaryNotesView;
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

        DictionaryNotesView view = new DictionaryNotesView();
        DictionaryNotes dictionaryNotes = new DictionaryNotes();
        RootLetters rootLetters = new RootLetters(ArabicLetterType.NOON, ArabicLetterType.SAD, ArabicLetterType.RA);
        dictionaryNotes.setRootLetters(rootLetters);
        view.setDictionaryNotes(dictionaryNotes);
        vBox.getChildren().addAll(view);

        Scene scene = new Scene(vBox);
        primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
