package com.alphasystem.morphologicalanalysis.ui.common;

import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.VerseTokenPairGroup;
import com.alphasystem.morphologicalanalysis.util.RepositoryTool;
import com.alphasystem.morphologicalanalysis.util.VerseTokensPairsReader;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Chapter;
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
import java.util.Map;

import static com.alphasystem.morphologicalanalysis.ui.common.Global.RESOURCE_BUNDLE;

/**
 * @author sali
 */
public class ChapterVerseSelectionPane extends BorderPane {

    Map<Integer, List<VerseTokenPairGroup>> groupMap;
    private Label chapterNameLabel;
    private ComboBox<Chapter> chapterNameComboBox;
    private Label verseNumberLabel;
    private ComboBox<VerseTokenPairGroup> verseComboBox;
    private List<Chapter> chapters;
    private BooleanProperty available = new SimpleBooleanProperty(false);

    @SuppressWarnings({"unchecked"})
    public ChapterVerseSelectionPane() {
        groupMap = VerseTokensPairsReader.read();

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
        verseComboBox.setButtonCell(new VerseTokenPairGroupListCell());
        verseComboBox.setCellFactory(param -> new VerseTokenPairGroupListCell());

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
        ObservableList<VerseTokenPairGroup> items = verseComboBox.getItems();
        items.remove(0, items.size());

        List<VerseTokenPairGroup> verseNumbers = new ArrayList<>();
        if (chapter != null) {
            verseNumbers.addAll(groupMap.get(chapter.getChapterNumber()));

        }
        int size = verseNumbers.size();
        verseComboBox.getItems().addAll(verseNumbers.toArray(new VerseTokenPairGroup[size]));
        if (size > 0) {
            verseComboBox.getSelectionModel().selectFirst();
        }
        verseComboBox.requestLayout();
    }

    public ReadOnlyObjectProperty<VerseTokenPairGroup> selectedVerseProperty() {
        return verseComboBox.getSelectionModel().selectedItemProperty();
    }

    public VerseTokenPairGroup getSelectedVerse() {
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

    public ComboBox<VerseTokenPairGroup> getVerseComboBox() {
        return verseComboBox;
    }
}
