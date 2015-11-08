package com.alphasystem.morphologicalanalysis.ui.dependencygraph.components;

import com.alphasystem.morphologicalanalysis.graph.model.DependencyGraph;
import com.alphasystem.morphologicalanalysis.ui.common.ChapterVerseSelectionPane;
import com.alphasystem.morphologicalanalysis.ui.common.DependencyGraphListCell;
import com.alphasystem.morphologicalanalysis.ui.common.Global;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.VerseTokenPairGroup;
import com.alphasystem.morphologicalanalysis.util.DependencyGraphComparator;
import com.alphasystem.morphologicalanalysis.util.RepositoryTool;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.util.List;

import static java.util.Collections.sort;
import static javafx.scene.control.ButtonType.CANCEL;
import static javafx.scene.control.ButtonType.OK;

/**
 * @author sali
 */
public class DependencyGraphSelectionDialog extends Dialog<DependencyGraph> {

    private final RepositoryTool repositoryTool = RepositoryTool.getInstance();
    private ChapterVerseSelectionPane chapterVerseSelectionPane;
    private ComboBox<DependencyGraph> dependencyGraphComboBox;

    public DependencyGraphSelectionDialog() {
        setTitle(getLabel("title"));

        dependencyGraphComboBox = new ComboBox<>();
        dependencyGraphComboBox.setCellFactory(param -> new DependencyGraphListCell());
        dependencyGraphComboBox.setButtonCell(new DependencyGraphListCell());

        chapterVerseSelectionPane = new ChapterVerseSelectionPane();
        chapterVerseSelectionPane.availableProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                chapterVerseSelectionPane.selectedVerseProperty().addListener((observable1, oldValue1, newValue1) -> {
                    if (newValue1 != null) {
                        updateTokens();
                    }
                });
                updateTokens();
                initDialog();
            }
        });

        setResultConverter(this::createResult);
    }

    private static String getLabel(String label) {
        return Global.getLabel(DependencyGraphSelectionDialog.class, label);
    }

    private void updateTokens() {
        VerseTokenPairGroup selectedGroup = chapterVerseSelectionPane.getSelectedVerse();
        List<DependencyGraph> dependencyGraphs = repositoryTool.getDependencyGraphs(selectedGroup);
        sort(dependencyGraphs, new DependencyGraphComparator());
        dependencyGraphComboBox.getItems().remove(0, dependencyGraphComboBox.getItems().size());
        dependencyGraphComboBox.getItems().addAll(dependencyGraphs);
        dependencyGraphComboBox.getSelectionModel().selectFirst();
        dependencyGraphComboBox.requestLayout();
    }

    private DependencyGraph createResult(ButtonType param) {
        ButtonBar.ButtonData buttonData = param.getButtonData();
        if (buttonData.isCancelButton()) {
            return null;
        }

        return dependencyGraphComboBox.getValue();
    }

    private void initDialog() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));

        gridPane.add(chapterVerseSelectionPane.getChapterNameLabel(), 0, 0);
        gridPane.add(chapterVerseSelectionPane.getChapterNameComboBox(), 1, 0);
        gridPane.add(chapterVerseSelectionPane.getVerseNumberLabel(), 0, 1);
        gridPane.add(chapterVerseSelectionPane.getVerseComboBox(), 1, 1);

        Label label = new Label(getLabel("graph"));
        label.setLabelFor(dependencyGraphComboBox);
        gridPane.add(label, 0, 2);
        gridPane.add(dependencyGraphComboBox, 1, 2);

        getDialogPane().getButtonTypes().addAll(OK, CANCEL);
        final Button okButton = (Button) getDialogPane().lookupButton(OK);

        dependencyGraphComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    okButton.disableProperty().setValue(newValue == null);
                });

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(gridPane);
        getDialogPane().setContent(borderPane);
    }

}
