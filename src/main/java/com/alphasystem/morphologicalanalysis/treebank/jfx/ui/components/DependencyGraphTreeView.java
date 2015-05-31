package com.alphasystem.morphologicalanalysis.treebank.jfx.ui.components;

import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.GraphNode;
import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.NodeType;
import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.RootNode;
import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.TerminalNode;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.util.List;

import static com.alphasystem.morphologicalanalysis.treebank.jfx.ui.Global.TREE_BANK_STYLE_SHEET;
import static com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.NodeType.ROOT;
import static javafx.scene.control.SelectionMode.SINGLE;

/**
 * @author sali
 */
public class DependencyGraphTreeView extends TreeView<GraphNode> {

    private final ObjectProperty<GraphNode> selectedNode = new SimpleObjectProperty<>();
    private TreeItem<GraphNode> rootItem = new TreeItem<>(new TerminalNode());

    public DependencyGraphTreeView(List<GraphNode> nodes){
        super();
        getStylesheets().add(TREE_BANK_STYLE_SHEET);
        rootItem.setExpanded(true);
        setRoot(rootItem);
        setShowRoot(false);
        initChildren(nodes);
        getSelectionModel().setSelectionMode(SINGLE);
        getSelectionModel().selectedItemProperty().addListener((observable, oldItem, newItem) -> {
            GraphNode graphNode = newItem.getValue();
            NodeType nodeType = graphNode.getNodeType();
            if(nodeType.equals(ROOT)){
                return;
            }
            selectedNodeProperty().setValue(graphNode);
        });
        setPrefWidth(300);
    }

    public GraphNode getSelectedNode() {
        return selectedNode.get();
    }

    public final ObjectProperty<GraphNode> selectedNodeProperty() {
        return selectedNode;
    }

    void initChildren(List<GraphNode> nodes) {
        rootItem.getChildren().remove(0, rootItem.getChildren().size());
        if(nodes != null && !nodes.isEmpty()){
            for (GraphNode node : nodes) {
                NodeType currentNodeType = node.getNodeType();
                TreeItem<GraphNode> newItem = new TreeItem<>(node);
                TreeItem<GraphNode> parent = null;
                for (TreeItem<GraphNode> child : rootItem.getChildren()) {
                    GraphNode childValue = child.getValue();
                    // first check whether current item has node type "ROOT"
                    if(childValue.getNodeType().equals(ROOT)){
                        // if yes look for a node whose child node type is equal to node type of cue
                        RootNode rootNode = (RootNode) childValue;
                        if(rootNode.getChildType().equals(currentNodeType)){
                            parent = child;
                            break;
                        } // end of if "rootNode.getChildType().equals(currentNodeType)"
                    } // end of if "childValue.getNodeType().equals(ROOT)"
                } // end of "for each" children
                if(parent == null){
                    // we don't have any parent  for this node, so create one
                    parent = new TreeItem<>(new RootNode(currentNodeType));
                    parent.setExpanded(true);
                    rootItem.getChildren().add(parent);
                }
                parent.getChildren().add(newItem);
            } // end of "for each" nodes
        }
        requestLayout();
    }
}
