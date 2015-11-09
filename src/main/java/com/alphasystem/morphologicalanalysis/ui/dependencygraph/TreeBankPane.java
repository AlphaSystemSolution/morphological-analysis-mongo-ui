package com.alphasystem.morphologicalanalysis.ui.dependencygraph;

import com.alphasystem.morphologicalanalysis.common.model.VerseTokensPair;
import com.alphasystem.morphologicalanalysis.graph.model.DependencyGraph;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.components.CanvasPane;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.components.ControlPane;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.components.DependencyGraphSelectionDialog;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.DependencyGraphAdapter;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.GraphMetaInfoAdapter;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.GraphNodeAdapter;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.TerminalNodeAdapter;
import com.alphasystem.morphologicalanalysis.util.RepositoryTool;
import javafx.collections.ObservableList;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static com.alphasystem.morphologicalanalysis.ui.common.Global.fromFont;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.isTerminal;
import static com.alphasystem.util.AppUtil.USER_HOME_DIR;
import static java.lang.String.format;
import static javafx.application.Platform.runLater;
import static javafx.embed.swing.SwingFXUtils.fromFXImage;
import static javafx.scene.Cursor.DEFAULT;
import static javafx.scene.Cursor.WAIT;
import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.Alert.AlertType.WARNING;
import static javafx.scene.control.ButtonType.NO;
import static javafx.scene.control.ButtonType.YES;
import static javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED;
import static javafx.scene.input.KeyCode.*;
import static javafx.scene.input.KeyCombination.*;

/**
 * @author sali
 */
public class TreeBankPane extends BorderPane {

    private static final String PNG_FORMATE = "png";
    private static final File WORKING_DIRECTORY = new File(USER_HOME_DIR, ".treebank");

    static {
        if (!WORKING_DIRECTORY.exists()) {
            @SuppressWarnings("unused")
            boolean mkdirs = WORKING_DIRECTORY.mkdirs();
        }
    }

    private RepositoryTool repositoryTool = RepositoryTool.getInstance();
    private DependencyGraphSelectionDialog dependencyGraphSelectionDialog;
    private Menu operationMenu;
    private Menu dependencyGraphTreeMenu;

    /**
     * pane for canvas
     */
    private CanvasPane canvasPane;

    @SuppressWarnings({"unused"})
    public TreeBankPane() {
        this(null);
    }

