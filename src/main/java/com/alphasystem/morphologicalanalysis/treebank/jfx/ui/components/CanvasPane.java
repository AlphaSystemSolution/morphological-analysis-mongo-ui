package com.alphasystem.morphologicalanalysis.treebank.jfx.ui.components;

import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.CanvasMetaData;
import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.util.TreeBankSVGTool;
import com.alphasystem.morphologicalanalysis.treebank.model.TreeBankData;
import com.alphasystem.svg.jfx.SVGGraphicsContext;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * @author sali
 */
public class CanvasPane extends Pane {

    private SVGGraphicsContext svgGraphicsContext;
    private TreeBankData data;
    private CanvasMetaData metaData;
    private FlowPane canvasPane;

    public CanvasPane(TreeBankData data, CanvasMetaData metaData) {
        this.data = data;

        this.metaData = metaData;
        int width = metaData.getWidth();
        int height = metaData.getHeight();

        initListeners();

        canvasPane = new FlowPane();
        svgGraphicsContext = new SVGGraphicsContext(canvasPane, width, height);
        initCanvas();

        canvasPane.setMinSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);
        canvasPane.setMaxSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);
        canvasPane.setPrefSize(width, height);
        canvasPane.setBackground(new Background(new BackgroundFill(Color.BEIGE, null, null)));

        getChildren().add(canvasPane);

        //setBackground(new Background(new BackgroundFill(WHITE, null, null)));
        setMinSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);
        setMaxSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);
        setPrefSize(width + 200, height + 200);
    }

    private void initListeners() {
        metaData.widthProperty().addListener((observable, oldValue, newValue) -> {
            initCanvas();
        });
        metaData.heightProperty().addListener((observable, oldValue, newValue) -> {
            initCanvas();
        });
        metaData.showGridLinesProperty().addListener((observable, oldValue, newValue) -> {
            initCanvas();
        });
        metaData.showOutLinesProperty().addListener((observable, oldValue, newValue) -> {
            initCanvas();
        });
    }

    private void initCanvas() {
        int size = canvasPane.getChildren().size();
        canvasPane.getChildren().remove(0, size);
        boolean showOutline = metaData.isShowOutLines();
        boolean showGridLines = metaData.isShowGridLines();
        int width = metaData.getWidth();
        int height = metaData.getHeight();

        if (showOutline || showGridLines) {
            svgGraphicsContext.draw(TreeBankSVGTool.drawGridLines(showGridLines, width, height));
        }

        setPrefSize(width + 200, height + 200);
        requestLayout();
    }

}
