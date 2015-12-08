package com.alphasystem.morphologicalanalysis.ui.wordbyword;

import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.TokenPropertiesView;
import com.alphasystem.morphologicalanalysis.util.MorphologicalAnalysisRepositoryUtil;
import com.alphasystem.morphologicalanalysis.util.RepositoryTool;
import com.alphasystem.morphologicalanalysis.util.SpringContextHelper;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import com.alphasystem.morphologicalanalysis.wordbyword.repository.TokenRepository;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import static com.alphasystem.util.AppUtil.NEW_LINE;
import static java.lang.String.format;
import static javafx.geometry.Pos.CENTER;

/**
 * @author sali
 */
public class ViewTestApp extends Application {

    static {
        SpringContextHelper.getInstance();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("View Test");
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());

        MorphologicalAnalysisRepositoryUtil repositoryUtil = RepositoryTool.getInstance().getRepositoryUtil();
        final TokenRepository tokenRepository = repositoryUtil.getTokenRepository();

        VBox vBox = new VBox();
        vBox.setSpacing(10);

        TokenPropertiesView root = new TokenPropertiesView();

        ComboBox<String> displayNameComboBox = new ComboBox<>();
        displayNameComboBox.getItems().addAll("18:1:1", "18:1:4", "18:1:5", "18:1:6");
        displayNameComboBox.valueProperty().addListener((o, ov, nv) -> {
            Token token = tokenRepository.findByDisplayName(nv);
            primaryStage.setTitle(format("View Test {%s}", token.getDisplayName()));
            root.setToken(token);
        });
        displayNameComboBox.getSelectionModel().select(0);
        FlowPane flowPane = new FlowPane();
        flowPane.setHgap(10);
        flowPane.setAlignment(CENTER);

        TextArea textArea = new TextArea();

        Button button = new Button("          Get Data          ");
        button.setOnAction(event -> {
            Token token = root.getToken();
            textArea.appendText(format("%s%sToken: %s", NEW_LINE, NEW_LINE, token.getDisplayName()));
        });

        flowPane.getChildren().addAll(displayNameComboBox, button);


        vBox.getChildren().addAll(root, flowPane, textArea);

        Scene scene = new Scene(vBox);
        primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
