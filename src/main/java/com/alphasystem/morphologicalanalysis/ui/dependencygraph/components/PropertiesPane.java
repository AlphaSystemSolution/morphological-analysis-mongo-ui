package com.alphasystem.morphologicalanalysis.ui.dependencygraph.components;

import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.DependencyGraphAdapter;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.GraphMetaInfoAdapter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.DoubleSpinnerValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import static com.alphasystem.morphologicalanalysis.ui.common.Global.*;
import static javafx.geometry.Pos.CENTER;

/**
 * @author sali
 */
@Deprecated
public class PropertiesPane extends BorderPane {

    private final Spinner<Double> widthField = new Spinner<>();
    private final Spinner<Double> heightField = new Spinner<>();
    private final Spinner<Double> tokenWidth = new Spinner<>();
    private final Spinner<Double> tokenHeight = new Spinner<>();
    private final Spinner<Double> gapBetweenTokens = new Spinner<>();
    private final CheckBox showOutlineOnlyCheckBox = new CheckBox();
    private final CheckBox showGridLinesCheckBox = new CheckBox();
    private final CheckBox debugModeCheckBox = new CheckBox();
    private final Spinner<Double> alignTokensYField = new Spinner<>();
    private final Spinner<Double> alignTranslationsY = new Spinner<>();
    private final Spinner<Double> alignPOSsYField = new Spinner<>();
    private final Spinner<Double> alignPOSControlYField = new Spinner<>();
    private final Spinner<Double> alignGroupTranslateYField = new Spinner<>();
    private final ObjectProperty<DependencyGraphAdapter> dependencyGraph = new SimpleObjectProperty<>();

