package com.alphasystem.morphologicalanalysis.ui.dependencygraph.components;

import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.GlobalPropertiesView;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.DependencyGraphAdapter;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.GraphMetaInfoAdapter;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.GraphNodeAdapter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;

import static javafx.geometry.Side.TOP;
import static javafx.scene.control.TabPane.TabClosingPolicy.UNAVAILABLE;

/**
 * @author sali
 */
public class ControlPane extends BorderPane {

    private final ObjectProperty<DependencyGraphAdapter> dependencyGraph = new SimpleObjectProperty<>();
    private DependencyGraphBuilderEditorPane editorPane;

    public ControlPane(DependencyGraphAdapter dependencyGraph) {
        editorPane = new DependencyGraphBuilderEditorPane(null, 0, 0);

        final GlobalPropertiesView globalPropertiesView = new GlobalPropertiesView(dependencyGraph);

        dependencyGraphProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            globalPropertiesView.setDependencyGraph(newValue);
            refreshPanel(newValue);
        });

        TabPane tabPane = new TabPane();
        tabPane.setRotateGraphic(false);
        tabPane.setTabClosingPolicy(UNAVAILABLE);
        tabPane.setSide(TOP);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(editorPane);


        tabPane.getTabs().addAll(
                new Tab(" Properties ", globalPropertiesView),
                new Tab("   Editor   ", borderPane));

        setCenter(tabPane);
        setDependencyGraph(dependencyGraph);
    }

    private void refreshPanel(DependencyGraphAdapter dependencyGraph) {
        getDependencyGraph().selectedNodeProperty().addListener(
                (observable, oldValue, newValue) -> editorPane.setGraphNode(newValue));
        if (dependencyGraph != null && !dependencyGraph.isEmpty()) {
            GraphNodeAdapter node = dependencyGraph.getGraphNodes().get(0);
            GraphMetaInfoAdapter graphMetaInfo = dependencyGraph.getGraphMetaInfo();
            editorPane.setCanvasWidth(graphMetaInfo.getWidth());
            editorPane.setCanvasHeight(graphMetaInfo.getHeight());
            editorPane.setGraphNode(node);
        }
        requestLayout();
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
