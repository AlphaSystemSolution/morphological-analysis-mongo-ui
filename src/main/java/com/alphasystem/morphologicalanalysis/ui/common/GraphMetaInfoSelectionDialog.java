package com.alphasystem.morphologicalanalysis.ui.common;

import com.alphasystem.morphologicalanalysis.graph.model.GraphMetaInfo;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.DoubleSpinnerValueFactory;
import javafx.scene.layout.GridPane;

import static com.alphasystem.morphologicalanalysis.ui.common.Global.*;
import static javafx.geometry.Pos.CENTER;
import static javafx.scene.control.ButtonType.CANCEL;
import static javafx.scene.control.ButtonType.OK;

/**
 * @author sali
 */
public class GraphMetaInfoSelectionDialog extends Dialog<GraphMetaInfo> {

    private final Spinner<Double> widthField;
    private final Spinner<Double> heightField;
    private final Spinner<Double> tokenWidth;
    private final Spinner<Double> tokenHeight;
    private final Spinner<Double> gapBetweenTokens;
    private final ObjectProperty<GraphMetaInfo> graphMetaInfo;

    public GraphMetaInfoSelectionDialog() {
        setTitle("Select Canvas Meta Properties");

        widthField = new Spinner<>();
        heightField = new Spinner<>();
        tokenWidth = new Spinner<>();
        tokenHeight = new Spinner<>();
        gapBetweenTokens = new Spinner<>();
        graphMetaInfo = new SimpleObjectProperty<>(new GraphMetaInfo());

        getDialogPane().setContent(createMainPanel());
        getDialogPane().getButtonTypes().addAll(OK, CANCEL);
        refreshDialog();
        setGraphMetaInfo(new GraphMetaInfo());

        setResultConverter(param -> {
            if (param.getButtonData().isCancelButton()) {
                return null;
            }
            return getGraphMetaInfo();
        });
    }

    public final GraphMetaInfo getGraphMetaInfo() {
        return graphMetaInfo.get();
    }

    public final void setGraphMetaInfo(GraphMetaInfo graphMetaInfo) {
        this.graphMetaInfo.set(graphMetaInfo);
    }

    private GridPane createMainPanel() {
        GridPane grid = new GridPane();
        grid.setAlignment(CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        int rowIndex = 0;
        // sets up "width" field
        Label label = new Label(RESOURCE_BUNDLE.getString("totalWidth.label"));
        grid.add(label, 0, rowIndex);

        GraphMetaInfo graphMetaInfo = getGraphMetaInfo();

        widthField.setValueFactory(new DoubleSpinnerValueFactory(MIN_WIDTH, MAX_WIDTH, graphMetaInfo.getWidth(),
                AMOUNT_STEP_BY));
        widthField.valueProperty().addListener((observable, oldValue, newValue) -> {
            graphMetaInfo.setWidth(newValue);
        });
        grid.add(widthField, 1, rowIndex);

        rowIndex++;
        // sets up "height" field
        label = new Label(RESOURCE_BUNDLE.getString("totalHeight.label"));
        grid.add(label, 0, rowIndex);

        heightField.setValueFactory(new DoubleSpinnerValueFactory(MIN_HEIGHT, MAX_HEIGHT, graphMetaInfo.getHeight(),
                AMOUNT_STEP_BY));
        heightField.valueProperty().addListener((observable, oldValue, newValue) -> {
            graphMetaInfo.setHeight(newValue);
        });
        grid.add(heightField, 1, rowIndex);

        rowIndex++;
        // sets up "tokenWidth" field
        label = new Label(RESOURCE_BUNDLE.getString("tokenWidth.label"));
        grid.add(label, 0, rowIndex);

        tokenWidth.setValueFactory(new DoubleSpinnerValueFactory(MIN_RECTANGLE_WIDTH, RECTANGLE_WIDTH,
                graphMetaInfo.getTokenWidth(), 10));
        tokenWidth.valueProperty().addListener((observable, oldValue, newValue) -> {
            graphMetaInfo.setTokenWidth(newValue);
        });
        grid.add(tokenWidth, 1, rowIndex);

        rowIndex++;
        // sets up "tokenHeight" field
        label = new Label(RESOURCE_BUNDLE.getString("tokenHeight.label"));
        grid.add(label, 0, rowIndex);

        tokenHeight.setValueFactory(new DoubleSpinnerValueFactory(MIN_RECTANGLE_HEIGHT, RECTANGLE_HEIGHT,
                graphMetaInfo.getTokenHeight(), 10));
        tokenHeight.valueProperty().addListener((observable, oldValue, newValue) -> {
            graphMetaInfo.setTokenHeight(newValue);
        });
        grid.add(tokenHeight, 1, rowIndex);

        rowIndex++;
        // sets up "gapBetweenTokens" field
        label = new Label(RESOURCE_BUNDLE.getString("gapBetweenTokens.label"));
        grid.add(label, 0, rowIndex);

        gapBetweenTokens.setValueFactory(new DoubleSpinnerValueFactory(MIN_GAP_BETWEEN_TOKENS, GAP_BETWEEN_TOKENS,
                graphMetaInfo.getGapBetweenTokens(), 10));
        this.gapBetweenTokens.valueProperty().addListener((observable, oldValue, newValue) -> {
            graphMetaInfo.setGapBetweenTokens(newValue);
        });
        grid.add(this.gapBetweenTokens, 1, rowIndex);

        return grid;
    }

    private void refreshDialog() {
        graphMetaInfo.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                widthField.getValueFactory().setValue(newValue.getWidth());
                heightField.getValueFactory().setValue(newValue.getHeight());
                tokenWidth.getValueFactory().setValue(newValue.getTokenWidth());
                tokenHeight.getValueFactory().setValue(newValue.getTokenHeight());
                gapBetweenTokens.getValueFactory().setValue(newValue.getGapBetweenTokens());
            }
        });
    }
}
