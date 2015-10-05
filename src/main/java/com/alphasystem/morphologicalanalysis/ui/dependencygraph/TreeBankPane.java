package com.alphasystem.morphologicalanalysis.ui.dependencygraph;

import com.alphasystem.morphologicalanalysis.graph.model.DependencyGraph;
import com.alphasystem.morphologicalanalysis.graph.repository.DependencyGraphRepository;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.components.CanvasPane;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.components.ControlPane;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.DependencyGraphAdapter;
import com.alphasystem.morphologicalanalysis.util.RepositoryTool;
import javafx.collections.ObservableList;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.BorderPane;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import static com.alphasystem.util.AppUtil.USER_HOME_DIR;
import static java.lang.String.format;
import static javafx.application.Platform.runLater;
import static javafx.embed.swing.SwingFXUtils.fromFXImage;
import static javafx.scene.Cursor.DEFAULT;
import static javafx.scene.Cursor.WAIT;
import static javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED;
import static javafx.scene.input.KeyCode.*;
import static javafx.scene.input.KeyCombination.ALT_DOWN;
import static javafx.scene.input.KeyCombination.CONTROL_DOWN;

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

    /**
     * pane for canvas
     */
    private CanvasPane canvasPane;

    public TreeBankPane() {
        this(new DependencyGraphAdapter(new DependencyGraph()));
    }

    public TreeBankPane(DependencyGraphAdapter dependencyGraph) {
        setTop(createMenuBar());
        canvasPane = new CanvasPane(dependencyGraph);
        ScrollPane scrollPane = new ScrollPane(canvasPane);
        scrollPane.setHbarPolicy(AS_NEEDED);
        scrollPane.setVbarPolicy(AS_NEEDED);
        setCenter(scrollPane);

        ControlPane controlPane = new ControlPane(dependencyGraph);
        setRight(controlPane);

        // connection between Canvas pane and Control pane
        controlPane.dependencyGraphProperty().bind(canvasPane.dependencyGraphProperty());
    }

    private static String getFileNameWithPadding(int value) {
        String padding = value < 10 ? "00" : (value < 100 ? "0" : "");
        return format("%s%s", padding, value);
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        MenuItem menuItem;
        ObservableList<Menu> menus = menuBar.getMenus();

        Menu menu = new Menu("File");
        menu.setAccelerator(new KeyCodeCombination(F));

        menuItem = new MenuItem("Open ...");
        menuItem.setAccelerator(new KeyCodeCombination(O, CONTROL_DOWN));
        menuItem.setOnAction(event -> {
            // TODO:
            DependencyGraphRepository dependencyGraphRepository = repositoryTool.getRepositoryUtil()
                    .getDependencyGraphRepository();
            DependencyGraph dependencyGraph = dependencyGraphRepository
                    .findByDisplayName("1:1:1:4");
            DependencyGraphAdapter dependencyGraphAdapter = new DependencyGraphAdapter(dependencyGraph);
            canvasPane.setDependencyGraph(dependencyGraphAdapter);
        });
        menu.getItems().add(menuItem);

        menuItem = new MenuItem("Save");
        menuItem.setAccelerator(new KeyCodeCombination(S, CONTROL_DOWN));
        menuItem.setOnAction(event ->
                repositoryTool.saveDependencyGraph(canvasPane.getDependencyGraph().getDependencyGraph()));
        menu.getItems().add(menuItem);

        menus.add(menu);

        menu.getItems().add(new SeparatorMenuItem());

        Menu exportMenu = new Menu("Export");
        menuItem = new MenuItem("PNG ...");
        menuItem.setAccelerator(new KeyCodeCombination(P, CONTROL_DOWN, ALT_DOWN));
        menuItem.setOnAction(event -> {
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
        });
        exportMenu.getItems().add(menuItem);
        menu.getItems().add(exportMenu);

        return menuBar;
    }

    private File getExportFile(String format) {
        DependencyGraph dependencyGraph = canvasPane.getDependencyGraph().getDependencyGraph();
        File parent = new File(WORKING_DIRECTORY, getFileNameWithPadding(dependencyGraph.getChapterNumber()));
        parent = new File(parent, getFileNameWithPadding(dependencyGraph.getVerseNumber()));
        if (!parent.exists()) {
            @SuppressWarnings("unused")
            boolean mkdirs = parent.mkdirs();
        }
        return new File(parent, format("%s_%s.%s", getFileNameWithPadding(dependencyGraph.getFirstTokenIndex()),
                getFileNameWithPadding(dependencyGraph.getLastTokenIndex()), format));
    }

}
