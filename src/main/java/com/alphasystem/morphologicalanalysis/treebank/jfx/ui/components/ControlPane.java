package com.alphasystem.morphologicalanalysis.treebank.jfx.ui.components;

import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.CanvasData;
import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.CanvasMetaData;
import javafx.geometry.Insets;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import static javafx.geometry.Pos.CENTER;

/**
 * @author sali
 */
public class ControlPane extends Pane {

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

        setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        // setPrefSize(350, 400);
    }
}
