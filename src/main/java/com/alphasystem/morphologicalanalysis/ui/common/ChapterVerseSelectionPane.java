package com.alphasystem.morphologicalanalysis.ui.common;

import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.util.RepositoryTool;
import com.alphasystem.morphologicalanalysis.ui.util.ChapterAdapter;
import com.alphasystem.morphologicalanalysis.ui.util.VerseAdapter;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Chapter;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

import static com.alphasystem.morphologicalanalysis.ui.common.Global.RESOURCE_BUNDLE;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.TREE_BANK_STYLE_SHEET;

/**
 * @author sali
 */
public class ChapterVerseSelectionPane extends BorderPane {

    private ComboBox<ChapterAdapter> chapterNameComboBox;
    private ComboBox<VerseAdapter> verseAdapterComboBox;

    // other elements
    private RepositoryTool repositoryTool = RepositoryTool.getInstance();
    private List<ChapterAdapter> chapters;
    private ChapterAdapter selectedChapter;
    private VerseAdapter selectedVerse;
    private VerseSelectionAction action;

    public ChapterVerseSelectionPane(VerseSelectionAction action) {
        this.action = action;
        Service<List<ChapterAdapter>> service = repositoryTool.getAllChapters();
        service.start();
        service.setOnSucceeded(event -> {
            chapters = (List<ChapterAdapter>) event.getSource().getValue();
            initPane();
        });
    }

    private void initPane() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        verseAdapterComboBox = new ComboBox<>();
        verseAdapterComboBox.getStylesheets().add(TREE_BANK_STYLE_SHEET);
        verseAdapterComboBox.setOnAction(event -> {
            selectedVerse = verseAdapterComboBox.getSelectionModel().getSelectedItem();
            if (selectedVerse == null) {
                return;
            }
            action.doOnVerseChange();
        });

        Label label = new Label(RESOURCE_BUNDLE.getString("chapterName.label"));
        grid.add(label, 0, 0);

        chapterNameComboBox = new ComboBox();
        chapterNameComboBox.getStylesheets().add(TREE_BANK_STYLE_SHEET);
        chapterNameComboBox.getItems().addAll(chapters.toArray(new ChapterAdapter[chapters.size()]));
        chapterNameComboBox.getSelectionModel().select(0);
        selectedChapter = chapters.get(0);
        chapterNameComboBox.setOnAction(event -> {
            selectedChapter = chapterNameComboBox.getSelectionModel().getSelectedItem();
            initVerseComboBox();
        });
        label.setLabelFor(chapterNameComboBox);
        grid.add(chapterNameComboBox, 0, 1);

        label = new Label(RESOURCE_BUNDLE.getString("verseNumber.label"));
        grid.add(label, 1, 0);
        initVerseComboBox();
        grid.add(verseAdapterComboBox, 1, 1);

        setCenter(grid);
    }

    private void initVerseComboBox() {
        ObservableList<VerseAdapter> items = verseAdapterComboBox.getItems();
        items.remove(0, items.size());

        List<VerseAdapter> verseNumbers = new ArrayList<>();
        if (selectedChapter != null) {
            Chapter chapter = selectedChapter.getChapter();
            int verseCount = chapter.getVerseCount();
            for (int i = 1; i <= verseCount; i++) {
                verseNumbers.add(new VerseAdapter(chapter.getChapterNumber(), i));
            }
        }
        int size = verseNumbers.size();
        verseAdapterComboBox.getItems().addAll(verseNumbers.toArray(new VerseAdapter[size]));
        if (size > 0) {
            verseAdapterComboBox.getSelectionModel().select(0);
            selectedVerse = verseAdapterComboBox.getItems().get(0);
            action.doOnVerseChange();
        }
        verseAdapterComboBox.requestLayout();
    }


}
