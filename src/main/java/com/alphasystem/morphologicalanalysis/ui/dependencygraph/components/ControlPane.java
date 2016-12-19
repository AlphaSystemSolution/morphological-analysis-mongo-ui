package com.alphasystem.morphologicalanalysis.ui.dependencygraph.components;

import com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.GlobalPropertiesView;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.*;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.*;
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

        PropertiesEditor editor = editors[0];
        editor.setNode(null);
        editorTab = new Tab("   Editor   ", editor);

        dependencyGraphProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            newValue.getGraphMetaInfo().widthProperty().addListener((observable1, oldValue1, newValue1) -> {
                final PropertiesEditor content = (PropertiesEditor) editorTab.getContent();
                content.setCanvasWidth((Double) newValue1);
            });
            newValue.getGraphMetaInfo().heightProperty().addListener((observable1, oldValue1, newValue1) -> {
                final PropertiesEditor content = (PropertiesEditor) editorTab.getContent();
                content.setCanvasHeight((Double) newValue1);
            });
            globalPropertiesView.setDependencyGraph(newValue);
            refreshPanel(newValue);
        });

        TabPane tabPane = new TabPane();
        tabPane.setRotateGraphic(false);
        tabPane.setTabClosingPolicy(UNAVAILABLE);
        tabPane.setSide(TOP);


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
                (observable, oldValue, newValue) -> updateEditor(newValue, getDependencyGraph().getGraphMetaInfo()));
        if (dependencyGraph != null && !dependencyGraph.isEmpty()) {
            GraphNodeAdapter node = dependencyGraph.getGraphNodes().get(0);
            editorTab.setContent(resetEditor(editors[0], node, dependencyGraph.getGraphMetaInfo()));
        }
        requestLayout();
    }

    @SuppressWarnings({"unchecked"})
    private void updateEditor(GraphNodeAdapter newValue, GraphMetaInfoAdapter graphMetaInfo) {
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
            editorTab.setContent(resetEditor(editor, newValue, graphMetaInfo));
        }
    }

    @SuppressWarnings({"unchecked"})
    private PropertiesEditor resetEditor(PropertiesEditor editor, GraphNodeAdapter node, GraphMetaInfoAdapter graphMetaInfoAdapter) {
        editor.setNode(node);
        editor.setCanvasWidth(graphMetaInfoAdapter.getWidth());
        editor.setCanvasHeight(graphMetaInfoAdapter.getHeight());
        final GraphNodeType type = node.getGraphNodeType();
        switch (type) {
            case TERMINAL:
            case REFERENCE:
            case HIDDEN:
            case IMPLIED:
                TerminalNodeAdapter terminalAdapter = (TerminalNodeAdapter) node;
                editor.setLowerXBound(terminalAdapter.getX1());
                editor.setUpperXBound(terminalAdapter.getX2());
                editor.setLowerYBound(0);
                editor.setUpperYBound(graphMetaInfoAdapter.getHeight()/3);
                break;
            case PART_OF_SPEECH:
                PartOfSpeechNodeAdapter posAdapter = (PartOfSpeechNodeAdapter) node;
                final TerminalNodeAdapter parent = (TerminalNodeAdapter) posAdapter.getParent();
                editor.setLowerXBound(parent.getX1());
                editor.setUpperXBound(parent.getX2());
                editor.setLowerYBound(0);
                editor.setUpperYBound(graphMetaInfoAdapter.getHeight()/3);
                break;
            case PHRASE:
                PhraseNodeAdapter phraseAdapter = (PhraseNodeAdapter) node;
                editor.setLowerXBound(phraseAdapter.getX1());
                editor.setUpperXBound(phraseAdapter.getX2());
                editor.setLowerYBound(0);
                editor.setUpperYBound(graphMetaInfoAdapter.getHeight()/3);
                break;
            case RELATIONSHIP:
                RelationshipNodeAdapter relationshipAdapter = (RelationshipNodeAdapter) node;
                editor.setLowerXBound(relationshipAdapter.getControlX1());
                editor.setUpperXBound(relationshipAdapter.getControlX2());
                editor.setLowerYBound(0);
                editor.setUpperYBound(graphMetaInfoAdapter.getHeight()/3);
                break;
            default:
                break;
        }
        return editor;
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