    public PropertiesPane(DependencyGraphAdapter dependencyGraph) {
        setDependencyGraph(dependencyGraph);
        GridPane gridPane = initPane();
        dependencyGraphProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                GraphMetaInfoAdapter graphMetaInfo = newValue.getGraphMetaInfo();
                widthField.getValueFactory().setValue(graphMetaInfo.getWidth());
                heightField.getValueFactory().setValue(graphMetaInfo.getHeight());
                showOutlineOnlyCheckBox.setSelected(graphMetaInfo.isShowOutLines());
                showGridLinesCheckBox.setSelected(graphMetaInfo.isShowGridLines());
                debugModeCheckBox.setSelected(graphMetaInfo.isDebugMode());
            }
        });

        setTop(gridPane);
    }

    private GridPane initPane() {
        GraphMetaInfoAdapter graphMetaInfo = getDependencyGraph().getGraphMetaInfo();
        double width = graphMetaInfo.getWidth();
        double height = graphMetaInfo.getHeight();

        GridPane grid = new GridPane();
        grid.setAlignment(CENTER);
        grid.setHgap(3);
        grid.setVgap(3);
        grid.setPadding(new Insets(5, 5, 5, 5));

        int rowIndex = 0;
        // sets up "width" field
        Label label = new Label(RESOURCE_BUNDLE.getString("totalWidth.label"));
        grid.add(label, 0, rowIndex);

        rowIndex++;
        widthField.setValueFactory(new DoubleSpinnerValueFactory(MIN_WIDTH, MAX_WIDTH,
                width, 20.0));
        widthField.valueProperty().addListener((observable, oldValue, newValue) -> {
            graphMetaInfo.setWidth(newValue);
        });
        grid.add(widthField, 0, rowIndex);

        rowIndex++;
        // sets up "height" field
        label = new Label(RESOURCE_BUNDLE.getString("totalHeight.label"));
        grid.add(label, 0, rowIndex);

        rowIndex++;
        heightField.setValueFactory(new DoubleSpinnerValueFactory(MIN_HEIGHT, MAX_HEIGHT, height, 20));
        heightField.valueProperty().addListener((observable, oldValue, newValue) -> {
            graphMetaInfo.setHeight(newValue);
        });
        grid.add(heightField, 0, rowIndex);

        rowIndex++;
        // sets up "show grid lines" field
        label = new Label(RESOURCE_BUNDLE.getString("showGridLines.label"));
        grid.add(label, 0, rowIndex);

        rowIndex++;
        showGridLinesCheckBox.setSelected(graphMetaInfo.isShowGridLines());
        showGridLinesCheckBox.setOnAction(event -> {
            boolean selected = showGridLinesCheckBox.isSelected();
            showGridLinesCheckBox.setSelected(selected);
        });
        showGridLinesCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            graphMetaInfo.setShowGridLines(newValue);
        });
        grid.add(showGridLinesCheckBox, 0, rowIndex);

        rowIndex++;
        // sets up "show out lines" field
        label = new Label(RESOURCE_BUNDLE.getString("showOutlineOnly.label"));
        grid.add(label, 0, rowIndex);

        rowIndex++;
        showOutlineOnlyCheckBox.setSelected(graphMetaInfo.isShowOutLines());
        showOutlineOnlyCheckBox.disableProperty().bind(showGridLinesCheckBox.selectedProperty());
        showOutlineOnlyCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            graphMetaInfo.setShowOutLines(newValue);
        });
        grid.add(showOutlineOnlyCheckBox, 0, rowIndex);

        rowIndex++;
        // sets up "debug mode" field
        label = new Label(RESOURCE_BUNDLE.getString("debugMode.label"));
        grid.add(label, 0, rowIndex);

        rowIndex++;
        debugModeCheckBox.setSelected(graphMetaInfo.isDebugMode());
        graphMetaInfo.debugModeProperty().bind(debugModeCheckBox.selectedProperty());
        grid.add(debugModeCheckBox, 0, rowIndex);

        rowIndex++;
        label = new Label(RESOURCE_BUNDLE.getString("alignTokensY.label"));
        grid.add(label, 0, rowIndex);

        rowIndex++;
        alignTokensYField.setValueFactory(new DoubleSpinnerValueFactory(0, MAX_HEIGHT, 0, 0.5));
        grid.add(alignTokensYField, 0, rowIndex);

        rowIndex++;
        label = new Label(RESOURCE_BUNDLE.getString("alignTranslationsY.label"));
        grid.add(label, 0, rowIndex);

        rowIndex++;
        alignTranslationsY.setValueFactory(new DoubleSpinnerValueFactory(0, MAX_HEIGHT, 0, 0.5));
        grid.add(alignTranslationsY, 0, rowIndex);

        rowIndex++;
        label = new Label(RESOURCE_BUNDLE.getString("alignPOSY.label"));
        grid.add(label, 0, rowIndex);

        rowIndex++;
        alignPOSsYField.setValueFactory(new DoubleSpinnerValueFactory(0, MAX_HEIGHT, 0, 0.5));
        grid.add(alignPOSsYField, 0, rowIndex);

        rowIndex++;
        label = new Label(RESOURCE_BUNDLE.getString("alignPOSYControl.label"));
        grid.add(label, 0, rowIndex);

        rowIndex++;
        alignPOSControlYField.setValueFactory(new DoubleSpinnerValueFactory(0, MAX_HEIGHT, 0, 0.5));
        grid.add(alignPOSControlYField, 0, rowIndex);

        rowIndex++;
        label = new Label(RESOURCE_BUNDLE.getString("alignGroupTranslateY.label"));
        grid.add(label, 0, rowIndex);

        rowIndex++;
        alignGroupTranslateYField.setValueFactory(new DoubleSpinnerValueFactory(-1 * height, height, 0, 0.5));
        grid.add(alignGroupTranslateYField, 0, rowIndex);

        return grid;
    }

    public final Spinner<Double> getWidthField() {
        return widthField;
    }

    public final Spinner<Double> getHeightField() {
        return heightField;
    }

    public final Spinner<Double> getAlignPOSControlYField() {
        return alignPOSControlYField;
    }

    public final Spinner<Double> getAlignPOSsYField() {
        return alignPOSsYField;
    }

    public final Spinner<Double> getAlignTokensYField() {
        return alignTokensYField;
    }

    public final Spinner<Double> getAlignTranslationsY() {
        return alignTranslationsY;
    }

    public Spinner<Double> getAlignGroupTranslateYField() {
        return alignGroupTranslateYField;
    }

    public final DependencyGraphAdapter getDependencyGraph() {
        return dependencyGraph.get();
    }

    public final void setDependencyGraph(DependencyGraphAdapter dependencyGraph) {
        this.dependencyGraph.set(dependencyGraph);
    }

    public final ObjectProperty<DependencyGraphAdapter> dependencyGraphProperty() {
        return dependencyGraph;
    }

}
