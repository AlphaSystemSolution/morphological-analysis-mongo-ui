package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control;

import com.alphasystem.morphologicalanalysis.ui.control.ChapterVerseSelectionPane;
import javafx.geometry.Pos;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author sali
 */
@Component
public class TokenEditorPane extends BorderPane {

    @Autowired private ChapterVerseSelectionPane chapterVerseSelectionPane;
    @Autowired private TokenListView tokenListView;
    @Autowired private TokenEditorView tokenEditorView;

    @PostConstruct
    public void postConstruct() {
        chapterVerseSelectionPane.selectedVerseProperty().addListener((observable, oldValue, newValue) ->
                tokenListView.setVerseTokenPairGroup(newValue));
        tokenEditorView.tokenProperty().bind(tokenListView.selectedTokenProperty());
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setSpacing(5);
        vBox.getChildren().addAll(chapterVerseSelectionPane, tokenListView);

        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(tokenEditorView, vBox);
        // during initialization split pane resize it self and thus reset divider position to default value, in order
        // to set desired divider position listen to width property change event and set the divider position
        splitPane.widthProperty().addListener((observable, oldValue, newValue) -> splitPane.setDividerPosition(0, 0.78f));

        setCenter(splitPane);
    }
}
