package com.alphasystem.morphologicalanalysis.ui.wordbyword;

import com.alphasystem.morphologicalanalysis.util.SpringContextHelper;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import static com.alphasystem.util.AppUtil.getResourceAsStream;
import static javafx.scene.input.KeyCode.*;
import static javafx.scene.input.KeyCombination.ALT_DOWN;
import static javafx.scene.input.KeyCombination.SHORTCUT_DOWN;

/**
 * @author sali
 */
public class TokenEditorApp extends Application {

    static {
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
        toolbarButton.setGraphic(new ImageView(new Image(getResourceAsStream("images.24x24.save-icon.png"))));
        toolbarButton.disableProperty().bind(tokenEditorPane.token().isNull());
        toolbarButton.setOnAction(event -> tokenEditorPane.saveToken());
        toolBar.getItems().add(toolbarButton);

        toolbarButton = new Button();
        toolbarButton.setGraphic(new ImageView(new Image(getResourceAsStream("images.24x24.next-icon.png"))));
        toolbarButton.disableProperty().bind(tokenEditorPane.nextToken().isNull());
        toolbarButton.setOnAction(event -> tokenEditorPane.loadNextToken());
        toolBar.getItems().add(toolbarButton);

        toolbarButton = new Button();
        toolbarButton.setGraphic(new ImageView(new Image(getResourceAsStream("images.24x24.previous-icon.png"))));
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

        menuItem = new MenuItem("Save", new ImageView(new Image(getResourceAsStream("images.16x16.save-icon.png"))));
        menuItem.disableProperty().bind(tokenEditorPane.token().isNull());
        menuItem.setAccelerator(new KeyCodeCombination(S, SHORTCUT_DOWN));
        menuItem.setOnAction(event -> tokenEditorPane.saveToken());
        menu.getItems().add(menuItem);

        menuItem = new MenuItem("Next", new ImageView(new Image(getResourceAsStream("images.16x16.next-icon.png"))));
        menuItem.disableProperty().bind(tokenEditorPane.nextToken().isNull());
        menuItem.setAccelerator(new KeyCodeCombination(RIGHT, SHORTCUT_DOWN));
        menuItem.setOnAction(event -> tokenEditorPane.loadNextToken());
        menu.getItems().add(menuItem);

        menuItem = new MenuItem("Previous", new ImageView(new Image(getResourceAsStream("images.16x16.previous-icon.png"))));
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
