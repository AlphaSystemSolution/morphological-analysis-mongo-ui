package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control;

import com.alphasystem.morphologicalanalysis.ui.control.ChapterVerseSelectionPane;
import javafx.geometry.Pos;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author sali
 */
@Component
public class TokenEditorMainPane extends BorderPane {

    @Autowired private ChapterVerseSelectionPane chapterVerseSelectionPane;
    @Autowired private TokenListView tokenListView;

    @PostConstruct
    public void postConstruct() {
        chapterVerseSelectionPane.selectedVerseProperty().addListener((observable, oldValue, newValue) ->
                tokenListView.setVerseTokenPairGroup(newValue));
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setSpacing(5);
        vBox.getChildren().addAll(chapterVerseSelectionPane, tokenListView);

        BorderPane rightPane = new BorderPane();
        rightPane.setCenter(vBox);

        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(new Pane(), rightPane);

        setCenter(splitPane);
    }
}
