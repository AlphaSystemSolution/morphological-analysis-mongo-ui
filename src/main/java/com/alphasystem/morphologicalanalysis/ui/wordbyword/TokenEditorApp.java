package com.alphasystem.morphologicalanalysis.ui.wordbyword;

import com.alphasystem.morphologicalanalysis.util.SpringContextHelper;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.alphasystem.morphologicalanalysis.ui.common.Global.DEFAULT_DICTIONARY_DIRECTORY;
import static com.alphasystem.util.nio.NIOFileUtils.copyDir;
import static de.jensd.fx.glyphs.GlyphsDude.setIcon;
import static de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.*;
import static javafx.scene.input.KeyCode.*;
import static javafx.scene.input.KeyCombination.ALT_DOWN;
import static javafx.scene.input.KeyCombination.SHORTCUT_DOWN;

/**
 * @author sali
 */
public class TokenEditorApp extends Application {

    static {
        if (!DEFAULT_DICTIONARY_DIRECTORY.exists()) {
            DEFAULT_DICTIONARY_DIRECTORY.mkdirs();

            try {
                copyDir(DEFAULT_DICTIONARY_DIRECTORY.toPath(), "asciidoctor", TokenEditorApp.class);
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }

        SpringContextHelper.getInstance();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Quranic Morphological Word By Word Token Editor");
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());

        TokenEditorPane tokenEditorPane = new TokenEditorPane();

        BorderPane pane = new BorderPane();
        pane.setCenter(tokenEditorPane);
        pane.setTop(createTopPane(primaryStage, tokenEditorPane));
        Scene scene = new Scene(pane);
        scene.getStylesheets().addAll("/styles/glyphs_custom.css");
        primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createTopPane(final Stage stage, final TokenEditorPane tokenEditorPane) {
        VBox vBox = new VBox();
        vBox.getChildren().addAll(createMenuBar(stage, tokenEditorPane), createToolBar(tokenEditorPane));
        return vBox;
    }

    private ToolBar createToolBar(final TokenEditorPane tokenEditorPane) {
        ToolBar toolBar = new ToolBar();

        Button toolbarButton;

        toolbarButton = new Button();
        toolbarButton.setTooltip(new Tooltip("Save current token"));
        setIcon(toolbarButton, SAVE, "2em");
        toolbarButton.disableProperty().bind(tokenEditorPane.token().isNull());
        toolbarButton.setOnAction(event -> tokenEditorPane.saveToken());
        toolBar.getItems().add(toolbarButton);

        toolbarButton = new Button();
        toolbarButton.setTooltip(new Tooltip("Go to next token"));
        setIcon(toolbarButton, ANGLE_DOUBLE_RIGHT, "2em");
        toolbarButton.disableProperty().bind(tokenEditorPane.nextToken().isNull());
        toolbarButton.setOnAction(event -> tokenEditorPane.loadNextToken());
        toolBar.getItems().add(toolbarButton);

        toolbarButton = new Button();
        toolbarButton.setTooltip(new Tooltip("Go to previous token"));
        setIcon(toolbarButton, ANGLE_DOUBLE_LEFT, "2em");
        toolbarButton.disableProperty().bind(tokenEditorPane.previousToken().isNull());
        toolbarButton.setOnAction(event -> tokenEditorPane.loadPreviousToken());
        toolBar.getItems().add(toolbarButton);

        return toolBar;
    }

    private MenuBar createMenuBar(final Stage stage, final TokenEditorPane tokenEditorPane) {
        MenuBar menuBar = new MenuBar();

        MenuItem menuItem;

        Menu menu = new Menu("File");
        menu.setAccelerator(new KeyCodeCombination(F));

        menuItem = new MenuItem("Save");
        setIcon(menuItem, SAVE);
        menuItem.disableProperty().bind(tokenEditorPane.token().isNull());
        menuItem.setAccelerator(new KeyCodeCombination(S, SHORTCUT_DOWN));
        menuItem.setOnAction(event -> tokenEditorPane.saveToken());
        menu.getItems().add(menuItem);

        menuItem = new MenuItem("Next");
        setIcon(menuItem, ANGLE_DOUBLE_RIGHT);
        menuItem.disableProperty().bind(tokenEditorPane.nextToken().isNull());
        menuItem.setAccelerator(new KeyCodeCombination(RIGHT, SHORTCUT_DOWN));
        menuItem.setOnAction(event -> tokenEditorPane.loadNextToken());
        menu.getItems().add(menuItem);

        menuItem = new MenuItem("Previous");
        setIcon(menuItem, ANGLE_DOUBLE_LEFT);
        menuItem.disableProperty().bind(tokenEditorPane.previousToken().isNull());
        menuItem.setAccelerator(new KeyCodeCombination(LEFT, SHORTCUT_DOWN));
        menuItem.setOnAction(event -> tokenEditorPane.loadPreviousToken());
        menu.getItems().add(menuItem);

        menu.getItems().add(new SeparatorMenuItem());

        menuItem = new MenuItem("Exit");
        menuItem.setAccelerator(new KeyCodeCombination(F4, ALT_DOWN));
        menuItem.setOnAction(event -> stage.close());
        menu.getItems().add(menuItem);

        menuBar.getMenus().add(menu);
        return menuBar;
    }
}
