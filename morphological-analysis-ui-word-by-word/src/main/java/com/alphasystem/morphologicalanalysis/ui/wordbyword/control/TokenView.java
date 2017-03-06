package com.alphasystem.morphologicalanalysis.ui.wordbyword.control;

import com.alphasystem.arabic.model.ArabicWord;
import com.alphasystem.fx.ui.util.UiUtilities;
import com.alphasystem.morphologicalanalysis.common.model.VerseTokenPairGroup;
import com.alphasystem.morphologicalanalysis.common.model.VerseTokensPair;
import com.alphasystem.morphologicalanalysis.ui.control.TextTableCell;
import com.alphasystem.morphologicalanalysis.ui.model.TokenCellModel;
import com.alphasystem.morphologicalanalysis.ui.util.ApplicationHelper;
import com.alphasystem.morphologicalanalysis.ui.util.RestClient;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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
    private final BooleanProperty refresh = new SimpleBooleanProperty(this, "refresh", true);
    private TableView<TokenCellModel> tableView = new TableView<>();
    @Autowired private RestClient restClient;

    public TokenView() {
        tableView.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        tableView.setFixedCellSize(ApplicationHelper.ROW_SIZE  + 20);
        tableView.setPrefSize(Screen.getPrimary().getVisualBounds().getWidth(), ApplicationHelper.calculateTableHeight(tableView.getItems().size()));
        tableView.setEditable(true);
        setCenter(UiUtilities.wrapInScrollPane(tableView));
    }

    @PostConstruct
    @SuppressWarnings("unchecked")
    public void postConstruct() {
        final TableColumn<TokenCellModel, String> descriptionColumn = new TableColumn<>();
        descriptionColumn.setMinWidth(1000);
        descriptionColumn.setText("Description");
        descriptionColumn.setCellValueFactory(param -> param.getValue().morphologicalDescriptionProperty());

        final TableColumn<TokenCellModel, ArabicWord> tokenColumn = new TableColumn<>();
        tokenColumn.setMinWidth(300);
        tokenColumn.setText("Text");
        tokenColumn.setCellValueFactory(param -> param.getValue().textProperty());
        tokenColumn.setCellFactory(param -> {
            TextTableCell<TokenCellModel, ArabicWord> cell = new TextTableCell<>(param);
            cell.setFont(ApplicationHelper.PREFERENCES.getArabicFont30());
            return cell;
        });

        final TableColumn<TokenCellModel, ArabicWord> tokenNumberColumn = new TableColumn<>();
        tokenNumberColumn.setMinWidth(200);
        tokenNumberColumn.setText("Token Number");
        tokenNumberColumn.setCellValueFactory(param -> param.getValue().displayNameProperty());
        tokenNumberColumn.setCellFactory(param -> {
            TextTableCell<TokenCellModel, ArabicWord> cell = new TextTableCell<>(param);
            cell.setFont(ApplicationHelper.PREFERENCES.getArabicFont30());
            return cell;
        });

        final TableColumn<TokenCellModel, Boolean> checkBoxColumn = new TableColumn<>();
        checkBoxColumn.setMaxWidth(50);
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

        tableView.getColumns().addAll(checkBoxColumn, tokenColumn, tokenNumberColumn, descriptionColumn);

        refresh.addListener((observable, oldValue, newValue) -> refreshTable(getVerseTokenPairGroup(), newValue));
        verseTokenPairGroup.addListener((observable, oldValue, newValue) -> refreshTable(newValue, isRefresh()));
        refreshTable(getVerseTokenPairGroup(), isRefresh());
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

    private VerseTokenPairGroup getVerseTokenPairGroup() {
        return verseTokenPairGroup.get();
    }

    public final void setVerseTokenPairGroup(VerseTokenPairGroup verseTokenPairGroup) {
        this.verseTokenPairGroup.set(verseTokenPairGroup);
    }

    private boolean isRefresh() {
        return refresh.get();
    }

    public final void setRefresh(boolean refresh) {
        this.refresh.set(refresh);
    }

}
