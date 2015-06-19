package com.alphasystem.morphologicalanalysis.ui.dependencygraph.components;

import com.alphasystem.morphologicalanalysis.graph.model.Relationship;
import com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.*;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.DependencyGraphGraphicTool;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.GraphBuilder;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.RelationshipType;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType.EMPTY;
import static com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType.HIDDEN;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.*;
import static com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.DependencyGraphGraphicTool.DARK_GRAY_CLOUD;
import static com.alphasystem.util.AppUtil.isGivenType;
import static java.lang.String.format;
import static javafx.scene.control.Alert.AlertType.INFORMATION;
import static javafx.scene.paint.Color.*;
import static javafx.scene.text.Font.font;
import static javafx.scene.text.FontPosture.REGULAR;
import static javafx.scene.text.TextAlignment.CENTER;
import static javafx.scene.text.TextAlignment.RIGHT;

/**
 * @author sali
 */
public class CanvasPane extends Pane {

    public static final String TERMINAL_GROUP_ID_PREFIX = "terminal";
    private static final double RADIUS = 2.0;
    private final ObjectProperty<CanvasData> canvasDataObject;
    private final ContextMenu terminalContextMenu;
    private final ContextMenu relationshipContextMenu;
    private RelationshipSelectionDialog relationshipSelectionDialog;
    private PhraseSelectionDialog phraseSelectionDialog;
    private DependencyGraphGraphicTool tool = DependencyGraphGraphicTool.getInstance();
    private GraphBuilder graphBuilder = GraphBuilder.getInstance();
    private Pane canvasPane;
    private Text selectedArabicText;
    private LinkSupport dependentLinkNode;
    private LinkSupport ownerLinkNode;
    private Node gridLines;

    public CanvasPane(CanvasData data) {
        this.canvasDataObject = new SimpleObjectProperty<>(data);

        CanvasMetaData metaData = data.getCanvasMetaData();
        int width = metaData.getWidth();
        int height = metaData.getHeight();
        terminalContextMenu = new ContextMenu();
        initTerminalContextMenu();
        relationshipContextMenu = new ContextMenu();
        relationshipSelectionDialog = new RelationshipSelectionDialog();
        phraseSelectionDialog = new PhraseSelectionDialog();
        initListeners();

        canvasPane = new Pane();
        initCanvas();

        canvasPane.setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        canvasPane.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        canvasPane.setPrefSize(width, height);
        canvasPane.setBackground(new Background(new BackgroundFill(BEIGE, null, null)));

        getChildren().add(canvasPane);

        setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        setPrefSize(width + 200, height + 200);
    }

    private void initTerminalContextMenu() {
        MenuItem menuItem = new MenuItem("Select Phrase");
        menuItem.setOnAction(event -> {
            if (selectedArabicText != null) {
                selectPhrase(selectedArabicText);
            }
        });
        terminalContextMenu.getItems().add(menuItem);

        menuItem = new MenuItem("Add Empty Node");
        menuItem.setOnAction(event -> addEmptyNode());
        terminalContextMenu.getItems().add(menuItem);
    }

    private MenuItem createRelationshipMenuItem(TerminalNode terminalNode, LinkSupport ownerNode) {
        String value = format("%s - %s", terminalNode.getText(), ownerNode.getText());
        Text text = new Text(value);
        text.setFont(ARABIC_FONT_SMALL_BOLD);
        MenuItem menuItem = new MenuItem("", text);
        menuItem.setUserData(ownerNode);
        menuItem.setOnAction(event -> {
            MenuItem source = (MenuItem) event.getSource();
            relationshipSelectionDialog.reset();
            relationshipSelectionDialog.setDependentNode(dependentLinkNode);
            ownerLinkNode = (LinkSupport) source.getUserData();
            relationshipSelectionDialog.setOwnerNode(ownerLinkNode);
            Optional<Relationship> result = relationshipSelectionDialog.showAndWait();
            result.ifPresent(relationship -> {
                addRelationship(relationship);
            });
        });
        return menuItem;
    }

