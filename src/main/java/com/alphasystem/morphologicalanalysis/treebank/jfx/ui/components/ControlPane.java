package com.alphasystem.morphologicalanalysis.treebank.jfx.ui.components;

import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.CanvasData;
import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.CanvasMetaData;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import static javafx.geometry.Pos.CENTER;

/**
 * @author sali
 */
public class ControlPane extends Pane {

    private final ObjectProperty<CanvasData> canvasDataObject = new SimpleObjectProperty<>();

    public ControlPane() {
        this(null, null);
    }

    public ControlPane(CanvasData canvasData, CanvasMetaData metaData) {

        CanvasData _canvasData = (canvasData == null) ? new CanvasData() : canvasData;
        CanvasMetaData _metaData = (metaData == null) ? new CanvasMetaData() : metaData;

        VBox vBox = new VBox(10);
        vBox.setAlignment(CENTER);
        vBox.setPadding(new Insets(5, 5, 5, 5));
        vBox.getChildren().addAll(new PropertiesPane(_metaData),
                new DependencyGraphBuilderPane(_canvasData.getNodes()));

        getChildren().add(vBox);

        canvasDataObject.addListener((observable, oldValue, newValue) -> {
            System.out.println("New value arrived");
            updateBuilderPane();
        });

        setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
    }

    public final ObjectProperty<CanvasData> canvasDataObjectProperty() {
        return canvasDataObject;
    }

    private void updateBuilderPane() {
        VBox pane = (VBox) getChildren().get(0);
        DependencyGraphBuilderPane node = (DependencyGraphBuilderPane) pane.getChildren().get(1);
        System.out.println(node);
    }
}
