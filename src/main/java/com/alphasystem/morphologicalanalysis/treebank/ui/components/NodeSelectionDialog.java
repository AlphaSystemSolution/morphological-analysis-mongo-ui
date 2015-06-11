package com.alphasystem.morphologicalanalysis.treebank.ui.components;

import com.alphasystem.morphologicalanalysis.graph.model.DependencyGraph;
import com.alphasystem.morphologicalanalysis.treebank.ui.model.TokenListCell;
import com.alphasystem.morphologicalanalysis.ui.common.ChapterVerseSelectionPane;
import com.alphasystem.morphologicalanalysis.ui.common.model.VerseAdapter;
import com.alphasystem.morphologicalanalysis.util.RepositoryTool;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.alphasystem.morphologicalanalysis.ui.common.Global.TREE_BANK_STYLE_SHEET;
import static javafx.scene.control.ButtonBar.ButtonData.CANCEL_CLOSE;
import static javafx.scene.control.ButtonBar.ButtonData.OK_DONE;

/**
 * @author sali
 */
public class NodeSelectionDialog extends Dialog<DependencyGraph> {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("resources");

    // UI elements
    private ChapterVerseSelectionPane chapterVerseSelectionPane;
    private ListView<TokenListCell> tokensList;

    // other elements
    private RepositoryTool repositoryTool = RepositoryTool.getInstance();

    public NodeSelectionDialog() {
        setTitle("Import Tokens");
        setHeaderText("Select chapter and verse.");

        chapterVerseSelectionPane = new ChapterVerseSelectionPane();

        chapterVerseSelectionPane.avaialbleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                initDialog();
            }
        });

    }

    private void initDialog() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        grid.add(chapterVerseSelectionPane, 0, 0, 2, 2);

        grid.add(new Separator(), 0, 3, 2, 1);

        tokensList = new ListView<>();
        tokensList.getStylesheets().add(TREE_BANK_STYLE_SHEET);
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(tokensList);
        grid.add(borderPane, 0, 3, 2, 1);

        chapterVerseSelectionPane.selectedVerseProperty().addListener((observable, oldValue, newValue) -> {
            loadTokens();
        });

        ButtonType okButton = new ButtonType("OK", OK_DONE);
        setResultConverter(dialogButton -> {
            List<Token> results = new ArrayList<Token>();
            ObservableList<TokenListCell> items = tokensList.getItems();
            for (TokenListCell item : items) {
                if (item.isSelected()) {
                    results.add(item.getToken());
                    item.setSelected(false);
                }
            }
            VerseAdapter selectedVerse = chapterVerseSelectionPane.getSelectedVerse();
            return repositoryTool.createDependencyGraph(selectedVerse.getChapterNumber(),
                    selectedVerse.getVerseNumber(), results);
        });

        loadTokens();

        ButtonType cancelButton = new ButtonType("Cancel", CANCEL_CLOSE);
        getDialogPane().getButtonTypes().addAll(okButton, cancelButton);
        getDialogPane().setContent(grid);
        getDialogPane().setPrefWidth(400);
    }

    private void loadTokens() {
        VerseAdapter selectedVerse = chapterVerseSelectionPane.getSelectedVerse();
        if (selectedVerse == null) {
            return;
        }
        Service<List<Token>> service = repositoryTool.getTokens(selectedVerse.getChapterNumber(),
                selectedVerse.getVerseNumber());
        service.start();
        service.setOnSucceeded(event -> {
            List<Token> tokens = (List<Token>) event.getSource().getValue();
            initTokenList(tokens);
        });
    }

    private void initTokenList(List<Token> tokens) {
        ObservableList<TokenListCell> items = tokensList.getItems();
        items.remove(0, items.size());
        ObservableList<TokenListCell> listItems = FXCollections.observableArrayList();
        for (Token token : tokens) {
            TokenListCell cell = new TokenListCell(token);
            listItems.add(cell);
        }
        tokensList.setItems(listItems);
        Callback<TokenListCell, ObservableValue<Boolean>> getProperty = new Callback<TokenListCell, ObservableValue<Boolean>>() {
            @Override
            public BooleanProperty call(TokenListCell layer) {
                return layer.selectedProperty();

            }
        };
        Callback<ListView<TokenListCell>, ListCell<TokenListCell>> forListView = CheckBoxListCell.forListView(getProperty);
        tokensList.setCellFactory(forListView);
        tokensList.requestLayout();
    }
}
