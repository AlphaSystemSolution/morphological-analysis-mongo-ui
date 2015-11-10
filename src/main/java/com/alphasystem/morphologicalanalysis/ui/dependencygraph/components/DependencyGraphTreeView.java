package com.alphasystem.morphologicalanalysis.ui.dependencygraph.components;

import com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType;
import com.alphasystem.morphologicalanalysis.ui.common.GraphNodeTreeCell;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import static com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType.*;
import static javafx.scene.control.SelectionMode.SINGLE;

/**
 * @author sali
 */
@Deprecated
public class DependencyGraphTreeView extends TreeView<GraphNodeAdapter> {

    private final ObjectProperty<GraphNodeAdapter> selectedNode = new SimpleObjectProperty<>();
    private final TreeItem<GraphNodeAdapter> rootItem = new TreeItem<>(new TerminalNodeAdapter());
    private final ObjectProperty<DependencyGraphAdapter> graph = new SimpleObjectProperty<>();

    public DependencyGraphTreeView(DependencyGraphAdapter dga) {
        super();
        rootItem.setExpanded(true);
        setCellFactory(param -> new GraphNodeTreeCell());
        setRoot(rootItem);
        setShowRoot(false);
        getSelectionModel().setSelectionMode(SINGLE);
        graphProperty().addListener((observable, oldValue, newValue) -> {
            initChildren(newValue);
        });
        setGraph(dga);
        getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            GraphNodeAdapter graphNode = newValue.getValue();
            GraphNodeType nodeType = graphNode.getGraphNodeType();
            if (nodeType.equals(ROOT)) {
                return;
            }
            selectedNodeProperty().setValue(graphNode);
        });
        setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
    }

    public ObjectProperty<DependencyGraphAdapter> graphProperty() {
        return graph;
    }

    public void setGraph(DependencyGraphAdapter graph) {
        this.graph.set(graph);
    }

    public ObjectProperty<GraphNodeAdapter> selectedNodeProperty() {
        return selectedNode;
    }

    private void initChildren(DependencyGraphAdapter graph) {
        rootItem.getChildren().remove(0, rootItem.getChildren().size());
        if (graph != null) {
            ObservableList<GraphNodeAdapter> graphNodes = graph.getGraphNodes();
            if (graphNodes != null && !graphNodes.isEmpty()) {
                for (GraphNodeAdapter node : graphNodes) {
                    GraphNodeType currentNodeType = node.getGraphNodeType();
                    TreeItem<GraphNodeAdapter> newItem = new TreeItem<>(node);
                    TreeItem<GraphNodeAdapter> parent = null;
                    for (TreeItem<GraphNodeAdapter> child : rootItem.getChildren()) {
                        GraphNodeAdapter childValue = child.getValue();
                        // first check whether current item has node type "ROOT"
                        GraphNodeType nodeType = childValue.getGraphNodeType();
                        if (nodeType.equals(ROOT)) {
                            // if yes look for a node whose dependent node type is equal to node type of current item
                            RootNodeAdapter rootNode = (RootNodeAdapter) childValue;
                            if (rootNode.getSrc().getChildNodeType().equals(currentNodeType)) {
                                parent = child;
                                break;
                            } // end of if "rootNode.getChildType().equals(currentNodeType)"
                        } // end of if "childValue.getNodeType().equals(ROOT)"
                    } // end of "for each" children
                    if (parent == null) {
                        // we don't have any parent  for this node, so create one
                        parent = new TreeItem<>(new RootNodeAdapter(currentNodeType));
                        parent.setExpanded(true);
                        rootItem.getChildren().add(parent);
                    } // end of "if" parent == null
                    if (currentNodeType.equals(TERMINAL) || currentNodeType.equals(IMPLIED)
                            || currentNodeType.equals(HIDDEN) || currentNodeType.equals(REFERENCE)) {
                        initPartOfSpeechItems(newItem, (TerminalNodeAdapter) node);
                    }
                    parent.getChildren().add(newItem);
                } // end of "for each" graphNodes
            } // end of "if" graphNodes
        } // end of "if" graph
        requestLayout();
        getSelectionModel().select(0);
    }

    private void initPartOfSpeechItems(TreeItem<GraphNodeAdapter> parent, TerminalNodeAdapter node) {
        ObservableList<PartOfSpeechNodeAdapter> partOfSpeeches = node.getPartOfSpeeches();
        if (partOfSpeeches == null || partOfSpeeches.isEmpty()) {
            return;
        }
        for (PartOfSpeechNodeAdapter posNode : partOfSpeeches) {
            TreeItem<GraphNodeAdapter> newItem = new TreeItem<>(posNode);
            parent.getChildren().add(newItem);
        }
        parent.setExpanded(true);
    }
}
