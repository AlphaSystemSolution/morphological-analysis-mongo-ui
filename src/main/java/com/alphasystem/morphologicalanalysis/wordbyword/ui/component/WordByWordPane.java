package com.alphasystem.morphologicalanalysis.wordbyword.ui.component;

import com.alphasystem.morphologicalanalysis.ui.common.ChapterVerseSelectionPane;
import com.alphasystem.morphologicalanalysis.ui.common.model.VerseAdapter;
import com.alphasystem.morphologicalanalysis.util.RepositoryTool;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Verse;
import com.alphasystem.morphologicalanalysis.wordbyword.ui.model.TableCellModel;
import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.alphasystem.morphologicalanalysis.ui.common.Global.TREE_BANK_STYLE_SHEET;
import static javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED;


/**
 * @author sali
 */
public class WordByWordPane extends BorderPane {

    private RepositoryTool repositoryTool = RepositoryTool.getInstance();
    private ChapterVerseSelectionPane chapterVerseSelectionPane;
    private TokenEditorDialog tokenEditorDialog;
    private TableView<TableCellModel> tableView;

    public WordByWordPane() {
        tokenEditorDialog = new TokenEditorDialog(repositoryTool.getTokenByDisplayName("1:1:1"));
        chapterVerseSelectionPane = new ChapterVerseSelectionPane();
        chapterVerseSelectionPane.avaialbleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                initPane();
            }
        });

        TableColumn descriptionColumn = new TableColumn<>();
        descriptionColumn.setMinWidth(700);
        descriptionColumn.setText("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("morphologicalDescription"));

        TableColumn tokenColumn = new TableColumn<>();
        tokenColumn.setMinWidth(200);
        tokenColumn.setText("Token");
        tokenColumn.setCellValueFactory(new PropertyValueFactory<>("tokenText"));

        TableColumn tokenNumberColumn = new TableColumn<>();
        tokenNumberColumn.setMinWidth(200);
        tokenNumberColumn.setText("Token Number");
        tokenNumberColumn.setCellValueFactory(new PropertyValueFactory<>("tokenNumber"));

        TableColumn checkBoxColumn = new TableColumn<>();
        checkBoxColumn.setMaxWidth(30);
        checkBoxColumn.setCellValueFactory(new PropertyValueFactory("checked"));
        checkBoxColumn.setCellFactory(CheckBoxTableCell.forTableColumn(checkBoxColumn));

        tableView = new TableView<>();
        tableView.setRowFactory(tv -> {
            TableRow<TableCellModel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    TableCellModel model = tableView.getItems().get(row.getIndex());
                    Token token = model.getToken();
                    tokenEditorDialog.setToken(null);
                    tokenEditorDialog.setToken(token);
                    Optional<Token> result = tokenEditorDialog.showAndWait();
                    result.ifPresent(t -> {
                        model.setToken(t);
                    });
                }
            });
            return row;
        });
        tableView.getStylesheets().add(TREE_BANK_STYLE_SHEET);
        tableView.setEditable(true);
        tableView.getColumns().addAll(descriptionColumn, tokenColumn, tokenNumberColumn, checkBoxColumn);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(tableView);
        scrollPane.setVbarPolicy(AS_NEEDED);
        scrollPane.setHbarPolicy(AS_NEEDED);

        setCenter(scrollPane);
    }

    private void initPane() {
        // top pane for Menu bar and chapter verse selection
        BorderPane topPane = new BorderPane();
        Pane left = new Pane();
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        double width = bounds.getWidth();
        left.setPrefWidth(width / 3);

        topPane.setLeft(left);
        topPane.setCenter(chapterVerseSelectionPane);
        chapterVerseSelectionPane.selectedVerseProperty().addListener((observable, oldValue, newValue) -> {
            refreshTable();
        });
        setTop(topPane);

        refreshTable();
    }

    private void refreshTable() {
        VerseAdapter selectedVerse = chapterVerseSelectionPane.getSelectedVerse();
        if (selectedVerse == null) {
            return;
        }
        int chapterNumber = selectedVerse.getChapterNumber();
        int verseNumber = selectedVerse.getVerseNumber();
        Verse verse = repositoryTool.getRepositoryUtil().getVerseRepository().
                findByChapterNumberAndVerseNumber(chapterNumber, verseNumber);

        ObservableList items = tableView.getItems();
        items.remove(0, items.size());

        List<Token> tokens = verse.getTokens();

        items.addAll(verse.getTokens().stream().map(TableCellModel::new).collect(Collectors.toList()));

        tableView.requestLayout();
    }
}
