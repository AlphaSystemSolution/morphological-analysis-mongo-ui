package com.alphasystem.morphologicalanalysis.treebank.jfx.ui;

import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.components.CanvasPane;
import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.components.ControlPane;
import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.CanvasMetaData;
import com.alphasystem.morphologicalanalysis.treebank.model.TreeBankData;
import com.alphasystem.svg.SVGTool;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import static javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED;
import static javafx.scene.input.KeyCode.F4;
import static javafx.scene.input.KeyCombination.ALT_DOWN;

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

    public TreeBankPane(TreeBankData treeBankData) {
        super();

        canvasMetaData = new CanvasMetaData();
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
        menu.setAccelerator(new KeyCodeCombination(KeyCode.F));

        menu.getItems().add(new SeparatorMenuItem());

        MenuItem menuItem = new MenuItem("Exit");
        menuItem.setAccelerator(new KeyCodeCombination(F4, ALT_DOWN));
        menuItem.setOnAction(event -> {
            Scene scene = getScene();
            Stage stage = (Stage) scene.getWindow();
            stage.close();
        });
        menu.getItems().add(menuItem);

        menus.add(menu);
        return menuBar;
    }

    private void initPane() {
        canvasPane = new CanvasPane(this.treeBankData, canvasMetaData);
        scrollPane = new ScrollPane(canvasPane);
        scrollPane.setHbarPolicy(AS_NEEDED);
        scrollPane.setVbarPolicy(AS_NEEDED);

        controlPane = new ControlPane(canvasMetaData);

        setCenter(scrollPane);
        setRight(controlPane);
    }
}
