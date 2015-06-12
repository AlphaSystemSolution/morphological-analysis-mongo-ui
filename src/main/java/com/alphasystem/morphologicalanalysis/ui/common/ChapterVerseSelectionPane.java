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

    private ComboBox<Chapter> chapterNameComboBox;
    private ComboBox<Verse> verseComboBox;

    // other elements
    private RepositoryTool repositoryTool = RepositoryTool.getInstance();
    private List<Chapter> chapters;
    private BooleanProperty avaialble = new SimpleBooleanProperty(false);

    public ChapterVerseSelectionPane() {
        Service<List<Chapter>> service = repositoryTool.getAllChapters();
        service.start();
        service.setOnSucceeded(event -> {
            chapters = (List<Chapter>) event.getSource().getValue();
            initPane();
            avaialble.setValue(true);
        });
    }

    public boolean getAvaialble() {
        return avaialble.get();
    }

    public BooleanProperty avaialbleProperty() {
        return avaialble;
    }

    private void initPane() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        verseComboBox = new ComboBox<>();
        verseComboBox.setButtonCell(new VerseListCell());
        verseComboBox.setCellFactory(param -> new VerseListCell());

        Label label = new Label(RESOURCE_BUNDLE.getString("chapterName.label"));
        grid.add(label, 0, 0);

        chapterNameComboBox = new ComboBox();
        chapterNameComboBox.setButtonCell(new ChapterListCell());
        chapterNameComboBox.setCellFactory(param -> new ChapterListCell());
        chapterNameComboBox.getItems().addAll(chapters.toArray(new Chapter[chapters.size()]));
        chapterNameComboBox.getSelectionModel().select(0);
        chapterNameComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    initVerseComboBox(newValue);
                });
        label.setLabelFor(chapterNameComboBox);
        grid.add(chapterNameComboBox, 0, 1);

        label = new Label(RESOURCE_BUNDLE.getString("verseNumber.label"));
        grid.add(label, 1, 0);
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
            verseComboBox.getSelectionModel().select(0);
        }
        verseComboBox.requestLayout();
    }

    public ComboBox<Verse> getVerseComboBox() {
        return verseComboBox;
    }

    public ReadOnlyObjectProperty<Verse> selectedVerseProperty() {
        return verseComboBox.getSelectionModel().selectedItemProperty();
    }

    public Verse getSelectedVerse() {
        return selectedVerseProperty().get();
    }
}
