package com.alphasystem.morphologicalanalysis.ui.dependencygraph.components;

import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.CanvasData;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.GraphNode;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.TerminalNode;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import static javafx.geometry.Pos.CENTER;
import static javafx.geometry.Side.TOP;
import static javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED;
import static javafx.scene.control.TabPane.TabClosingPolicy.UNAVAILABLE;

/**
 * @author sali
 */
public class ControlPane extends BorderPane {

    private final ObjectProperty<CanvasData> canvasDataObject;

    private DependencyGraphTreeView tree;
    private DependencyGraphBuilderEditorPane editorPane;

    public ControlPane(CanvasData canvasData) {

        canvasDataObject = new SimpleObjectProperty<>(canvasData);

        VBox vBox = new VBox(10);
        vBox.setAlignment(CENTER);
        vBox.setPadding(new Insets(5, 5, 5, 5));

        ObservableList<GraphNode> nodes = canvasData.getNodes();

        // init tree
        tree = new DependencyGraphTreeView(nodes);

        // init editor pane
        GraphNode graphNode = new TerminalNode();
        if (!nodes.isEmpty()) {
            graphNode = nodes.get(0);
        }
        editorPane = new DependencyGraphBuilderEditorPane(graphNode);

        canvasData.selectedNodeProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                editorPane.initPane(newValue);
            }
        });

        tree.selectedNodeProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                canvasData.setSelectedNode(newValue);
            }
        });


        PropertiesPane propertiesPane = new PropertiesPane(canvasData.getCanvasMetaData());
        propertiesPane.getWidthField().valueProperty().addListener((observable, oldValue, newValue) -> {
            editorPane.setCanvasWidth(newValue);
        });
        propertiesPane.getHeightField().valueProperty().addListener((observable, oldValue, newValue) -> {
            editorPane.setCanvasHeight(newValue);
        });

        TabPane tabPane = new TabPane();
        tabPane.setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        tabPane.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        tabPane.setRotateGraphic(false);
        tabPane.setTabClosingPolicy(UNAVAILABLE);
        tabPane.setSide(TOP);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(editorPane);

        // removing tree tab for now, it should be removed all together in future commits
        tabPane.getTabs().addAll(new Tab("Properties", propertiesPane),
                new Tab("Dependency Graph Tree", initTreePane()),
                new Tab("Dependency Graph Controls", borderPane));

        setCenter(tabPane);
        canvasDataObject.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateBuilderPane(newValue);
            }
        });

        setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
    }

    public final ObjectProperty<CanvasData> canvasDataObjectProperty() {
        return canvasDataObject;
    }

    private void updateBuilderPane(CanvasData newValue) {
        tree.initChildren(newValue.getNodes());
        requestLayout();
    }

    private Pane initTreePane() {
        BorderPane borderPane = new BorderPane();
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(AS_NEEDED);
        scrollPane.setVbarPolicy(AS_NEEDED);
        scrollPane.setContent(tree);
        borderPane.setCenter(scrollPane);
        borderPane.setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        borderPane.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        return borderPane;
    }

}
