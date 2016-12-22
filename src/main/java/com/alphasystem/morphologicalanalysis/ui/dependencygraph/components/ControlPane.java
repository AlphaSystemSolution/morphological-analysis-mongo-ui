package com.alphasystem.morphologicalanalysis.ui.dependencygraph.components;

import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.GlobalPropertiesView;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.DependencyGraphAdapter;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.GraphMetaInfoAdapter;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.GraphNodeAdapter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
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

    @SuppressWarnings({"unchecked"})
    public ControlPane(DependencyGraphAdapter dependencyGraph) {
        editorPane = new DependencyGraphBuilderEditorPane(null);
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

        tabPane.getTabs().addAll(
                new Tab(" Properties ", wrapInBorderPane(globalPropertiesView)),
                new Tab("   Editor   ", wrapInBorderPane(editorPane)));

        setCenter(tabPane);
        setDependencyGraph(dependencyGraph);
    }

    private BorderPane wrapInBorderPane(Node node) {
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(node);
        borderPane.setPadding(new Insets(5, 5, 5, 5));
        return borderPane;
    }

    @SuppressWarnings({"unchecked"})
    private void refreshPanel(DependencyGraphAdapter dependencyGraph) {
        getDependencyGraph().selectedNodeProperty().addListener(
                (observable, oldValue, newValue) -> editorPane.setGraphNode(newValue));
        if (dependencyGraph != null && !dependencyGraph.isEmpty()) {
            GraphNodeAdapter node = dependencyGraph.getGraphNodes().get(0);
            GraphMetaInfoAdapter graphMetaInfo = dependencyGraph.getGraphMetaInfo();
            editorPane.setMetaInfo(graphMetaInfo);
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
