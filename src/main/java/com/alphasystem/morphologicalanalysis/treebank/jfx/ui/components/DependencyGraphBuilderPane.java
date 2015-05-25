package com.alphasystem.morphologicalanalysis.treebank.jfx.ui.components;

import javafx.scene.control.TitledPane;
import javafx.scene.layout.Pane;

/**
 * @author sali
 */
public class DependencyGraphBuilderPane extends Pane {

    public DependencyGraphBuilderPane() {

        getChildren().add(new TitledPane("Dependency Graph Builder", initPane()));

        setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
    }

    private Pane initPane() {

        return new Pane();
    }
}
