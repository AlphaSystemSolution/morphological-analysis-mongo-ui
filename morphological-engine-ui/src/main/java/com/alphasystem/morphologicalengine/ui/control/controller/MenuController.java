package com.alphasystem.morphologicalengine.ui.control.controller;

import de.jensd.fx.glyphs.GlyphIcons;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

import static de.jensd.fx.glyphs.GlyphsDude.setIcon;
import static de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.CLONE;
import static de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.REMOVE;
import static de.jensd.fx.glyphs.materialicons.MaterialIcon.ADD_BOX;
import static de.jensd.fx.glyphs.materialicons.MaterialIcon.SETTINGS_APPLICATIONS;
import static javafx.scene.input.KeyCode.A;
import static javafx.scene.input.KeyCode.C;
import static javafx.scene.input.KeyCode.Q;
import static javafx.scene.input.KeyCode.R;
import static javafx.scene.input.KeyCode.S;
import static javafx.scene.input.KeyCode.W;
import static javafx.scene.input.KeyCombination.ALT_DOWN;
import static javafx.scene.input.KeyCombination.SHORTCUT_DOWN;

/**
 * @author sali
 */
final class MenuController {

    private static MenuController instance;
    private final MorphologicalEngineController pane;

    /**
     * Do not let any one instantiate this class
     *
     * @param pane main control which
     */
    private MenuController(MorphologicalEngineController pane) {
        this.pane = pane;
    }

    static synchronized MenuController getInstance(MorphologicalEngineController pane) {
        if (instance == null) {
            instance = new MenuController(pane);
        }
        return instance;
    }

    private Button createButton(String tooltip, GlyphIcons icon, EventHandler<ActionEvent> event) {
        Button button = new Button();
        button.setTooltip(new Tooltip(tooltip));
        setIcon(button, icon, "2em");
        button.setOnAction(event);
        return button;
    }

    private MenuItem createMenuItem(String text, GlyphIcons icon, String iconSize, KeyCombination accelerator, EventHandler<ActionEvent> action) {
        MenuItem menuItem = new MenuItem(text);
        if (icon != null) {
            setIcon(menuItem, icon, iconSize);
        }
        if (accelerator != null) {
            menuItem.setAccelerator(accelerator);
        }
        menuItem.setOnAction(action);
        return menuItem;
    }

    private MenuItem createMenuItem(String text, GlyphIcons icon, KeyCombination accelerator, EventHandler<ActionEvent> action) {
        return createMenuItem(text, icon, "1em", accelerator, action);
    }

    private Button createNewButton() {
        return createButton("Create New File", FontAwesomeIcon.FILE_ALT, event -> pane.newAction());
    }

    private Button createOpenButton() {
        return createButton("Open File", FontAwesomeIcon.FOLDER_OPEN_ALT, event -> pane.openAction());
    }

    private Button createAddRowButton() {
        return createButton("Add new Row", ADD_BOX, event -> pane.addNewRowAction());
    }

    private Button createCloneRowsButton() {
        return createButton("Duplicate Selected Row(s)", CLONE, event -> pane.duplicateRowAction());
    }

    private Button createRemoveRowsButton() {
        return createButton("Remove Selected Row", REMOVE, event -> pane.removeRowAction());
    }

    private Button createSettingsButton() {
        return createButton("View / Edit Chart Configuration", SETTINGS_APPLICATIONS, event -> pane.updateChartConfiguration());
    }

    private MenuItem createNewMenuItem() {
        return createMenuItem("New", FontAwesomeIcon.FILE_ALT, "1.5em", new KeyCodeCombination(KeyCode.N, SHORTCUT_DOWN), event -> pane.newAction());
    }

    private MenuItem createOpenMenuItem() {
        return createMenuItem("Open", FontAwesomeIcon.FOLDER_OPEN_ALT, "1.5em", new KeyCodeCombination(KeyCode.O, SHORTCUT_DOWN), event -> pane.openAction());
    }

    private MenuItem createSaveMenuItem() {
        return createMenuItem("Save ...", FontAwesomeIcon.SAVE, "1.5em", new KeyCodeCombination(S, SHORTCUT_DOWN), event -> pane.saveAction());
    }

    private MenuItem createSaveAsMenuItem() {
        return createMenuItem("Save As ...", null, new KeyCodeCombination(S, SHORTCUT_DOWN, ALT_DOWN), event -> pane.saveAsAction());
    }

