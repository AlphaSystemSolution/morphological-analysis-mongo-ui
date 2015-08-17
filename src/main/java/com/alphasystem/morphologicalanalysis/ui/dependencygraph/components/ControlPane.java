package com.alphasystem.morphologicalanalysis.ui.dependencygraph.components;

import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.*;
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

import static com.alphasystem.util.AppUtil.isGivenType;
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
        final CanvasMetaData canvasMetaData = canvasData.getCanvasMetaData();
        editorPane = new DependencyGraphBuilderEditorPane(graphNode,
                canvasMetaData.getWidth(), canvasMetaData.getHeight());

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


        PropertiesPane propertiesPane = new PropertiesPane(canvasMetaData);
        propertiesPane.getWidthField().valueProperty().addListener((observable, oldValue, newValue) -> {
            editorPane.setCanvasWidth(newValue);
        });
        propertiesPane.getHeightField().valueProperty().addListener((observable, oldValue, newValue) -> {
            editorPane.setCanvasHeight(newValue);
        });
        propertiesPane.getAlignTokensYField().getValueFactory().setValue(graphNode.getY());
        propertiesPane.getAlignTokensYField().valueProperty().addListener((observable, oldValue, newValue) -> {
            nodes.stream().filter(gn -> isGivenType(TerminalNode.class, gn)).forEach(gn -> gn.setY(newValue));
        });
        if(isGivenType(TerminalNode.class, graphNode)){
            TerminalNode terminalNode = (TerminalNode) graphNode;
            propertiesPane.getAlignTranslationsY().getValueFactory().setValue(terminalNode.getY3());
            ObservableList<PartOfSpeechNode> partOfSpeeches = terminalNode.getPartOfSpeeches();
            if(!partOfSpeeches.isEmpty()){
                PartOfSpeechNode partOfSpeechNode = partOfSpeeches.get(0);
                propertiesPane.getAlignPOSsYField().getValueFactory().setValue(partOfSpeechNode.getY());
                propertiesPane.getAlignPOSControlYField().getValueFactory().setValue(partOfSpeechNode.getCy());
            }
        }
        propertiesPane.getAlignTranslationsY().valueProperty().addListener((observable, oldValue, newValue) -> {
            nodes.stream().filter(gn -> isGivenType(TerminalNode.class, gn)).forEach(
                    gn -> ((TerminalNode)gn).setY3(newValue));
        });
        propertiesPane.getAlignPOSsYField().valueProperty().addListener((observable, oldValue, newValue) -> {
            nodes.stream().filter(gn -> isGivenType(TerminalNode.class, gn)).forEach(gn -> {
                TerminalNode terminalNode = (TerminalNode) gn;
                ObservableList<PartOfSpeechNode> partOfSpeeches = terminalNode.getPartOfSpeeches();
                if(!partOfSpeeches.isEmpty()){
                    partOfSpeeches.forEach(partOfSpeechNode -> partOfSpeechNode.setY(newValue));
                }
            });
        });
        propertiesPane.getAlignPOSControlYField().valueProperty().addListener((observable, oldValue, newValue) -> {
            nodes.stream().filter(gn -> isGivenType(TerminalNode.class, gn)).forEach(gn -> {
                TerminalNode terminalNode = (TerminalNode) gn;
                ObservableList<PartOfSpeechNode> partOfSpeeches = terminalNode.getPartOfSpeeches();
                if(!partOfSpeeches.isEmpty()){
                    partOfSpeeches.forEach(partOfSpeechNode -> partOfSpeechNode.setCy(newValue));
                }
            });
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
