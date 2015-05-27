package com.alphasystem.morphologicalanalysis.treebank.jfx.ui;

import com.alphasystem.morphologicalanalysis.model.Token;
import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.components.CanvasPane;
import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.components.ControlPane;
import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.components.NodeSelectionDialog;
import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.CanvasData;
import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.CanvasMetaData;
import com.alphasystem.morphologicalanalysis.treebank.model.TreeBankData;
import com.alphasystem.svg.SVGTool;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

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

    private TreeBankData treeBankData;

    private CanvasMetaData canvasMetaData;

    private CanvasData canvasData;

    private NodeSelectionDialog dialog;

    public TreeBankPane(TreeBankData treeBankData) {
        super();

        dialog = new NodeSelectionDialog();
        canvasMetaData = new CanvasMetaData();
        canvasData = new CanvasData();
        setTop(createMenuBar());
        this.treeBankData = treeBankData == null ? SVGTool.getInstance().createTreeBankData() : treeBankData;
        initPane();
    }

    public TreeBankPane() {
        this(null);
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        ObservableList<Menu> menus = menuBar.getMenus();

        Menu menu = new Menu("File");
        menu.setAccelerator(new KeyCodeCombination(F));

        ObservableList<MenuItem> items = menu.getItems();

        MenuItem menuItem = new MenuItem("Import Tokens");
        menuItem.setAccelerator(new KeyCodeCombination(I, CONTROL_DOWN));
        menuItem.setOnAction(event -> {
            Global.getInstance().getGlobalScene().setCursor(WAIT);
            Platform.runLater(() -> {
                Optional<List<Token>> result = dialog.showAndWait();
                result.ifPresent(selectedItems -> {
                    Global.getInstance().getGlobalScene().setCursor(DEFAULT);
                });

            });
        });
        items.add(menuItem);

        items.add(new SeparatorMenuItem());

        menuItem = new MenuItem("Exit");
        menuItem.setAccelerator(new KeyCodeCombination(F4, ALT_DOWN));
        menuItem.setOnAction(event -> {
            Scene scene = getScene();
            Stage stage = (Stage) scene.getWindow();
            stage.close();
        });
        items.add(menuItem);

        menus.add(menu);
        return menuBar;
    }

    private void initPane() {
        canvasPane = new CanvasPane(this.treeBankData, canvasMetaData);
        scrollPane = new ScrollPane(canvasPane);
        scrollPane.setHbarPolicy(AS_NEEDED);
        scrollPane.setVbarPolicy(AS_NEEDED);

        controlPane = new ControlPane(canvasData, canvasMetaData);

        setCenter(scrollPane);
        setRight(controlPane);
    }


    // TODO: remove this
    // Just for testing
    private CanvasData loadData(List<Token> tokens) {
        CanvasData canvasData = new CanvasData();

        return canvasData;
    }
}
