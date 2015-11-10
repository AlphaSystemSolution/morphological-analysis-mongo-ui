package com.alphasystem.morphologicalanalysis.ui.dependencygraph.components;

import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;

import static com.alphasystem.util.AppUtil.isGivenType;
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
        PropertiesPane propertiesPane = new PropertiesPane(dependencyGraph);
        propertiesPane.getWidthField().valueProperty().addListener((observable, oldValue, newValue) -> {
            editorPane.setCanvasWidth(newValue);
        });
        propertiesPane.getHeightField().valueProperty().addListener((observable, oldValue, newValue) -> {
            editorPane.setCanvasHeight(newValue);
        });

        dependencyGraphProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            propertiesPane.setDependencyGraph(newValue);
            refreshPanel(newValue);
            TerminalNodeAdapter node = new TerminalNodeAdapter();
            ObservableList<GraphNodeAdapter> nodes = getDependencyGraph().getGraphNodes();
            if (!nodes.isEmpty()) {
                node = (TerminalNodeAdapter) nodes.get(0);
            }
            propertiesPane.getAlignTokensYField().getValueFactory().setValue(node.getY());
            propertiesPane.getAlignTokensYField().valueProperty().addListener((observable1, oldValue1, newValue1) -> {
                nodes.stream().filter(gn -> isGivenType(TerminalNodeAdapter.class, gn)).forEach(gn -> gn.setY(newValue1));
            });
            propertiesPane.getAlignTranslationsY().getValueFactory().setValue(node.getTranslationY());
            ObservableList<PartOfSpeechNodeAdapter> partOfSpeeches = node.getPartOfSpeeches();
            if (!partOfSpeeches.isEmpty()) {
                PartOfSpeechNodeAdapter partOfSpeechNode = partOfSpeeches.get(0);
                propertiesPane.getAlignPOSsYField().getValueFactory().setValue(partOfSpeechNode.getY());
                propertiesPane.getAlignPOSControlYField().getValueFactory().setValue(partOfSpeechNode.getCy());
            }
            propertiesPane.getAlignTranslationsY().valueProperty().addListener((observable1, oldValue1, newValue1) -> {
                nodes.stream().filter(gn -> isGivenType(TerminalNodeAdapter.class, gn)).forEach(
                        gn -> ((TerminalNodeAdapter) gn).setTranslationY(newValue1));
            });
            propertiesPane.getAlignPOSsYField().valueProperty().addListener((observable1, oldValue1, newValue1) -> {
                nodes.stream().filter(gn -> isGivenType(TerminalNodeAdapter.class, gn)).forEach(gn -> {
                    TerminalNodeAdapter terminalNode = (TerminalNodeAdapter) gn;
                    ObservableList<PartOfSpeechNodeAdapter> partOfSpeeches1 = terminalNode.getPartOfSpeeches();
                    if (!partOfSpeeches1.isEmpty()) {
                        partOfSpeeches1.forEach(partOfSpeechNode -> partOfSpeechNode.setY(newValue1));
                    }
                });
            });
            propertiesPane.getAlignPOSControlYField().valueProperty().addListener((observable1, oldValue1, newValue1) -> {
                nodes.stream().filter(gn -> isGivenType(TerminalNodeAdapter.class, gn)).forEach(gn -> {
                    TerminalNodeAdapter terminalNode = (TerminalNodeAdapter) gn;
                    ObservableList<PartOfSpeechNodeAdapter> partOfSpeeches1 = terminalNode.getPartOfSpeeches();
                    if (!partOfSpeeches1.isEmpty()) {
                        partOfSpeeches1.forEach(partOfSpeechNode -> partOfSpeechNode.setCy(newValue1));
                    }
                });
            });
            propertiesPane.getAlignGroupTranslateYField().getValueFactory().setValue(node.getTranslateY());
            propertiesPane.getAlignGroupTranslateYField().valueProperty().addListener((observable1, oldValue1, newValue1) -> {
                nodes.stream().filter(gn -> isGivenType(TerminalNodeAdapter.class, gn)).forEach(
                        tn -> tn.setTranslateY(newValue1));
            });

        });

        TabPane tabPane = new TabPane();
        tabPane.setRotateGraphic(false);
        tabPane.setTabClosingPolicy(UNAVAILABLE);
        tabPane.setSide(TOP);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(editorPane);

        tabPane.getTabs().addAll(
                new Tab(" Properties ", propertiesPane),
                new Tab("   Editor   ", borderPane));

        setCenter(tabPane);
        setDependencyGraph(dependencyGraph);
    }

    private void refreshPanel(DependencyGraphAdapter dependencyGraph) {
        getDependencyGraph().selectedNodeProperty().addListener(
                (observable, oldValue, newValue) -> {
                    editorPane.setGraphNode(newValue);
                });
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
