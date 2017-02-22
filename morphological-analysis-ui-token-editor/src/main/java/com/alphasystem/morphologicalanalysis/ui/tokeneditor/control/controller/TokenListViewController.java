package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.controller;

import com.alphasystem.arabic.model.ArabicWord;
import com.alphasystem.fx.ui.util.UiUtilities;
import com.alphasystem.morphologicalanalysis.common.model.VerseTokenPairGroup;
import com.alphasystem.morphologicalanalysis.ui.control.TextTableCell;
import com.alphasystem.morphologicalanalysis.ui.model.TokenCellModel;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.TokenListView;
import com.alphasystem.morphologicalanalysis.ui.util.ApplicationHelper;
import com.alphasystem.morphologicalanalysis.ui.util.RestClient;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sali
 */
@Component
public class TokenListViewController extends BorderPane {

    private static final int WIDTH1 = 120;
    private static final int WIDTH2 = 200;
    private static final int WIDTH3 = 50;
    private static final int WIDTH = WIDTH1 + WIDTH2 + WIDTH3 + 30;

    @Autowired private RestClient restClient;
    @Autowired private TokenListView control;
    private final TableView<TokenCellModel> tableView = new TableView<>();

    @Autowired
    @SuppressWarnings({"unchecked"})
    void postConstruct() {
        // initialize table, hide header
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableView.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        tableView.setEditable(true);
        tableView.setMaxWidth(WIDTH);
        tableView.widthProperty().addListener((observable, oldValue, newValue) -> {
            final Pane header = (Pane) tableView.lookup("TableHeaderRow");
            if (header.isVisible()) {
                header.setMaxHeight(0);
                header.setMinHeight(0);
                header.setPrefHeight(0);
                header.setVisible(false);
            }
        });

        // initializing tokenNumberColumn
        final TableColumn<TokenCellModel, ArabicWord> tokenNumberColumn = new TableColumn<>();
        tokenNumberColumn.setMinWidth(WIDTH1);
        tokenNumberColumn.setMaxWidth(WIDTH1);
        tokenNumberColumn.setPrefWidth(WIDTH1);
        tokenNumberColumn.setCellValueFactory(param -> param.getValue().displayNameProperty());
        tokenNumberColumn.setCellFactory(TextTableCell::new);

        // initializing tokenTextColumn
        final TableColumn<TokenCellModel, ArabicWord> tokenTextColumn = new TableColumn<>();
        tokenTextColumn.setMinWidth(WIDTH2);
        tokenTextColumn.setMaxWidth(WIDTH2);
        tokenTextColumn.setPrefWidth(WIDTH2);
        tokenTextColumn.setCellValueFactory(param -> param.getValue().textProperty());
        tokenTextColumn.setCellFactory(TextTableCell::new);

        // initializing tokenTextColumn
        final TableColumn<TokenCellModel, Boolean> checkBoxColumn = new TableColumn<>();
        checkBoxColumn.setMinWidth(WIDTH3);
        checkBoxColumn.setMaxWidth(WIDTH3);
        checkBoxColumn.setPrefWidth(WIDTH3);
        checkBoxColumn.setCellValueFactory(param -> param.getValue().checkedProperty());
        Callback<Integer, ObservableValue<Boolean>> cb = index -> {
            final TokenCellModel tableModel = checkBoxColumn.getTableView().getItems().get(index);
            final BooleanProperty checkedProperty = tableModel.checkedProperty();
            if (checkedProperty.get()) {
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>: " + tableModel.getToken());
            }
            return checkedProperty;
        };
        checkBoxColumn.setCellFactory(param -> new CheckBoxTableCell<>(cb));

        tableView.getColumns().addAll(checkBoxColumn, tokenTextColumn, tokenNumberColumn);

        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                control.setSelectedToken((newValue == null) ? null : newValue.getToken()));
        control.refreshProperty().addListener((observable, oldValue, newValue) -> refreshTable(control.getVerseTokenPairGroup(), newValue));
        control.verseTokenPairGroupProperty().addListener((observable, oldValue, newValue) -> refreshTable(newValue, control.isRefresh()));
        refreshTable(control.getVerseTokenPairGroup(), control.isRefresh());

        setCenter(UiUtilities.wrapInScrollPane(tableView));
    }

    private void refreshTable(VerseTokenPairGroup group, boolean refresh) {
        tableView.getItems().clear();
        if (group != null) {
            final List<Token> tokens = restClient.getTokens(group, refresh);
            if (tokens != null && !tokens.isEmpty()) {
                tableView.setItems(FXCollections.observableArrayList(tokens.stream().map(TokenCellModel::new).collect(Collectors.toList())));
                tableView.getSelectionModel().selectFirst();
                final double height = ApplicationHelper.calculateTableHeight(tableView.getItems().size());
                tableView.setMinHeight(height);
                tableView.setMaxHeight(height);
                tableView.setPrefHeight(height);
            }
        }
        tableView.refresh();
    }

    public List<Token> getSelectedTokens() {
        List<Token> tokens = new ArrayList<>();
        final ObservableList<TokenCellModel> items = tableView.getItems();
        if (items != null && !items.isEmpty()) {
            items.forEach(model -> {
                if (model.isChecked()) {
                    tokens.add(model.getToken());
                }
            });
        }
        return tokens;
    }
}
