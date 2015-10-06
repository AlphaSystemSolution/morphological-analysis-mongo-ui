package com.alphasystem.morphologicalanalysis.ui.common;

import com.alphasystem.morphologicalanalysis.util.RepositoryTool;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Chapter;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Verse;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

import static com.alphasystem.morphologicalanalysis.ui.common.Global.RESOURCE_BUNDLE;

/**
 * @author sali
 */
public class ChapterVerseSelectionPane extends BorderPane {

    private Label chapterNameLabel;
    private ComboBox<Chapter> chapterNameComboBox;
    private Label verseNumberLabel;
    private ComboBox<Verse> verseComboBox;

    // other elements
    private List<Chapter> chapters;
    private BooleanProperty available = new SimpleBooleanProperty(false);

    @SuppressWarnings({"unchecked"})
    public ChapterVerseSelectionPane() {
        Service<List<Chapter>> service = RepositoryTool.getInstance().getAllChapters();
        service.start();
        service.setOnSucceeded(event -> {
            chapters = (List<Chapter>) event.getSource().getValue();
            initPane();
            available.setValue(true);
        });
    }

    public BooleanProperty availableProperty() {
        return available;
    }

    private void initPane() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        verseComboBox = new ComboBox<>();
        verseComboBox.setButtonCell(new VerseListCell());
        verseComboBox.setCellFactory(param -> new VerseListCell());

        chapterNameLabel = new Label(RESOURCE_BUNDLE.getString("chapterName.label"));
        grid.add(chapterNameLabel, 0, 0);

        chapterNameComboBox = new ComboBox<>();
        chapterNameComboBox.setButtonCell(new ChapterListCell());
        chapterNameComboBox.setCellFactory(param -> new ChapterListCell());
        chapterNameComboBox.getItems().addAll(chapters.toArray(new Chapter[chapters.size()]));
        chapterNameComboBox.getSelectionModel().select(0);
        chapterNameComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    initVerseComboBox(newValue);
                });
        chapterNameLabel.setLabelFor(chapterNameComboBox);
        grid.add(chapterNameComboBox, 0, 1);

        verseNumberLabel = new Label(RESOURCE_BUNDLE.getString("verseNumber.label"));
        grid.add(verseNumberLabel, 1, 0);
        initVerseComboBox(chapterNameComboBox.getItems().get(0));
        grid.add(verseComboBox, 1, 1);

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(0, 50, 0, 50));
        hBox.getChildren().add(grid);
        setCenter(hBox);
    }

    private void initVerseComboBox(Chapter chapter) {
        ObservableList<Verse> items = verseComboBox.getItems();
        items.remove(0, items.size());

        List<Verse> verseNumbers = new ArrayList<>();
        if (chapter != null) {
            int verseCount = chapter.getVerseCount();
            for (int i = 1; i <= verseCount; i++) {
                verseNumbers.add(new Verse(chapter.getChapterNumber(), i));
            }
        }
        int size = verseNumbers.size();
        verseComboBox.getItems().addAll(verseNumbers.toArray(new Verse[size]));
        if (size > 0) {
            verseComboBox.getSelectionModel().selectFirst();
        }
        verseComboBox.requestLayout();
    }

    public ReadOnlyObjectProperty<Verse> selectedVerseProperty() {
        return verseComboBox.getSelectionModel().selectedItemProperty();
    }

    public Verse getSelectedVerse() {
        return selectedVerseProperty().get();
    }

    public Label getChapterNameLabel() {
        return chapterNameLabel;
    }

    public ComboBox<Chapter> getChapterNameComboBox() {
        return chapterNameComboBox;
    }

    public Label getVerseNumberLabel() {
        return verseNumberLabel;
    }

    public ComboBox<Verse> getVerseComboBox() {
        return verseComboBox;
    }
}