    private void initRelationshipContextMenu(String currentPosId) {
        ObservableList<MenuItem> items = relationshipContextMenu.getItems();
        items.remove(0, items.size());

        Menu menu = new Menu("Make Relationship");
        ObservableList<GraphNode> nodes = canvasDataObject.get().getNodes();
        nodes.forEach(graphNode -> {
            GraphNodeType nodeType = graphNode.getNodeType();
            switch (nodeType) {
                case TERMINAL:
                case EMPTY:
                    TerminalNode tn = (TerminalNode) graphNode;
                    ObservableList<PartOfSpeechNode> partOfSpeeches = tn.getPartOfSpeeches();
                    for (PartOfSpeechNode partOfSpeech : partOfSpeeches) {
                        if (currentPosId.equals(partOfSpeech.getId())) {
                            continue;
                        }
                        menu.getItems().add(createRelationshipMenuItem(tn, partOfSpeech));
                    }
                    break;
                case PHRASE:

                    break;
            }
        });
        items.add(menu);
    }

    private void addEmptyNode() {
        Group parent = (Group) selectedArabicText.getParent();
        ObservableList<Node> children = parent.getChildren();
        Line line = null;
        for (Node child : children) {
            if (isGivenType(Line.class, child)) {
                line = (Line) child;
                break;
            }
        }
        if (line == null) {
            showAlert(INFORMATION, "No line found.", null);
            return;
        }
        addEmptyNode(line);
    }

    private void initListeners() {
        CanvasMetaData metaData = canvasDataObject.get().getCanvasMetaData();
        metaData.widthProperty().addListener((observable, oldValue, newValue) -> {
            resizeCanvas();
        });
        metaData.heightProperty().addListener((observable, oldValue, newValue) -> {
            resizeCanvas();
        });
        metaData.showGridLinesProperty().addListener((observable, oldValue, newValue) -> {
            toggleGridLines(newValue, metaData.isShowOutLines());
        });
        metaData.showOutLinesProperty().addListener((observable, oldValue, newValue) -> {
            toggleGridLines(metaData.isShowGridLines(), newValue);
        });
        metaData.debugModeProperty().addListener((observable, oldValue, newValue) -> {
            initCanvas();
        });
        canvasDataObject.addListener((observable, oldValue, newData) -> {
            if (newData != null) {
                ObservableList<GraphNode> nodes = newData.getNodes();
                if (nodes != null && !nodes.isEmpty()) {
                    drawNodes(nodes, false);
                }
            }
        });
    }

    @SuppressWarnings({"unused"})
    public CanvasData getCanvasDataObject() {
        return canvasDataObject.get();
    }

    @SuppressWarnings({"unused"})
    public void setCanvasDataObject(CanvasData canvasDataObject) {
        this.canvasDataObject.set(canvasDataObject);
    }

    public final ObjectProperty<CanvasData> canvasDataObjectProperty() {
        return canvasDataObject;
    }

    private void toggleGridLines(boolean showGridLines, boolean showOutline) {
        canvasPane.getChildren().remove(gridLines);
        gridLines = null;
        if (showOutline || showGridLines) {
            CanvasMetaData metaData = canvasDataObject.get().getCanvasMetaData();
            int width = metaData.getWidth();
            int height = metaData.getHeight();
            gridLines = tool.drawGridLines(showGridLines, width, height);
            canvasPane.getChildren().add(gridLines);
        }
    }

    private void resizeCanvas() {
        CanvasMetaData metaData = canvasDataObject.get().getCanvasMetaData();

        int width = metaData.getWidth();
        int height = metaData.getHeight();
        canvasPane.setPrefSize(width, height);

        setPrefSize(width + 200, height + 200);

        toggleGridLines(metaData.isShowGridLines(), metaData.isShowOutLines());
        requestLayout();
    }

