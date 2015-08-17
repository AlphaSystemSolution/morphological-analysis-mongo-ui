package com.alphasystem.morphologicalanalysis.ui.dependencygraph;

import com.alphasystem.morphologicalanalysis.graph.model.DependencyGraph;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.components.CanvasPane;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.components.ControlPane;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.CanvasData;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.CanvasMetaData;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.GraphBuilder;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.SVGExport;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.SerializationTool;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import static com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.SerializationTool.MDG_EXTENSION_ALL;
import static com.alphasystem.util.AppUtil.CURRENT_USER_DIR;
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
    private static final String SVG_FORMAT = "svg";
    /**
     * pane for canvas
     */
    private CanvasPane canvasPane;

    /**
     * control pane
     */
    private ControlPane controlPane;

    private ScrollPane scrollPane;

    private GraphBuilder graphBuilder = GraphBuilder.getInstance();

    private SerializationTool serializationTool = SerializationTool.getInstance();

    private FileChooser fileChooser = new FileChooser();

    private File currentFile;

    public TreeBankPane() {
        this(null, new CanvasMetaData());
    }

    public TreeBankPane(DependencyGraph dependencyGraph, CanvasMetaData canvasMetaData) {
        super();

        setTop(createMenuBar());
        initPane(createFromGraph(dependencyGraph, canvasMetaData));
        fileChooser.setInitialDirectory(CURRENT_USER_DIR);
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter(format("Morphology Dependency Graph file (%s)", MDG_EXTENSION_ALL),
                        MDG_EXTENSION_ALL));
    }

    private CanvasData createFromGraph(DependencyGraph dependencyGraph, CanvasMetaData canvasMetaData) {
        CanvasData canvasData = new CanvasData(canvasMetaData);
        if (dependencyGraph != null) {
            canvasData.setDependencyGraph(dependencyGraph);
            graphBuilder.set(canvasMetaData);
            canvasData.setNodes(graphBuilder.toGraphNodes(dependencyGraph));
        }
        return canvasData;
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        ObservableList<Menu> menus = menuBar.getMenus();

        Menu menu = new Menu("File");
        menu.setAccelerator(new KeyCodeCombination(F));

        MenuItem menuItem;

        ObservableList<MenuItem> items = menu.getItems();

        menuItem = new MenuItem("Open ...");
        menuItem.setAccelerator(new KeyCodeCombination(O, CONTROL_DOWN));
        menuItem.setOnAction(event -> {
            getScene().setCursor(WAIT);
            File file = fileChooser.showOpenDialog(getStage());
            if (file != null) {
                currentFile = file;
                fileChooser.setInitialDirectory(file.getParentFile());
                CanvasData savedCanvasData = serializationTool.open(file);
                getChildren().removeAll(scrollPane, controlPane);
                initPane(savedCanvasData);
                requestLayout();
                getScene().setCursor(DEFAULT);
            } else {
                getScene().setCursor(DEFAULT);
            }

        });
        items.add(menuItem);

        menuItem = new MenuItem("Save ...");
        menuItem.setAccelerator(new KeyCodeCombination(S, CONTROL_DOWN));
        menuItem.setOnAction(event -> {
            setCursor(WAIT);
            if (currentFile == null) {
                currentFile = fileChooser.showSaveDialog(getStage());
            }
            if (currentFile != null) {
                runLater(this::saveFile);
            } else {
                setCursor(DEFAULT);
            }
        });
        items.add(menuItem);

        menuItem = new MenuItem("Save As ...");
        menuItem.setAccelerator(new KeyCodeCombination(S, CONTROL_DOWN, ALT_DOWN));
        menuItem.setOnAction(event -> {
            setCursor(WAIT);
            File file = fileChooser.showSaveDialog(getStage());
            if (file != null) {
                currentFile = file;
                saveFile();
            } else {
                setCursor(DEFAULT);
            }
        });
        items.add(menuItem);

        items.add(new SeparatorMenuItem());

        Menu subMenu;
        // subMenu = new Menu("Operations");

        //items.add(subMenu);

        subMenu = new Menu("Export");

        menuItem = new MenuItem("PNG ...");
        menuItem.setAccelerator(new KeyCodeCombination(P, CONTROL_DOWN, ALT_DOWN));
        menuItem.setOnAction(event -> {
            getScene().setCursor(WAIT);
            runLater(() -> {
                File file = getExportFile(PNG_FORMATE);
                if (file != null) {
                    WritableImage writableImage = canvasPane.getCanvasPane().snapshot(new SnapshotParameters(), null);
                    try {
                        ImageIO.write(fromFXImage(writableImage, null), PNG_FORMATE, file);
                        Desktop.getDesktop().open(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                getScene().setCursor(DEFAULT);
            });
        });
        subMenu.getItems().add(menuItem);

        menuItem = new MenuItem("SVG ...");
        menuItem.setAccelerator(new KeyCodeCombination(S, CONTROL_DOWN, ALT_DOWN));
        menuItem.setOnAction(event -> {
            File svgFile = getExportFile(SVG_FORMAT);
            if (svgFile != null) {
                SVGExport.export(canvasPane.canvasDataObjectProperty().get().getCanvasMetaData(),
                        canvasPane.getCanvasPane(), svgFile);
            }
        });
        subMenu.getItems().add(menuItem);
        items.add(subMenu);


        items.add(new SeparatorMenuItem());

        menuItem = new MenuItem("Exit");
        menuItem.setAccelerator(new KeyCodeCombination(F4, ALT_DOWN));
        menuItem.setOnAction(event -> getStage().close());
        items.add(menuItem);

        menus.add(menu);
        return menuBar;
    }

    private File getExportFile(String ext) {
        if (currentFile == null) {
            return null;
        }
        File parentFolder = currentFile.getParentFile();
        String baseName = FilenameUtils.getBaseName(currentFile.getAbsolutePath());
        return new File(parentFolder, format("%s.%s", baseName, ext));
    }

    private void saveFile() {
        serializationTool.save(currentFile, canvasPane.canvasDataObjectProperty().get());
        fileChooser.setInitialDirectory(currentFile.getParentFile());
        setCursor(DEFAULT);
    }

    private Stage getStage() {
        Scene scene = getScene();
        return (Stage) scene.getWindow();
    }

    private void initPane(CanvasData cd) {
        CanvasData canvasData = cd == null ? new CanvasData(new CanvasMetaData()) : cd;

        canvasPane = new CanvasPane(canvasData);
        scrollPane = new ScrollPane(canvasPane);
        scrollPane.setHbarPolicy(AS_NEEDED);
        scrollPane.setVbarPolicy(AS_NEEDED);

        controlPane = new ControlPane(canvasData);

        // connection between Canvas pane and Control pane
        // NOTE: binding is bidirectional
        controlPane.canvasDataObjectProperty().bind(canvasPane.canvasDataObjectProperty());

        setCenter(scrollPane);
        setRight(controlPane);
    }

}