    private MenuItem createSaveSelectedMenuItem() {
        return createMenuItem("Save Selected ...", null, null, event -> pane.saveSelectedAction());
    }

    private MenuItem createExportToWordMenuItem() {
        return createMenuItem("Export to Word", FontAwesomeIcon.FILE_WORD_ALT, "1.5em",
                new KeyCodeCombination(KeyCode.W, SHORTCUT_DOWN, ALT_DOWN), event -> pane.exportToWordAction());
    }

    private MenuItem createExportSelectedToWordMenuItem() {
        return createMenuItem("Export Selected to Word", FontAwesomeIcon.FILE_WORD_ALT, "1.5em", null,
                event -> pane.exportSelectedToWordAction());
    }

    private MenuItem createExportAbbreviatedConjugationsToWord() {
        return createMenuItem("Export Abbreviated Conjugations to Word", FontAwesomeIcon.FILE_WORD_ALT, "1.5em", null,
                event -> pane.exportAbbreviatedConjugationsToWordAction());
    }

    private MenuItem createExportDetailConjugationsToWord() {
        return createMenuItem("Export Detail Conjugations to Word", FontAwesomeIcon.FILE_WORD_ALT, "1.5em", null,
                event -> pane.exportDetailedConjugationsToWordAction());
    }

    private MenuItem createAddRowMenuItem() {
        return createMenuItem("Add new Row", ADD_BOX, "1.5em", new KeyCodeCombination(A, SHORTCUT_DOWN, ALT_DOWN), event -> pane.addNewRowAction());
    }

    private MenuItem createCloneRowsMenuItem() {
        return createMenuItem("Duplicate Selected Row(s)", CLONE, "1.5em", new KeyCodeCombination(C, SHORTCUT_DOWN, ALT_DOWN), event -> pane.duplicateRowAction());
    }

    private MenuItem createRemoveRowsMenuItem() {
        return createMenuItem("Remove Selected Row", REMOVE, "1.5em", new KeyCodeCombination(R, SHORTCUT_DOWN, ALT_DOWN), event -> pane.removeRowAction());
    }

    private MenuItem createCloseMenuItem() {
        return createMenuItem("Close", null, new KeyCodeCombination(W, SHORTCUT_DOWN), event -> pane.closeAction());
    }

    private MenuItem createExitMenuItem() {
        return createMenuItem("Exit", null, new KeyCodeCombination(Q, SHORTCUT_DOWN), event -> pane.exitAction());
    }

    private MenuButton createSaveMenu() {
        MenuButton menuButton = new MenuButton("Save");
        setIcon(menuButton, FontAwesomeIcon.SAVE, "2em");
        menuButton.getItems().addAll(createSaveMenuItem(), createSaveAsMenuItem(), createSaveSelectedMenuItem());
        return menuButton;
    }

    private MenuButton createExportMenu() {
        MenuButton menuButton = new MenuButton();
        setIcon(menuButton, MaterialDesignIcon.EXPORT, "2em");
        menuButton.setTooltip(new Tooltip("Export Conjugation to external format"));
        menuButton.getItems().addAll(createExportToWordMenuItem(), createExportSelectedToWordMenuItem(),
                createExportAbbreviatedConjugationsToWord(), createExportDetailConjugationsToWord());
        return menuButton;
    }

    MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.setUseSystemMenuBar(true);

        Menu fileMenu = new Menu("File");
        fileMenu.getItems().addAll(createNewMenuItem(), createOpenMenuItem(), new SeparatorMenuItem(), createSaveMenuItem(),
                createSaveAsMenuItem(), createSaveSelectedMenuItem(), new SeparatorMenuItem(), createCloseMenuItem(), createExitMenuItem());

        Menu tableMenu = new Menu("Table");
        tableMenu.getItems().addAll(createAddRowMenuItem(), createCloneRowsMenuItem(), createRemoveRowsMenuItem());

        menuBar.getMenus().addAll(fileMenu, tableMenu);

        return menuBar;
    }

    ToolBar createToolBar() {
        ToolBar toolBar = new ToolBar();
        toolBar.getItems().addAll(createNewButton(), createOpenButton(), createSaveMenu(), createExportMenu(), new Separator(),
                createAddRowButton(), createCloneRowsButton(), createRemoveRowsButton(), new Separator(), createSettingsButton());
        return toolBar;
    }

}
