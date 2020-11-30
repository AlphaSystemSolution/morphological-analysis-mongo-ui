package com.alphasystem.morphologicalanalysis.ui.wordbyword.control;

import com.alphasystem.morphologicalanalysis.ui.control.ChapterVerseSelectionPane;
import javafx.scene.layout.BorderPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author sali
 */
@Component
public class WordByWordPane extends BorderPane {

    @Autowired private ChapterVerseSelectionPane chapterVerseSelectionPane;
    @Autowired private TokenView tokenView;

    public WordByWordPane() {
    }

    @PostConstruct
    public void postConstruct() {
        chapterVerseSelectionPane.selectedVerseProperty().addListener((observable, oldValue, newValue) ->
                tokenView.setVerseTokenPairGroup(newValue));
        BorderPane topPane = new BorderPane();
        topPane.setCenter(chapterVerseSelectionPane);

        setTop(topPane);
        setCenter(tokenView);
    }
}