    public TreeBankPane(DependencyGraphAdapter dependencyGraph) {
        if (dependencyGraph == null) {
            dependencyGraph = new DependencyGraphAdapter(new DependencyGraph());
        }
        setTop(createMenuBar());
        dependencyGraphSelectionDialog = new DependencyGraphSelectionDialog();
        canvasPane = new CanvasPane(dependencyGraph);
        ScrollPane scrollPane = new ScrollPane(canvasPane);
        scrollPane.setHbarPolicy(AS_NEEDED);
        scrollPane.setVbarPolicy(AS_NEEDED);
        setCenter(scrollPane);

        ControlPane controlPane = new ControlPane(dependencyGraph);
        setRight(controlPane);

        // connection between Canvas pane and Control pane
        controlPane.dependencyGraphProperty().bind(canvasPane.dependencyGraphProperty());
        canvasPane.dependencyGraphProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                canvasPane.dependencyGraphProperty().get().selectedNodeProperty().addListener(
                        (observable1, oldValue1, newValue1) -> {
                            canvasPane.updateOperations(newValue1, operationMenu);
                        });
            }
        });

    }

    private static String getFileNameWithPadding(int value) {
        String padding = value < 10 ? "00" : (value < 100 ? "0" : "");
        return format("%s%s", padding, value);
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        menuBar.getMenus().add(createFileMenu());

        operationMenu = new Menu("Operation");
        dependencyGraphTreeMenu = new Menu("Dependency Graph Tree");
        menuBar.getMenus().addAll(operationMenu, dependencyGraphTreeMenu);

        return menuBar;
    }

    private Menu createFileMenu() {
        MenuItem menuItem;
        Menu menu = new Menu("File");
        menu.setAccelerator(new KeyCodeCombination(F));

        menuItem = new MenuItem("Open ...");
        menuItem.setAccelerator(new KeyCodeCombination(O, CONTROL_DOWN));
        menuItem.setOnAction(event -> openAction());
        menu.getItems().add(menuItem);

        menuItem = new MenuItem("Save");
        menuItem.setAccelerator(new KeyCodeCombination(S, CONTROL_DOWN));
        menuItem.setOnAction(event -> saveAction());
        menu.getItems().add(menuItem);

        menuItem = new MenuItem("Delete");
        menuItem.setAccelerator(new KeyCodeCombination(D, CONTROL_DOWN));
        menuItem.setOnAction(event -> deleteAction());
        menu.getItems().add(menuItem);

        menu.getItems().add(new SeparatorMenuItem());

        Menu exportMenu = new Menu("Export");
        menuItem = new MenuItem("PNG ...");
        menuItem.setAccelerator(new KeyCodeCombination(P, CONTROL_DOWN, ALT_DOWN));
        menuItem.setOnAction(event -> exportToPngAction());
        exportMenu.getItems().add(menuItem);
        menu.getItems().add(exportMenu);

        menu.getItems().add(new SeparatorMenuItem());

        menuItem = new MenuItem("Exit");
        menuItem.setAccelerator(new KeyCodeCombination(F4, SHORTCUT_DOWN));
        menuItem.setOnAction(event -> exitAction());
        menu.getItems().add(menuItem);
        return menu;
    }

    private void openAction() {
        Optional<DependencyGraph> result = dependencyGraphSelectionDialog.showAndWait();
        result.ifPresent(this::updateCanvas);
    }

    private void updateCanvas(DependencyGraph dependencyGraph) {
        DependencyGraphAdapter dependencyGraphAdapter = new DependencyGraphAdapter(dependencyGraph);
        GraphMetaInfoAdapter graphMetaInfo = dependencyGraphAdapter.getGraphMetaInfo();
        for (GraphNodeAdapter nodeAdapter : dependencyGraphAdapter.getGraphNodes()) {
            if (isTerminal(nodeAdapter)) {
                TerminalNodeAdapter terminalNodeAdapter = (TerminalNodeAdapter) nodeAdapter;
                graphMetaInfo.setTerminalFont(fromFont(terminalNodeAdapter.getFont()));
                graphMetaInfo.setTranslationFont(fromFont(terminalNodeAdapter.getTranslationFont()));
                graphMetaInfo.setPosFont(fromFont(terminalNodeAdapter.getPartOfSpeeches().get(0).getFont()));
                break;
            }
        }
        canvasPane.setDependencyGraph(dependencyGraphAdapter);
        ObservableList<GraphNodeAdapter> graphNodes = dependencyGraphAdapter.getGraphNodes();
        if (!graphNodes.isEmpty()) {
            canvasPane.getDependencyGraph().setSelectedNode(graphNodes.get(0));
        }
    }

    private void saveAction() {
        try {
            repositoryTool.saveDependencyGraph(canvasPane.getDependencyGraph().getDependencyGraph(),
                    canvasPane.getImpliedOrHiddenTokens(), canvasPane.getRemovalIdMap());
            canvasPane.getImpliedOrHiddenTokens().clear();
            canvasPane.getRemovalIdMap().clear();
        } catch (Exception e) {
            Alert alert = new Alert(ERROR);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void deleteAction() {
        Alert alert = new Alert(WARNING, "Are you sure?", YES, NO);
        Optional<ButtonType> result = alert.showAndWait();
        result.ifPresent(buttonType -> {
            if (buttonType.getButtonData().isDefaultButton()) {
                DependencyGraph dependencyGraph = canvasPane.getDependencyGraph().getDependencyGraph();
                canvasPane.removeAll();
                repositoryTool.deleteDependencyGraph(dependencyGraph.getId(), canvasPane.getRemovalIdMap());
                canvasPane.getRemovalIdMap().clear();
                updateCanvas(new DependencyGraph());
            }
        });

    }

    private void exitAction() {
        Stage stage = (Stage) getScene().getWindow();
        stage.close();
    }

    private void exportToPngAction() {
        getScene().setCursor(WAIT);
        runLater(() -> {
            File file = getExportFile(PNG_FORMATE);
            WritableImage writableImage = canvasPane.getCanvasPane().snapshot(new SnapshotParameters(), null);
            try {
                ImageIO.write(fromFXImage(writableImage, null), PNG_FORMATE, file);
                Desktop.getDesktop().open(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            getScene().setCursor(DEFAULT);
        });
    }

    private File getExportFile(String format) {
        DependencyGraph dependencyGraph = canvasPane.getDependencyGraph().getDependencyGraph();
        File parent = new File(WORKING_DIRECTORY, getFileNameWithPadding(dependencyGraph.getChapterNumber()));
        java.util.List<VerseTokensPair> tokens = dependencyGraph.getTokens();
        VerseTokensPair tokenInfo = tokens.get(0);
        parent = new File(parent, getFileNameWithPadding(tokenInfo.getVerseNumber()));
        if (!parent.exists()) {
            parent.mkdirs();
        }
        return new File(parent, format("%s_%s.%s", getFileNameWithPadding(tokenInfo.getFirstTokenIndex()),
                getFileNameWithPadding(tokenInfo.getLastTokenIndex()), format));
    }

}
