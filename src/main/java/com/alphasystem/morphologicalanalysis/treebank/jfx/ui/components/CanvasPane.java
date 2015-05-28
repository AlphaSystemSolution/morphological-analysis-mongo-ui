package com.alphasystem.morphologicalanalysis.treebank.jfx.ui.components;

import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.CanvasData;
import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.CanvasMetaData;
import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.GraphNode;
import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.TerminalNode;
import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.util.DependencyGraphGraphicTool;
import com.alphasystem.svg.jfx.SVGGraphicsContext;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 * @author sali
 */
public class CanvasPane extends Pane {

    private final ObjectProperty<CanvasData> canvasDataObject;
    private SVGGraphicsContext svgGraphicsContext;
    private DependencyGraphGraphicTool tool = DependencyGraphGraphicTool.getInstance();
    private Pane canvasPane;

    public CanvasPane(CanvasData data) {
        this.canvasDataObject = new SimpleObjectProperty<>(data);
        CanvasMetaData metaData = data.getCanvasMetaData();
        int width = metaData.getWidth();
        int height = metaData.getHeight();

        initListeners();

        canvasPane = new Pane();
        svgGraphicsContext = new SVGGraphicsContext(canvasPane, width, height);
        initCanvas();

        canvasPane.setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        canvasPane.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        canvasPane.setPrefSize(width, height);
        canvasPane.setBackground(new Background(new BackgroundFill(Color.BEIGE, null, null)));

        getChildren().add(canvasPane);

        //setBackground(new Background(new BackgroundFill(WHITE, null, null)));
        setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        setPrefSize(width + 200, height + 200);
    }

    private void initListeners() {
        CanvasMetaData metaData = canvasDataObject.get().getCanvasMetaData();
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
        canvasDataObject.addListener((observable, oldValue, newData) -> {
            if (newData != null) {
                System.out.println("<<<<<<<<<<<<<<< >>>>>>>>>>>>>> " + newData);
                ObservableList<GraphNode> nodes = newData.getNodes();
                for (GraphNode node : nodes) {
                    TerminalNode tn = (TerminalNode) node;
                    System.out.println(String.format("%s,%s %s,%s", tn.getX1(), tn.getY1(), tn.getX2(), tn.getY2()));
                    Line line = tool.drawLine(tn.getId(), tn.getX1(), tn.getY1(), tn.getX2(), tn.getY2(),
                            DependencyGraphGraphicTool.DARK_GRAY_CLOUD, 0.5);
                    svgGraphicsContext.draw(line);
                }
            }
        });
    }

    public CanvasData getCanvasDataObject() {
        return canvasDataObject.get();
    }

    public void setCanvasDataObject(CanvasData canvasDataObject) {
        this.canvasDataObject.set(canvasDataObject);
    }

    public final ObjectProperty<CanvasData> canvasDataObjectProperty() {
        return canvasDataObject;
    }

    private void initCanvas() {
        CanvasMetaData metaData = canvasDataObject.get().getCanvasMetaData();
        int size = canvasPane.getChildren().size();
        canvasPane.getChildren().remove(0, size);
        boolean showOutline = metaData.isShowOutLines();
        boolean showGridLines = metaData.isShowGridLines();
        int width = metaData.getWidth();
        int height = metaData.getHeight();
        canvasPane.setPrefSize(width, height);
        if (showOutline || showGridLines) {
            svgGraphicsContext.draw(tool.drawGridLines(showGridLines, width, height));
        }

        setPrefSize(width + 200, height + 200);
        requestLayout();
    }

}
