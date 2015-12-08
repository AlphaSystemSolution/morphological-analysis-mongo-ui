package com.alphasystem.morphologicalanalysis.ui.wordbyword;

import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.LocationPropertiesView;
import com.alphasystem.morphologicalanalysis.util.MorphologicalAnalysisRepositoryUtil;
import com.alphasystem.morphologicalanalysis.util.RepositoryTool;
import com.alphasystem.morphologicalanalysis.util.SpringContextHelper;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.repository.LocationRepository;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import static com.alphasystem.util.AppUtil.NEW_LINE;
import static java.lang.String.format;
import static javafx.geometry.Pos.CENTER;
import static javafx.scene.layout.BorderStroke.THIN;
import static javafx.scene.layout.BorderStrokeStyle.SOLID;
import static javafx.scene.layout.CornerRadii.EMPTY;
import static javafx.scene.paint.Color.LIGHTGREY;

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
        final LocationRepository locationRepository = repositoryUtil.getLocationRepository();

        Location location = locationRepository.findByDisplayName("1:2:1:2");
        LocationPropertiesView root = new LocationPropertiesView();
        root.setLocation(location);
        root.setBorder(new Border(new BorderStroke(LIGHTGREY, SOLID, EMPTY, THIN)));

        VBox vBox = new VBox();
        vBox.setSpacing(10);

        ComboBox<String> displayNameComboBox = new ComboBox<>();
        displayNameComboBox.getItems().addAll("18:1:1:2", "18:1:4:1", "18:1:5:1", "18:1:6:2");
        displayNameComboBox.valueProperty().addListener((o, ov, nv) -> {
            Location l = locationRepository.findByDisplayName(nv);
            primaryStage.setTitle(format("View Test {%s}", l.getDisplayName()));
            root.setLocation(l);
        });
        displayNameComboBox.getSelectionModel().select(0);
        FlowPane flowPane = new FlowPane();
        flowPane.setHgap(10);
        flowPane.setAlignment(CENTER);

        TextArea textArea = new TextArea();

        Button button = new Button("          Get Data          ");
        button.setOnAction(event -> {
            Location rootLocation = root.getLocation();
            String text = format("%s*-*-*-*-*-*-*-*-*-*-*-*-*-*-%s%s%sPOS: %s",
                    NEW_LINE, NEW_LINE, rootLocation.getDisplayName(), NEW_LINE, rootLocation.getPartOfSpeech());
            textArea.setText(textArea.getText() + text);
        });

        flowPane.getChildren().addAll(displayNameComboBox, button);
        vBox.getChildren().addAll(root, flowPane, textArea);

        Scene scene = new Scene(vBox);
        primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
