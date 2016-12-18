package com.alphasystem.morphologicalanalysis.ui.dependencygraph.components;

import com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.GlobalPropertiesView;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.*;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.DependencyGraphAdapter;
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
    private final PropertiesEditor[] editors = new PropertiesEditor[4];
    private final Tab editorTab;

    @SuppressWarnings({"unchecked"})
    public ControlPane(DependencyGraphAdapter dependencyGraph) {
        editors[0] = new TerminalPropertiesEditor();
        editors[1] = new PartOfSpeechPropertiesEditor();
        editors[2] = new PhrasePropertiesEditor();
        editors[3] = new RelationshipPropertiesEditor();
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

        PropertiesEditor editor = editors[0];
        editor.setNode(null);
        editorTab = new Tab("   Editor   ", editor);
        tabPane.getTabs().addAll(
                new Tab(" Properties ", wrapInBorderPane(globalPropertiesView)),
                editorTab);

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
                (observable, oldValue, newValue) -> updateEditor(newValue));
        if (dependencyGraph != null && !dependencyGraph.isEmpty()) {
            GraphNodeAdapter node = dependencyGraph.getGraphNodes().get(0);
            PropertiesEditor editor = new TerminalPropertiesEditor();
            editor.setNode(node);
            editorTab.setContent(editor);
        }
        requestLayout();
    }

    @SuppressWarnings({"unchecked"})
    private void updateEditor(GraphNodeAdapter newValue) {
        PropertiesEditor editor = null;
        final GraphNodeType type = newValue.getGraphNodeType();
        switch (type) {
            case TERMINAL:
            case REFERENCE:
            case HIDDEN:
            case IMPLIED:
                editor = editors[0];
                break;
            case PART_OF_SPEECH:
                editor = editors[1];
                break;
            case PHRASE:
                editor = editors[2];
                break;
            case RELATIONSHIP:
                editor = editors[3];
                break;
            default:
                break;
        }
        if (editor != null) {
            editor.setNode(newValue);
            editorTab.setContent(editor);
        }
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
