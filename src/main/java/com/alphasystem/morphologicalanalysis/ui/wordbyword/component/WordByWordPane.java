package com.alphasystem.morphologicalanalysis.ui.wordbyword.component;

import com.alphasystem.morphologicalanalysis.graph.model.DependencyGraph;
import com.alphasystem.morphologicalanalysis.ui.common.ChapterVerseSelectionPane;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.TreeBankPane;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.model.TableCellModel;
import com.alphasystem.morphologicalanalysis.util.RepositoryTool;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Verse;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.alphasystem.morphologicalanalysis.ui.common.Global.ARABIC_FONT_MEDIUM_BOLD;
import static java.lang.String.format;
import static javafx.geometry.NodeOrientation.RIGHT_TO_LEFT;
import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.Alert.AlertType.INFORMATION;
import static javafx.scene.control.ButtonType.OK;
import static javafx.scene.control.ContentDisplay.GRAPHIC_ONLY;
import static javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED;
import static javafx.scene.text.TextAlignment.CENTER;


/**
 * @author sali
 */
public class WordByWordPane extends BorderPane {

    private final ObjectProperty<Action> action = new SimpleObjectProperty<>();
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

        actionProperty().addListener((observable, oldValue, newValue) -> performAction(newValue));

        TableColumn descriptionColumn = new TableColumn<>();
        descriptionColumn.setMinWidth(700);
        descriptionColumn.setText("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("morphologicalDescription"));

        TableColumn<TableCellModel, String> tokenColumn = new TableColumn<>();
        tokenColumn.setMinWidth(200);
        tokenColumn.setText("Token");
        tokenColumn.setCellValueFactory(new PropertyValueFactory<>("tokenText"));
        tokenColumn.setCellFactory(column -> new TableCell<TableCellModel, String>() {
            private final Text text;

            {
                setContentDisplay(GRAPHIC_ONLY);
                text = new Text();
                text.setFont(ARABIC_FONT_MEDIUM_BOLD);
                text.setTextAlignment(CENTER);
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
        });

        TableColumn tokenNumberColumn = new TableColumn<>();
        tokenNumberColumn.setMinWidth(200);
        tokenNumberColumn.setText("Token Number");
        tokenNumberColumn.setCellValueFactory(new PropertyValueFactory<>("tokenNumber"));
        tokenNumberColumn.setCellFactory(column -> new TableCell<TableCellModel, String>() {
            private final Text text;

            {
                setContentDisplay(GRAPHIC_ONLY);
                text = new Text();
                text.setFont(ARABIC_FONT_MEDIUM_BOLD);
                text.setTextAlignment(CENTER);
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
        });

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
        tableView.setEditable(true);
        tableView.getColumns().addAll(descriptionColumn, tokenColumn, tokenNumberColumn, checkBoxColumn);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(tableView);
        scrollPane.setVbarPolicy(AS_NEEDED);
        scrollPane.setHbarPolicy(AS_NEEDED);

        setCenter(scrollPane);
    }

    private void performAction(Action action) {
        if (action == null) {
            return;
        }
        switch (action) {
            case CREATE_DEPENDENCY_GRAPH:
                exportTokens();
                break;
        }
    }

    private void exportTokens() {
        ObservableList<TableCellModel> items = tableView.getItems();
        List<Token> tokens = new ArrayList<>();
        items.forEach(tcm -> {
            if (tcm.isChecked()) {
                tokens.add(tcm.getToken());
            }
        });
        if (tokens.isEmpty()) {
            Alert alert = new Alert(ERROR);
            alert.setContentText("No row selected in the table");
            alert.showAndWait();
            return;
        }
        DependencyGraph dependencyGraph = repositoryTool.createDependencyGraph(tokens);
        Alert alert = new Alert(INFORMATION);
        alert.setContentText(format("Graph Created {%s}", dependencyGraph.getDisplayName()));
        alert.showAndWait().filter(response -> response == OK).ifPresent(response -> {
            items.forEach(tcm -> {
                if (tcm.isChecked()) {
                    tcm.setChecked(false);
                }
            });
            Stage stage = new Stage();
            Scene scene = new Scene(new TreeBankPane());
            stage.setMaximized(true);
            stage.setScene(scene);
            stage.show();
        });

    }

    public final void setAction(Action action) {
        this.action.set(action);
    }

    public final ObjectProperty<Action> actionProperty() {
        return action;
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
        Verse selectedVerse = chapterVerseSelectionPane.getSelectedVerse();
        if (selectedVerse == null) {
            return;
        }
        int chapterNumber = selectedVerse.getChapterNumber();
        int verseNumber = selectedVerse.getVerseNumber();
        Verse verse = repositoryTool.getRepositoryUtil().getVerseRepository().
                findByChapterNumberAndVerseNumber(chapterNumber, verseNumber);

        ObservableList items = tableView.getItems();
        items.remove(0, items.size());

        items.addAll(verse.getTokens().stream().map(TableCellModel::new).collect(Collectors.toList()));

        tableView.requestLayout();
    }

    public enum Action {
        CREATE_DEPENDENCY_GRAPH
    }
}
