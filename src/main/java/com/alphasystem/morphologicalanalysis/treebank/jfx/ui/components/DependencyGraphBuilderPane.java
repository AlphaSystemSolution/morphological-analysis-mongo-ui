package com.alphasystem.morphologicalanalysis.treebank.jfx.ui.components;

import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.GraphNode;
import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.TerminalNode;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.List;

import static javafx.geometry.Pos.CENTER;
import static javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED;

/**
 * @author sali
 */
@Deprecated
public class DependencyGraphBuilderPane extends Pane {

    private final DoubleProperty canvasWidth = new SimpleDoubleProperty(800);
    private final DoubleProperty canvasHeight = new SimpleDoubleProperty(400);
    private DependencyGraphTreeView tree;
    private DependencyGraphBuilderEditorPane editorPane;

    public DependencyGraphBuilderPane(List<GraphNode> nodes) {
        VBox vBox = new VBox(10);
        vBox.setAlignment(CENTER);
        vBox.setPadding(new Insets(5, 5, 5, 5));

        tree = new DependencyGraphTreeView(nodes);

        GraphNode graphNode = new TerminalNode();
        if (!nodes.isEmpty()) {
            graphNode = nodes.get(0);
        }

        editorPane = new DependencyGraphBuilderEditorPane(graphNode);
        canvasWidthProperty().addListener((observable1, oldWidth, newWidth) -> {
            if(oldWidth != newWidth){
                editorPane.updateWidth((Double) newWidth);
            }
        });
        canvasHeightProperty().addListener((observable1, oldHeight, newHeight) -> {
            if(oldHeight != newHeight){
                editorPane.updateHeight((Double) newHeight);
            }
        });

        tree.selectedNodeProperty().addListener((observable, oldValue, newValue) -> {
            editorPane.initPane(newValue);
        });

        vBox.getChildren().addAll(new TitledPane("Dependency Graph Tree", initTreePane()),
                new TitledPane("Dependency Graph Controls", editorPane));

        getChildren().add(vBox);

        setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        //setPrefSize(300, 600);
    }

    public DoubleProperty canvasHeightProperty() {
        return canvasHeight;
    }

    public DoubleProperty canvasWidthProperty() {
        return canvasWidth;
    }

    private Pane initTreePane() {
        BorderPane borderPane = new BorderPane();
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(AS_NEEDED);
        scrollPane.setVbarPolicy(AS_NEEDED);
        scrollPane.setContent(tree);
        borderPane.setCenter(scrollPane);
        return borderPane;
    }

    public void updatePane(List<GraphNode> nodes) {
        tree.initChildren(nodes);
        requestLayout();
    }
}
