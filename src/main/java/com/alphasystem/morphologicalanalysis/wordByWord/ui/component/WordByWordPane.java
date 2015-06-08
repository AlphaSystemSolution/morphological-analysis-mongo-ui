package com.alphasystem.morphologicalanalysis.wordByWord.ui.component;

import com.alphasystem.morphologicalanalysis.ui.common.ChapterVerseSelectionPane;
import com.alphasystem.morphologicalanalysis.ui.common.model.VerseAdapter;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;

/**
 * @author sali
 */
public class WordByWordPane extends BorderPane {

    private ChapterVerseSelectionPane chapterVerseSelectionPane;

    public WordByWordPane() {

        chapterVerseSelectionPane = new ChapterVerseSelectionPane();
        chapterVerseSelectionPane.avaialbleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                initPane();
            }
        });

    }

    private void initPane() {
        // top pane for Menu bar and chapter verse selection
        BorderPane topPane = new BorderPane();
        Pane left = new Pane();
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        double width = bounds.getWidth();
        left.setPrefWidth(width / 3);

        topPane.setLeft(left);
        topPane.setCenter(chapterVerseSelectionPane);
        chapterVerseSelectionPane.selectedVerseProperty().addListener((observable, oldValue, newValue) -> {
            refreshTable();
        });
        setTop(topPane);

        refreshTable();
    }

    private void refreshTable() {
        VerseAdapter selectedVerse = chapterVerseSelectionPane.getSelectedVerse();
        if (selectedVerse == null) {
            return;
        }
        System.out.println(selectedVerse.getChapterNumber() + " : " + selectedVerse.getVerseNumber());
    }
}
