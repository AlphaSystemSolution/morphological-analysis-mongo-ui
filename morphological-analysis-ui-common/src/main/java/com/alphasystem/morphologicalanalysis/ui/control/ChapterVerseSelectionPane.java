package com.alphasystem.morphologicalanalysis.ui.control;


import com.alphasystem.morphologicalanalysis.common.model.VerseTokenPairGroup;
import com.alphasystem.morphologicalanalysis.ui.util.RestClient;
import com.alphasystem.morphologicalanalysis.ui.util.VerseTokensPairsReader;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Chapter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import static javafx.geometry.Pos.CENTER;

/**
 * @author sali
 */
@Component
public class ChapterVerseSelectionPane extends BorderPane {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(ChapterVerseSelectionPane.class.getSimpleName());

    @Autowired private RestClient restClient;
    private ObjectProperty<LayoutPolicy> layoutPolicy = new SimpleObjectProperty<>(this, "", LayoutPolicy.HORIZONTAL);
    private Map<Integer, List<VerseTokenPairGroup>> groupMap;
    private Label chapterNameLabel;
    private ComboBox<Chapter> chapterNameComboBox;
    private Label verseNumberLabel;
    private ComboBox<VerseTokenPairGroup> verseComboBox;
    private Chapter[] chapters;

    @SuppressWarnings({"unchecked"})
    public ChapterVerseSelectionPane() {
        groupMap = VerseTokensPairsReader.read();
    }

    @PostConstruct
    public void postConstruct() {
        chapters = restClient.getChapters();
        initPane();
        layoutPolicy.addListener((observable, oldValue, newValue) -> initPane());
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
        chapterNameComboBox.getItems().addAll(chapters);
        chapterNameComboBox.getSelectionModel().select(0);
        chapterNameComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> initVerseComboBox(newValue));
        chapterNameLabel.setLabelFor(chapterNameComboBox);
        grid.add(chapterNameComboBox, 0, 1);

        verseNumberLabel = new Label(RESOURCE_BUNDLE.getString("verseNumber.label"));

        final LayoutPolicy layoutPolicy = this.layoutPolicy.get();
        final boolean horizontalPolicy = LayoutPolicy.HORIZONTAL.equals(layoutPolicy);
        int columnIndex = horizontalPolicy ? 1 : 0;
        int rowIndex = horizontalPolicy ? 0 : 2;
        grid.add(verseNumberLabel, columnIndex, rowIndex);
        initVerseComboBox(chapterNameComboBox.getItems().get(0));

        columnIndex = horizontalPolicy ? 1 : 0;
        rowIndex = horizontalPolicy ? 1 : 3;
        grid.add(verseComboBox, columnIndex, rowIndex);

        HBox hBox = new HBox();
        hBox.setAlignment(CENTER);
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

    public final void setLayoutPolicy(LayoutPolicy layoutPolicy) {
        this.layoutPolicy.set(layoutPolicy);
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

    public enum LayoutPolicy {
        HORIZONTAL, VERTICAL;
    }
}
