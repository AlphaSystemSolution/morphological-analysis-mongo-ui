package com.alphasystem.morphologicalanalysis.treebank.jfx.ui.components;

import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.CanvasMetaData;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.ResourceBundle;

import static javafx.geometry.Pos.CENTER;
import static javafx.scene.control.Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL;

/**
 * @author sali
 */
public class PropertiesPane extends Pane {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("resources");

    private Spinner<Integer> widthField;
    private Spinner<Integer> heightField;
    private CheckBox showOutlineOnlyCheckBox;
    private CheckBox showGridLinesCheckBox;
    private CheckBox debugModeCheckBox;

    private CanvasMetaData metaData;

    public PropertiesPane() {
        this(null);
    }

    public PropertiesPane(CanvasMetaData metaData) {

        this.metaData = metaData;

        getChildren().add(new TitledPane("Properties", initMetaProperties()));

        setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
    }

    private GridPane initMetaProperties() {
        int width = metaData.getWidth();
        int height = metaData.getHeight();

        GridPane grid = new GridPane();
        grid.setAlignment(CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // sets up "width" field
        Label label = new Label(RESOURCE_BUNDLE.getString("totalWidth.label"));
        grid.add(label, 0, 0);

        widthField = new Spinner<>();
        widthField.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(20, 1000, width, 20));
        widthField.getStyleClass().add(STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        metaData.widthProperty().bind(widthField.valueProperty());
        grid.add(widthField, 1, 0);

        // sets up "height" field
        label = new Label(RESOURCE_BUNDLE.getString("totalHeight.label"));
        grid.add(label, 0, 1);

        heightField = new Spinner<>();
        heightField.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(20, 1000, height, 20));
        heightField.getStyleClass().add(STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        metaData.heightProperty().bind(heightField.valueProperty());
        grid.add(heightField, 1, 1);

        // sets up "show grid lines" field
        label = new Label(RESOURCE_BUNDLE.getString("showGridLines.label"));
        grid.add(label, 0, 2);

        showGridLinesCheckBox = new CheckBox();
        showGridLinesCheckBox.setSelected(metaData.isShowGridLines());
        metaData.showGridLinesProperty().bind(showGridLinesCheckBox.selectedProperty());
        showGridLinesCheckBox.setOnAction(event -> {
            boolean selected = showGridLinesCheckBox.isSelected();
            showOutlineOnlyCheckBox.setSelected(selected);
        });
        grid.add(showGridLinesCheckBox, 1, 2);

        // sets up "show out lines" field
        label = new Label(RESOURCE_BUNDLE.getString("showOutlineOnly.label"));
        grid.add(label, 0, 3);

        showOutlineOnlyCheckBox = new CheckBox();
        showOutlineOnlyCheckBox.setSelected(metaData.isShowOutLines());
        metaData.showOutLinesProperty().bind(showOutlineOnlyCheckBox.selectedProperty());
        grid.add(showOutlineOnlyCheckBox, 1, 3);

        showOutlineOnlyCheckBox.disableProperty().bind(showGridLinesCheckBox.selectedProperty());

        // sets up "debug mode" field
        label = new Label(RESOURCE_BUNDLE.getString("debugMode.label"));
        grid.add(label, 0, 4);

        debugModeCheckBox = new CheckBox();
        debugModeCheckBox.setSelected(metaData.isDebugMode());
        grid.add(debugModeCheckBox, 1, 4);

        return grid;
    }

    public CanvasMetaData getMetaData() {
        return metaData;
    }

    public Spinner<Integer> getHeightField() {
        return heightField;
    }

    public Spinner<Integer> getWidthField() {
        return widthField;
    }
}
