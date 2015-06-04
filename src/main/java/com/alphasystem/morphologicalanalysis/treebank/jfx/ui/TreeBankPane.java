package com.alphasystem.morphologicalanalysis.treebank.jfx.ui;

import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.components.CanvasPane;
import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.components.ControlPane;
import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.components.NodeSelectionDialog;
import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.CanvasData;
import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.CanvasMetaData;
import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.GraphNode;
import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.util.GraphBuilder;
import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.util.SerializationTool;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
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

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.alphasystem.morphologicalanalysis.treebank.jfx.ui.util.SerializationTool.MDG_EXTENSION_ALL;
import static com.alphasystem.util.AppUtil.CURRENT_USER_DIR;
import static java.lang.String.format;
import static javafx.application.Platform.runLater;
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

    /**
     * pane for canvas
     */
    private CanvasPane canvasPane;

    /**
     * control pane
     */
    private ControlPane controlPane;

    private ScrollPane scrollPane;

    private NodeSelectionDialog dialog;

    private GraphBuilder graphBuilder = GraphBuilder.getInstance();

    private SerializationTool serializationTool = SerializationTool.getInstance();

    private FileChooser fileChooser = new FileChooser();

    private FileChooser imageFileChooser = new FileChooser();

    private File currentFile;

    public TreeBankPane() {
        super();

        dialog = new NodeSelectionDialog();
        setTop(createMenuBar());
        initPane(null);
        fileChooser.setInitialDirectory(CURRENT_USER_DIR);
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter(format("Morphology Dependency Graph file (%s)", MDG_EXTENSION_ALL),
                        MDG_EXTENSION_ALL));
        imageFileChooser = new FileChooser();
        imageFileChooser.setInitialDirectory(CURRENT_USER_DIR);
        imageFileChooser.getExtensionFilters().addAll(new ExtensionFilter("PNG", "*.png"));
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        ObservableList<Menu> menus = menuBar.getMenus();

        Menu menu = new Menu("File");
        menu.setAccelerator(new KeyCodeCombination(F));

        MenuItem menuItem = null;

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
                runLater(() -> {
                    saveFile();
                });
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

        Menu subMenu = new Menu("Operations");

        menuItem = new MenuItem("Import Tokens ...");
        menuItem.setAccelerator(new KeyCodeCombination(I, CONTROL_DOWN));
        menuItem.setOnAction(event -> {
            getScene().setCursor(WAIT);
            runLater(() -> {
                Optional<List<Token>> result = dialog.showAndWait();
                result.ifPresent(selectedItems -> {
                    updateCanvas(graphBuilder.toGraphNodes(selectedItems));
                });

            });
        });
        subMenu.getItems().add(menuItem);
        items.add(subMenu);

        subMenu = new Menu("Export");

        menuItem = new MenuItem("PNG ...");
        menuItem.setAccelerator(new KeyCodeCombination(P, CONTROL_DOWN, ALT_DOWN));
        menuItem.setOnAction(event -> {
            getScene().setCursor(WAIT);
            runLater(() -> {
                imageFileChooser.setInitialDirectory(fileChooser.getInitialDirectory());
                File file = imageFileChooser.showSaveDialog(getStage());
                if (file != null) {
                    WritableImage writableImage = canvasPane.getCanvasPane().snapshot(new SnapshotParameters(), null);
                    try {
                        ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
                        Desktop.getDesktop().open(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                getScene().setCursor(DEFAULT);
            });
        });
        subMenu.getItems().add(menuItem);
        items.add(subMenu);


        items.add(new SeparatorMenuItem());

        menuItem = new MenuItem("Exit");
        menuItem.setAccelerator(new KeyCodeCombination(F4, ALT_DOWN));
        menuItem.setOnAction(event -> {
            getStage().close();
        });
        items.add(menuItem);

        menus.add(menu);
        return menuBar;
    }

    private void saveFile() {
        serializationTool.save(currentFile, null, canvasPane.canvasDataObjectProperty().get());
        fileChooser.setInitialDirectory(currentFile.getParentFile());
        setCursor(DEFAULT);
    }

    private Stage getStage() {
        Scene scene = getScene();
        return (Stage) scene.getWindow();
    }

    private void updateCanvas(ObservableList<GraphNode> nodes) {
        ObjectProperty<CanvasData> canvasDataObjectProperty = canvasPane.canvasDataObjectProperty();
        CanvasData canvasData = canvasDataObjectProperty.get();
        canvasData.setNodes(nodes);
        // when we are not setting null we are not getting any updates
        // so set null first then set new value
        canvasDataObjectProperty.set(null);
        canvasDataObjectProperty.set(canvasData);
        getScene().setCursor(DEFAULT);
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
