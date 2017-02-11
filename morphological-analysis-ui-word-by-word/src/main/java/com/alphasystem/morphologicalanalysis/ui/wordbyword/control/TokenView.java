package com.alphasystem.morphologicalanalysis.ui.wordbyword.control;

import com.alphasystem.fx.ui.util.UiUtilities;
import com.alphasystem.morphologicalanalysis.common.model.VerseTokenPairGroup;
import com.alphasystem.morphologicalanalysis.common.model.VerseTokensPair;
import com.alphasystem.morphologicalanalysis.ui.util.ApplicationHelper;
import com.alphasystem.morphologicalanalysis.ui.util.RestClient;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.model.TableCellModel;
import com.alphasystem.morphologicalanalysis.util.MorphologicalAnalysisPreferences;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import com.alphasystem.util.GenericPreferences;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.CheckBoxTreeTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

import static javafx.geometry.NodeOrientation.RIGHT_TO_LEFT;
import static javafx.scene.control.ContentDisplay.GRAPHIC_ONLY;

/**
 * @author sali
 */
@Component
public class TokenView extends BorderPane {

    private static TableCellModel rootElement = new TableCellModel((Token) null);
    private static VerseTokenPairGroup initialGroup = new VerseTokenPairGroup();

    static {
        initialGroup.setChapterNumber(1);
        initialGroup.getPairs().add(new VerseTokensPair(1, 1));
        initialGroup.getPairs().add(new VerseTokensPair(2, 1));
        initialGroup.getPairs().add(new VerseTokensPair(3, 1));
        initialGroup.getPairs().add(new VerseTokensPair(4, 1));
    }

    private final MorphologicalAnalysisPreferences preferences = GenericPreferences.getInstance(MorphologicalAnalysisPreferences.class);
    private final ObjectProperty<VerseTokenPairGroup> verseTokenPairGroup = new SimpleObjectProperty<>(this, "verseTokenPairGroup", initialGroup);
    private final BooleanProperty refresh = new SimpleBooleanProperty(this, "refresh", true);
    private TreeTableView<TableCellModel> treeTableView = new TreeTableView<>();
    private final TreeItem<TableCellModel> root = new TreeItem<>(rootElement);
    @Autowired private RestClient restClient;

    public TokenView() {
        treeTableView.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        treeTableView.setShowRoot(false);
        treeTableView.setFixedCellSize(ApplicationHelper.ROW_SIZE);
        treeTableView.setPrefSize(Screen.getPrimary().getVisualBounds().getWidth(), ApplicationHelper.calculateTableHeight(root.getChildren().size()));
        treeTableView.setEditable(true);
        treeTableView.setRoot(root);
        root.setExpanded(true);
        setCenter(UiUtilities.wrapInScrollPane(treeTableView));
    }

    @PostConstruct
    @SuppressWarnings("unchecked")
    public void postConstruct() {
        TreeTableColumn<TableCellModel, String> descriptionColumn = new TreeTableColumn<>();
        descriptionColumn.setMinWidth(1000);
        descriptionColumn.setText("Description");
        descriptionColumn.setCellValueFactory(param -> param.getValue().getValue().morphologicalDescriptionProperty());

        TreeTableColumn<TableCellModel, String> tokenColumn = new TreeTableColumn<>();
        tokenColumn.setMinWidth(300);
        tokenColumn.setText("Text");
        tokenColumn.setCellValueFactory(param -> param.getValue().getValue().textProperty());
        tokenColumn.setCellFactory(TextTreeTableCell::new);

        TreeTableColumn<TableCellModel, String> tokenNumberColumn = new TreeTableColumn<>();
        tokenNumberColumn.setMinWidth(200);
        tokenNumberColumn.setText("Token Number");
        tokenNumberColumn.setCellValueFactory(param -> param.getValue().getValue().displayNameProperty());
        tokenNumberColumn.setCellFactory(TextTreeTableCell::new);

        TreeTableColumn<TableCellModel, Boolean> checkBoxColumn = new TreeTableColumn<>();
        checkBoxColumn.setMaxWidth(50);
        checkBoxColumn.setCellValueFactory(param -> param.getValue().getValue().checkedProperty());
        checkBoxColumn.setCellFactory(CheckBoxTreeTableCell.forTreeTableColumn(checkBoxColumn));

        treeTableView.getColumns().addAll(checkBoxColumn, tokenColumn, tokenNumberColumn, descriptionColumn);

        refresh.addListener((observable, oldValue, newValue) -> refreshTable(getVerseTokenPairGroup(), newValue));
        verseTokenPairGroup.addListener((observable, oldValue, newValue) -> refreshTable(newValue, isRefresh()));
        refreshTable(getVerseTokenPairGroup(), isRefresh());
    }

    private void refreshTable(VerseTokenPairGroup group, boolean refresh) {
        root.getChildren().remove(0, root.getChildren().size());
        if (group != null) {
            final List<Token> tokens = restClient.getTokens(group, refresh);
            if (tokens != null && !tokens.isEmpty()) {
                tokens.forEach(this::addTokenToTree);
                treeTableView.setPrefHeight(ApplicationHelper.calculateTableHeight(root.getChildren().size()));
            }
        }
        treeTableView.requestLayout();
    }

    private void addTokenToTree(Token token) {
        TreeItem<TableCellModel> tokenTreeItem = new TreeItem<>(new TableCellModel(token));
        // token.getLocations().forEach(location -> addLocationToTree(tokenTreeItem, location));
        root.getChildren().add(tokenTreeItem);
    }

    private void addLocationToTree(TreeItem<TableCellModel> tokenTreeItem, Location location) {
        TreeItem<TableCellModel> locationTeeItem = new TreeItem<>(new TableCellModel(location));
        tokenTreeItem.getChildren().add(locationTeeItem);
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

    private class TextTreeTableCell extends TreeTableCell<TableCellModel, String> {
        private final Text text;

        private TextTreeTableCell(TreeTableColumn<TableCellModel, String> column) {
            setContentDisplay(GRAPHIC_ONLY);
            text = new Text();
            text.setFont(preferences.getArabicFont30());
            text.setTextAlignment(TextAlignment.RIGHT);
            text.setNodeOrientation(RIGHT_TO_LEFT);
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            BorderPane borderPane = new BorderPane();
            if (item != null && !empty) {
                text.setText(item);
                borderPane.setCenter(text);
            }
            setGraphic(borderPane);
        }
    }
}