    private void initCanvas() {
        CanvasMetaData metaData = canvasDataObject.get().getCanvasMetaData();
        int size = canvasPane.getChildren().size();
        canvasPane.getChildren().remove(0, size);
        boolean showOutline = metaData.isShowOutLines();
        boolean showGridLines = metaData.isShowGridLines();
        int width = metaData.getWidth();
        int height = metaData.getHeight();
        canvasPane.setPrefSize(width, height);

        ObservableList<GraphNode> nodes = canvasDataObject.get().getNodes();
        System.out.println("<<<<<<<<<<<<<<< " + nodes);
        if (nodes != null && !nodes.isEmpty()) {
            drawNodes(nodes, true);
        }

        if (showOutline || showGridLines) {
            gridLines = tool.drawGridLines(showGridLines, width, height);
            canvasPane.getChildren().add(gridLines);
        }

        setPrefSize(width + 200, height + 200);
        requestLayout();
    }

    private void removeAll(boolean removeGridLines) {
        ObservableList<Node> children = canvasPane.getChildren();
        Iterator<Node> iterator = children.iterator();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            String id = node.getId();
            if ("gridLines".equals(id) && !removeGridLines) {
                continue;
            }
            iterator.remove();
        }

    }

    private void drawNodes(ObservableList<GraphNode> nodes, boolean removeGridLines) {
        removeAll(removeGridLines);
        for (GraphNode node : nodes) {
            GraphNodeType nodeType = node.getNodeType();
            System.out.println(">>>>>>>>>>>>>>> " + nodeType);
            switch (nodeType) {
                case TERMINAL:
                case EMPTY:
                    buildTerminalNode((TerminalNode) node);
                    break;
                case RELATIONSHIP:
                    buildRelationshipNode((RelationshipNode) node);
                    break;
                case PHRASE:
                    buildPhraseNode((PhraseNode) node);
                    break;
                default:
                    break;
            }
        }
    }

    private void addEmptyNode(Line referenceLine) {
        // Step 1: call GraphBuilder to create an empty node
        EmptyNode emptyNode = graphBuilder.buildEmptyNode(referenceLine);

        // add this node into existing list of nodes and
        // update canvasDataObject for changes to take effect
        CanvasData canvasData = canvasDataObject.get();
        canvasData.getNodes().add(emptyNode);
        canvasDataObject.setValue(null);
        canvasDataObject.setValue(canvasData);
    }

    private void addRelationship(Relationship relationship) {
        RelationshipNode relationshipNode = graphBuilder.buildRelationshipNode(relationship,
                dependentLinkNode, ownerLinkNode);

        buildRelationshipNode(relationshipNode);
    }

    private void addPhrase(PhraseSelectionModel model) {
        // Step 1: call GraphBuilder to create a phrase
        PhraseNode phraseNode = graphBuilder.buildPhraseNode(null, model);

        // add this node into existing list of nodes and
        // update canvasDataObject for changes to take effect
        CanvasData canvasData = canvasDataObject.get();
        canvasData.getNodes().add(phraseNode);
        canvasDataObject.setValue(null);
        canvasDataObject.setValue(canvasData);
    }

    private Line drawLine(LineSupport node) {
        Line line = tool.drawLine(node.getId(), node.getX1(), node.getY1(), node.getX2(), node.getY2(),
                DARK_GRAY_CLOUD, 0.5);

        // bind line co-ordinates
        line.startXProperty().bind(node.x1Property());
        line.startYProperty().bind(node.y1Property());
        line.endXProperty().bind(node.x2Property());
        line.endYProperty().bind(node.y2Property());

        return line;
    }

    private Text drawText(GraphNode node, Color color, Font font) {
        Text arabicText = tool.drawText(node.getId(), node.getText(), RIGHT, color,
                node.getX(), node.getY(), font);
        arabicText.setUserData(node);

        // bind text x and y locations
        arabicText.textProperty().bind(node.textProperty());
        arabicText.xProperty().bind(node.xProperty());
        arabicText.yProperty().bind(node.yProperty());

        return arabicText;
    }

    private void buildTerminalNode(TerminalNode tn) {
        Line line = drawLine(tn);

        GraphNodeType nodeType = tn.getNodeType();
        boolean hiddenOrEmptyNode = nodeType.equals(EMPTY) || nodeType.equals(HIDDEN);
        Color hiddenOrEmptyNodeColor = LIGHTGRAY.darker();
        Color color = hiddenOrEmptyNode ? hiddenOrEmptyNodeColor : BLACK;

        String tokenId = "";
        String trans = "";
        Token token = tn.getToken();
        if (token != null) {
            tokenId = token.getDisplayName();
            trans = token.getTranslation();
            List<Location> locations = token.getLocations();
            if (token.getLocationCount() == 1) {
                Location location = locations.get(0);
                color = web(location.getPartOfSpeech().getColorCode());
            }
        }


        Text arabicText = drawText(tn, color, ARABIC_FONT_BIG);

        double translateX = tn.getTranslateX();
        double translateY = tn.getTranslateY();
        arabicText.setOnMouseClicked(event -> {
            selectedArabicText = (Text) event.getSource();
            if (event.isPopupTrigger()) {
                terminalContextMenu.show(selectedArabicText, event.getScreenX(), event.getScreenY());
            }
        });

        String id = format("trans_%s", tn.getId());
        Text englishText = tool.drawText(id, trans, CENTER, BLACK,
                tn.getX3(), tn.getY3(), font("Candara", REGULAR, 16));
        // bind text x and y locations
        englishText.xProperty().bind(tn.x3Property());
        englishText.yProperty().bind(tn.y3Property());

        Group group = new Group();

        group.setId(format("%s_%s", TERMINAL_GROUP_ID_PREFIX, tokenId));
        group.getChildren().addAll(englishText, arabicText, line);

        ObservableList<PartOfSpeechNode> partOfSpeeches = tn.getPartOfSpeeches();
        for (PartOfSpeechNode pn : partOfSpeeches) {
            color = hiddenOrEmptyNode ? hiddenOrEmptyNodeColor : web(pn.getPartOfSpeech().getColorCode());
            arabicText = drawText(pn, color, ARABIC_FONT_SMALL);
            arabicText.setOnMouseClicked(event -> {
                Text source = (Text) event.getSource();
                String thisId = pn.getId();
                if (event.isPopupTrigger()) {
                    ownerLinkNode = null;
                    dependentLinkNode = pn;
                    initRelationshipContextMenu(thisId);
                    relationshipContextMenu.show(source, event.getScreenX(), event.getScreenY());
                }
                // selectRelationship(pn.getText(), pn.getCx(), pn.getCy(), translateX, translateY, source);
            });

            id = format("c_%s", pn.getId());
            Circle circle = tool.drawCircle(id, color, pn.getCx(), pn.getCy(), RADIUS);
            // bind coordinates
            circle.centerXProperty().bind(pn.cxProperty());
            circle.centerYProperty().bind(pn.cyProperty());

            group.getChildren().addAll(arabicText, circle);
        }

        group.setTranslateX(translateX);
        group.setTranslateY(translateY);

        tn.translateXProperty().addListener((observable, oldValue, newValue) -> {
            group.setTranslateX((Double) newValue);
        });
        tn.translateYProperty().addListener((observable, oldValue, newValue) -> {
            group.setTranslateY((Double) newValue);
        });

        canvasPane.getChildren().add(group);
    }

    private void selectPhrase(Text text) {
        TerminalNode userData = (TerminalNode) text.getUserData();
        double translateX = userData.getTranslateX();
        double translateY = userData.getTranslateY();

        PhraseSelectionModel phraseSelectionModel = phraseSelectionDialog.getPhraseSelectionModel();
        if (phraseSelectionModel.getFirstNode() == null) {
            phraseSelectionModel.setFirstNode(userData);
        } else {
            phraseSelectionModel.setLastNode(userData);
        }

        Optional<PhraseSelectionModel> result = phraseSelectionDialog.showAndWait();
        result.ifPresent(psm -> {
            RelationshipType relationship = psm.getRelationship();
            if (relationship == null || relationship.equals(RelationshipType.NONE)) {
                return;
            }
            addPhrase(psm);
            phraseSelectionDialog.reset();
        });
    }

    private void selectRelationship(String text, double cx, double cy, double translateX, double translateY,
                                    Text arabicText) {

    }

    private void buildRelationshipNode(RelationshipNode rn) {
        Color color = rn.getStroke();
        CubicCurve cubicCurve = tool.drawCubicCurve(rn.getId(), rn.getStartX(), rn.getStartY(), rn.getControlX1(),
                rn.getControlY1(), rn.getControlX2(), rn.getControlY2(), rn.getEndX(), rn.getEndY(),
                color);

        // bind line co-ordinates
        //TODO: bind start and end properties with the circle of part of speech
        cubicCurve.startYProperty().bind(rn.startXProperty());
        cubicCurve.startYProperty().bind(rn.startYProperty());
        cubicCurve.controlX1Property().bind(rn.controlX1Property());
        cubicCurve.controlY1Property().bind(rn.controlY1Property());
        cubicCurve.controlX2Property().bind(rn.controlX2Property());
        cubicCurve.controlY2Property().bind(rn.controlY2Property());
        cubicCurve.endYProperty().bind(rn.endXProperty());
        cubicCurve.endYProperty().bind(rn.endYProperty());
        cubicCurve.strokeProperty().bind(rn.strokeProperty());

        Text arabicText = drawText(rn, color, ARABIC_FONT_SMALL);
        arabicText.fillProperty().bind(rn.strokeProperty());

        // small arrow pointing towards the relationship direction
        Polyline triangle = tool.drawPolyline(rn.getCurvePointX(), rn.getCurvePointY(), rn.getArrowPointX1(),
                rn.getArrowPointY1(), rn.getArrowPointX2(), rn.getArrowPointY2(), color);
        // bind line co-ordinates
        //TODO: need to figure out how to bind polyline
        // commented following code as it causes application to crashed
//        rn.t1Property().addListener((observable, oldValue, newValue) -> {
//            runLater(this::initCanvas);
//        });
//        rn.t2Property().addListener((observable, oldValue, newValue) -> {
//            runLater(this::initCanvas);
//        });

        Group group = new Group();

        group.getChildren().addAll(cubicCurve, arabicText, triangle);

        if (canvasDataObject.get().getCanvasMetaData().isDebugMode()) {
            Path path = tool.drawCubicCurveBounds(rn.getStartX(), rn.getStartY(), rn.getControlX1(),
                    rn.getControlY1(), rn.getControlX2(), rn.getControlY2(), rn.getEndX(), rn.getEndY());
            group.getChildren().add(path);
        }
        canvasPane.getChildren().add(group);
    }

    private void buildPhraseNode(PhraseNode pn) {
        Color color = web(pn.getGrammaticalRelationship().getColorCode());

        Line line = drawLine(pn);
        Text arabicText = drawText(pn, color, ARABIC_FONT_SMALL);
        arabicText.setOnMouseClicked(event -> selectRelationship(pn.getText(),
                pn.getCx(), pn.getCy(), 0d, 0d, (Text) event.getSource()));
        Circle circle = tool.drawCircle(null, color, pn.getCx(), pn.getCy(), RADIUS);
        // bind coordinates
        circle.centerXProperty().bind(pn.cxProperty());
        circle.centerYProperty().bind(pn.cyProperty());

        Group group = new Group();
        group.getChildren().addAll(line, arabicText, circle);
        canvasPane.getChildren().add(group);
    }

    private Optional<ButtonType> showAlert(Alert.AlertType type, String contentText, String headerText) {
        Alert alert = new Alert(type);
        if (headerText != null) {
            alert.setHeaderText(headerText);
        }
        alert.setContentText(contentText);
        return alert.showAndWait();
    }

    public Pane getCanvasPane() {
        return canvasPane;
    }
}
