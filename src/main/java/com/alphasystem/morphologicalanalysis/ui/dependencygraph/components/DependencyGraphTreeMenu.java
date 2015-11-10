package com.alphasystem.morphologicalanalysis.ui.dependencygraph.components;

import com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.DependencyGraphAdapter;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.GraphNodeAdapter;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.PartOfSpeechNodeAdapter;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.TerminalNodeAdapter;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.CanvasUtil;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import static com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType.*;

/**
 * @author sali
 */
public class DependencyGraphTreeMenu {

    private final ObjectProperty<GraphNodeAdapter> selectedNode = new SimpleObjectProperty<>();
    private final ObjectProperty<DependencyGraphAdapter> graph = new SimpleObjectProperty<>();
    private CanvasUtil canvasUtil = CanvasUtil.getInstance();
    private Menu treeMenu;

    public DependencyGraphTreeMenu(DependencyGraphAdapter dga, Menu treeMenu) {
        this.treeMenu = treeMenu;
        graphProperty().addListener((observable, oldValue, newValue) -> {
            initMenu(newValue);
        });
        setGraph(dga);
    }

    private void initMenu(DependencyGraphAdapter graph) {
        treeMenu.getItems().remove(0, treeMenu.getItems().size());
        if (graph == null) {
            MenuItem menuItem = new MenuItem("Graph is empty");
            menuItem.setDisable(true);
            treeMenu.getItems().add(menuItem);
        } else {
            Menu terminalMenu = new Menu(TERMINAL.name());
            Menu phraseMenu = new Menu(PHRASE.name());
            Menu relationshipMenu = new Menu(RELATIONSHIP.name());
            treeMenu.getItems().addAll(terminalMenu, phraseMenu, relationshipMenu);
            ObservableList<GraphNodeAdapter> graphNodes = graph.getGraphNodes();
            if (graphNodes != null && !graphNodes.isEmpty()) {
                graphNodes.forEach(node -> {
                    GraphNodeType graphNodeType = node.getGraphNodeType();
                    switch (graphNodeType) {
                        case TERMINAL:
                        case REFERENCE:
                        case HIDDEN:
                        case IMPLIED:
                            TerminalNodeAdapter tna = (TerminalNodeAdapter) node;
                            Menu menu = (Menu) createMenuItem(tna);
                            terminalMenu.getItems().addAll(menu);
                            initPartOfSpeechMenuItems(menu, tna);
                            break;
                        case RELATIONSHIP:
                            relationshipMenu.getItems().add(createMenuItem(node));
                            break;
                        case PHRASE:
                            phraseMenu.getItems().add(createMenuItem(node));
                            break;
                    }

                });
            }
        }
    }

    private void initPartOfSpeechMenuItems(Menu menu, TerminalNodeAdapter node) {
        ObservableList<PartOfSpeechNodeAdapter> partOfSpeeches = node.getPartOfSpeeches();
        if (partOfSpeeches == null || partOfSpeeches.isEmpty()) {
            return;
        }
        partOfSpeeches.forEach(posNode -> menu.getItems().add(createMenuItem(posNode)));
    }

    private MenuItem createMenuItem(GraphNodeAdapter node) {
        MenuItem menuItem = canvasUtil.createMenuItem(node);
        menuItem.setOnAction(event -> setSelectedNode(node));
        return menuItem;
    }

    public void setGraph(DependencyGraphAdapter graph) {
        this.graph.set(graph);
    }

    public ObjectProperty<DependencyGraphAdapter> graphProperty() {
        return graph;
    }

    public void setSelectedNode(GraphNodeAdapter selectedNode) {
        this.selectedNode.set(selectedNode);
    }

    public ObjectProperty<GraphNodeAdapter> selectedNodeProperty() {
        return selectedNode;
    }

}
