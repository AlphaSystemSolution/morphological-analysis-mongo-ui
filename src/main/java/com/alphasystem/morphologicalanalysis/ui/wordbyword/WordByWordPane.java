package com.alphasystem.morphologicalanalysis.ui.wordbyword;

import com.alphasystem.morphologicalanalysis.common.model.VerseTokensPair;
import com.alphasystem.morphologicalanalysis.graph.model.DependencyGraph;
import com.alphasystem.morphologicalanalysis.ui.common.ChapterVerseSelectionPane;
import com.alphasystem.morphologicalanalysis.ui.common.GraphMetaInfoSelectionDialog;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.TreeBankPane;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.DependencyGraphAdapter;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.GraphMetaInfoAdapter;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.VerseTokenPairGroup;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.CanvasUtil;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.TokenEditorDialog;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.model.TableCellModel;
import com.alphasystem.morphologicalanalysis.util.RepositoryTool;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Verse;
import com.alphasystem.morphologicalanalysis.wordbyword.repository.VerseRepository;
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

import java.util.*;
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

    private static Map<String, List<Token>> cache = new LinkedHashMap<>();
    private final ObjectProperty<Action> action = new SimpleObjectProperty<>();
    private ChapterVerseSelectionPane chapterVerseSelectionPane;
    private TokenEditorDialog tokenEditorDialog;
    private GraphMetaInfoSelectionDialog graphMetaInfoSelectionDialog;
    private TableView<TableCellModel> tableView;

    @SuppressWarnings({"unchecked"})
    public WordByWordPane() {
        tokenEditorDialog = new TokenEditorDialog();
        tokenEditorDialog.setToken(RepositoryTool.getInstance().getTokenByDisplayName("1:1:1"));
        chapterVerseSelectionPane = new ChapterVerseSelectionPane();
        chapterVerseSelectionPane.availableProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                initPane();
            }
        });
        graphMetaInfoSelectionDialog = new GraphMetaInfoSelectionDialog();
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
                    tokenEditorDialog.setToken(token);
                    try {
                        Optional<Token> result = tokenEditorDialog.showAndWait();
                        result.ifPresent(model::setToken);
                    } catch (Exception e) {
                        // do nothing
                    }
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

    private static List<Token> getTokens(VerseTokenPairGroup group) {
        Integer chapterNumber = group.getChapterNumber();
        String key = group.toString();
        List<Token> tokens = cache.get(key);
        if (tokens == null || tokens.isEmpty()) {
            tokens = new ArrayList<>();
            VerseRepository verseRepository = RepositoryTool.getInstance().getRepositoryUtil().getVerseRepository();
            for (VerseTokensPair pair : group.getPairs()) {
                Verse verse = verseRepository.findByChapterNumberAndVerseNumber(chapterNumber,
                        pair.getVerseNumber());
                tokens.addAll(verse.getTokens());
            }
            cache.put(key, tokens);
        }
        return tokens;
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

        Token token = tokens.get(0);
        VerseTokenPairGroup group = new VerseTokenPairGroup();
        group.setChapterNumber(token.getChapterNumber());
        Integer verseNumber = token.getVerseNumber();
        VerseTokensPair pair = new VerseTokensPair(verseNumber);
        pair.setFirstTokenIndex(token.getTokenNumber());
        group.getPairs().add(pair);
        for (int i = 1; i < tokens.size(); i++) {
            token = tokens.get(i);
            if (verseNumber.equals(token.getVerseNumber())) {
                pair.setLastTokenIndex(token.getTokenNumber());
            } else {
                pair.initDisplayName();
                verseNumber = token.getVerseNumber();
                pair = new VerseTokensPair(verseNumber);
                pair.setFirstTokenIndex(token.getTokenNumber());
                group.getPairs().add(pair);
            }
        }
        pair.initDisplayName();

        graphMetaInfoSelectionDialog.setGraphMetaInfo(null);
        Optional<GraphMetaInfoAdapter> result = graphMetaInfoSelectionDialog.showAndWait();
        result.ifPresent(graphMetaInfo -> {
            DependencyGraph dependencyGraph = CanvasUtil.getInstance().createDependencyGraph(group, graphMetaInfo);
            DependencyGraphAdapter dependencyGraphAdapter = new DependencyGraphAdapter(dependencyGraph);
            dependencyGraphAdapter.setGraphMetaInfo(graphMetaInfo);
            Alert alert = new Alert(INFORMATION);
            alert.setContentText(format("Graph Created {%s}", dependencyGraph.getDisplayName()));
            alert.showAndWait().filter(response -> response == OK).ifPresent(response -> {
                items.forEach(tcm -> {
                    if (tcm.isChecked()) {
                        tcm.setChecked(false);
                    }
                });
                openTreeBankApp(dependencyGraphAdapter);
            });
        });
    }

    private void openTreeBankApp(DependencyGraphAdapter dependencyGraphAdapter) {
        Stage stage = new Stage();
        stage.setTitle("Quranic Morphological Dependency Graph Builder");
        Scene scene = new Scene(new TreeBankPane(dependencyGraphAdapter));
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.show();
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

    @SuppressWarnings({"unchecked"})
    private void refreshTable() {
        VerseTokenPairGroup selectedGrroup = chapterVerseSelectionPane.getSelectedVerse();
        if (selectedGrroup == null) {
            return;
        }
        ObservableList items = tableView.getItems();
        items.remove(0, items.size());

        List<Token> tokens = getTokens(selectedGrroup);

        items.addAll(tokens.stream().map(TableCellModel::new).collect(Collectors.toList()));

        tableView.requestLayout();
    }

    public enum Action {
        CREATE_DEPENDENCY_GRAPH
    }
}
