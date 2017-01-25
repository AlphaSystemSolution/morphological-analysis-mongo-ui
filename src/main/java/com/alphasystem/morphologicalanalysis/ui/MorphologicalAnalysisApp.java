package com.alphasystem.morphologicalanalysis.ui;

import com.alphasystem.morphologicalanalysis.ui.dependencygraph.TreeBankPane;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.TokenEditorApp;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.TokenEditorPane;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.WordByWordPane;
import com.alphasystem.morphologicalanalysis.util.SpringContextHelper;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.alphasystem.morphologicalanalysis.ui.common.Global.DEFAULT_DICTIONARY_DIRECTORY;
import static com.alphasystem.util.nio.NIOFileUtils.copyDir;

/**
 * @author sali
 */
public class MorphologicalAnalysisApp extends Application {

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
        primaryStage.setTitle("Morphological Analysis Applications");

        BorderPane mainPane = new BorderPane();

        VBox vBox = new VBox();
        vBox.setSpacing(20);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(20));

        Button button1 = createButton("Word by Word Builder");
        button1.setOnAction(event -> showApp(new WordByWordPane(), "Quranic Morphological Word By Word Builder"));

        Button button2 = createButton("Token Editor");
        button2.setOnAction(event -> showApp(new TokenEditorPane(), "Quranic Morphological Word By Word Token Editor"));

        Button button3 = createButton("Dependency Graph Builder");
        button3.setOnAction(event -> showApp(new TreeBankPane(), "Quranic Morphological Dependency Graph Builder"));

        vBox.getChildren().addAll(button1, button2, button3);

        mainPane.setCenter(vBox);

        Scene scene = new Scene(mainPane);

        primaryStage.setScene(scene);
        primaryStage.setMinWidth(300);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private static Button createButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: linear-gradient(#f2f2f2, #d6d6d6)," +
                "linear-gradient(#fcfcfc 0%, #d9d9d9 20%, #d6d6d6 100%),linear-gradient(#dddddd 0%, #f6f6f6 50%);" +
                "-fx-background-radius: 8,7,6;-fx-background-insets: 0,1,2;-fx-text-fill: black;" +
                "-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );");
        button.setMaxWidth(Double.MAX_VALUE);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefSize(128, 64);
        return button;
    }

    private static void showApp(Parent root, String title){
        Stage primaryStage = new Stage();

        primaryStage.setTitle(title);
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());

        Scene scene = new Scene(root);
        scene.getStylesheets().addAll("/styles/application.css", "/styles/glyphs_custom.css");
        primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
