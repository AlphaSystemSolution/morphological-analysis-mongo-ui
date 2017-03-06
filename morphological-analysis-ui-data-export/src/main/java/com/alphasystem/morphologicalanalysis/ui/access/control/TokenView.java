package com.alphasystem.morphologicalanalysis.ui.access.control;

import com.alphasystem.access.model.TokenAdapter;
import com.alphasystem.arabic.model.ArabicWord;
import com.alphasystem.fx.ui.util.UiUtilities;
import com.alphasystem.morphologicalanalysis.common.model.VerseTokenPairGroup;
import com.alphasystem.morphologicalanalysis.common.model.VerseTokensPair;
import com.alphasystem.morphologicalanalysis.ui.access.model.AccessTokenCellModel;
import com.alphasystem.morphologicalanalysis.ui.control.TextTableCell;
import com.alphasystem.morphologicalanalysis.ui.service.TokenLoaderService;
import com.alphasystem.morphologicalanalysis.ui.service.TokenResultAdapter;
import com.alphasystem.morphologicalanalysis.ui.util.ApplicationHelper;
import com.alphasystem.morphologicalanalysis.ui.util.RestClient;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sali
 */
@Component
public class TokenView extends BorderPane {

    private static VerseTokenPairGroup initialGroup = new VerseTokenPairGroup();

    static {
        initialGroup.setChapterNumber(1);
        initialGroup.getPairs().add(new VerseTokensPair(1, 1));
        initialGroup.getPairs().add(new VerseTokensPair(2, 1));
        initialGroup.getPairs().add(new VerseTokensPair(3, 1));
        initialGroup.getPairs().add(new VerseTokensPair(4, 1));
    }

    private final ObjectProperty<VerseTokenPairGroup> verseTokenPairGroup = new SimpleObjectProperty<>(this, "verseTokenPairGroup", initialGroup);
    private TableView<AccessTokenCellModel> tableView = new TableView<>();
    private CheckBox selectAll;
    @Autowired private RestClient restClient;

    public TokenView() {
        tableView.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        tableView.setFixedCellSize(ApplicationHelper.ROW_SIZE + 20);
        tableView.setPrefSize(Screen.getPrimary().getVisualBounds().getWidth(), ApplicationHelper.calculateTableHeight(tableView.getItems().size()));
        tableView.setEditable(true);
        setCenter(tableView);
    }

    @PostConstruct
    @SuppressWarnings("unchecked")
    public void postConstruct() {
        final TableColumn<AccessTokenCellModel, Boolean> highlightedColumn = new TableColumn<>();
        highlightedColumn.setMinWidth(150);
        highlightedColumn.setMaxWidth(150);
        highlightedColumn.setPrefWidth(150);
        highlightedColumn.setText("Highlight");
        highlightedColumn.setCellValueFactory(param -> param.getValue().highlightedProperty());
        Callback<Integer, ObservableValue<Boolean>> cb = index -> highlightedColumn.getTableView().getItems().get(index).highlightedProperty();
        highlightedColumn.setCellFactory(param -> new CheckBoxTableCell<>(cb));

        final TableColumn<AccessTokenCellModel, ArabicWord> tokenColumn = new TableColumn<>();
        tokenColumn.setPrefWidth(500);
        tokenColumn.setText("Text");
        tokenColumn.setCellValueFactory(param -> param.getValue().textProperty());
        tokenColumn.setCellFactory(param -> {
            TextTableCell<AccessTokenCellModel, ArabicWord> cell = new TextTableCell<>(param);
            cell.setFont(ApplicationHelper.PREFERENCES.getArabicFont30());
            return cell;
        });

        final TableColumn<AccessTokenCellModel, ArabicWord> tokenNumberColumn = new TableColumn<>();
        tokenNumberColumn.setMinWidth(500);
        tokenNumberColumn.setMaxWidth(500);
        tokenNumberColumn.setPrefWidth(500);
        tokenNumberColumn.setText("Token Number");
        tokenNumberColumn.setCellValueFactory(param -> param.getValue().displayNameProperty());
        tokenNumberColumn.setCellFactory(param -> {
            TextTableCell<AccessTokenCellModel, ArabicWord> cell = new TextTableCell<>(param);
            cell.setFont(ApplicationHelper.PREFERENCES.getArabicFont30());
            return cell;
        });

        selectAll = new CheckBox();
        selectAll.setOnAction(this::selectAll);
        final TableColumn<AccessTokenCellModel, Boolean> checkBoxColumn = new TableColumn<>();
        checkBoxColumn.setMinWidth(50);
        checkBoxColumn.setMaxWidth(50);
        checkBoxColumn.setPrefWidth(50);
        checkBoxColumn.setGraphic(selectAll);
        checkBoxColumn.setCellValueFactory(param -> param.getValue().checkedProperty());
        Callback<Integer, ObservableValue<Boolean>> cb1 = index -> checkBoxColumn.getTableView().getItems().get(index).checkedProperty();
        checkBoxColumn.setCellFactory(param -> new CheckBoxTableCell<>(cb1));

        tableView.getColumns().addAll(checkBoxColumn, tokenColumn, tokenNumberColumn, highlightedColumn);

        verseTokenPairGroup.addListener((observable, oldValue, newValue) -> refreshTable(newValue, false));
        refreshTable(getVerseTokenPairGroup(), false);
    }

    private void selectAll(ActionEvent event) {
        tableView.getItems().forEach(model -> model.setChecked(selectAll.isSelected()));
    }

    private void refreshTable(final VerseTokenPairGroup group, final boolean refresh) {
        if (group != null) {
            UiUtilities.waitCursor(this);
            tableView.getItems().clear();
            final TokenLoaderService service = new TokenLoaderService(group, refresh);
            service.setOnSucceeded(this::onSucceeded);
            service.setOnFailed(this::onFailed);
            service.start();
        }
    }

    private void onSucceeded(WorkerStateEvent event) {
        UiUtilities.defaultCursor(this);
        final TokenResultAdapter result = (TokenResultAdapter) event.getSource().getValue();
        final List<Token> tokens = result.getTokens();
        if (tokens != null && !tokens.isEmpty()) {
            tableView.setItems(FXCollections.observableArrayList(tokens.stream().map(AccessTokenCellModel::new).collect(Collectors.toList())));
            tableView.getSelectionModel().selectFirst();
            final double height = ApplicationHelper.calculateTableHeight(tableView.getItems().size() + 5);
            tableView.setMinHeight(height);
            tableView.setMaxHeight(height);
            tableView.setPrefHeight(height);
        }
        tableView.refresh();
    }

    private void onFailed(WorkerStateEvent event) {
        UiUtilities.defaultCursor(this);
        try {
            throw event.getSource().getException();
        } catch (Throwable throwable) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(String.format("%s:%s", throwable.getClass().getName(), throwable.getMessage()));
            alert.showAndWait();
        }
        tableView.refresh();
    }

    private VerseTokenPairGroup getVerseTokenPairGroup() {
        return verseTokenPairGroup.get();
    }

    public final void setVerseTokenPairGroup(VerseTokenPairGroup verseTokenPairGroup) {
        this.verseTokenPairGroup.set(verseTokenPairGroup);
    }

    public List<TokenAdapter> getSelectedTokens() {
        List<TokenAdapter> selectedTokens = new ArrayList<>();
        tableView.getItems().forEach(model -> {
            if (model.isChecked()) {
                selectedTokens.add(new TokenAdapter(model.getToken(), model.isHighlighted()));
                model.setChecked(false);
                model.setHighlighted(false);
            }
        });
        selectAll.setSelected(false);
        return selectedTokens;
    }

}
