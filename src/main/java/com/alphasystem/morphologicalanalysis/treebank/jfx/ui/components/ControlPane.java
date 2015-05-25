package com.alphasystem.morphologicalanalysis.treebank.jfx.ui.components;

import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.CanvasMetaData;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * @author sali
 */
public class ControlPane extends Pane {

    public ControlPane() {
        this(null);
    }

    public ControlPane(CanvasMetaData metaData) {

        CanvasMetaData _metaData = (metaData == null) ? new CanvasMetaData() : metaData;

        VBox vBox = new VBox(10);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(5, 5, 5, 5));
        vBox.getChildren().addAll(new PropertiesPane(_metaData), new DependencyGraphBuilderPane());

        getChildren().add(vBox);

        setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        // setPrefSize(350, 400);
    }
}
