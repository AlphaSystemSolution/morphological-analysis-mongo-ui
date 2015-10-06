package com.alphasystem.morphologicalanalysis.ui.dependencygraph.components;

import com.alphasystem.arabic.model.ArabicWord;
import com.alphasystem.morphologicalanalysis.graph.model.*;
import com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.*;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.CanvasUtil;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.DependencyGraphGraphicTool;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.GraphBuilder;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.PartOfSpeech;
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
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType.PHRASE;
import static com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType.TERMINAL;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.*;
import static com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.CanvasUtil.getLocationWord;
import static com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.CubicCurveHelper.arrowPoints;
import static com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.DependencyGraphGraphicTool.DARK_GRAY_CLOUD;
import static com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.DependencyGraphGraphicTool.GRID_LINES;
import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.PartOfSpeech.NOUN;
import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.PartOfSpeech.VERB;
import static com.alphasystem.util.AppUtil.isGivenType;
import static java.lang.String.format;
import static java.util.Collections.reverse;
import static javafx.collections.FXCollections.observableArrayList;
import static javafx.geometry.NodeOrientation.RIGHT_TO_LEFT;
import static javafx.scene.control.Alert.AlertType.INFORMATION;
import static javafx.scene.paint.Color.*;
import static javafx.scene.text.TextAlignment.CENTER;
import static javafx.scene.text.TextAlignment.RIGHT;

/**
 * @author sali
 */
public class CanvasPane extends Pane {

    private static final String TERMINAL_GROUP_ID_PREFIX = "terminal";
    private static final Color DEFAULT_COLOR = BLACK;
    private static final Color NON_TERMINAL_COLOR = LIGHTGRAY.darker();
    private static final double RADIUS = 2.0;

    private final ObjectProperty<DependencyGraphAdapter> dependencyGraph;
    private final ContextMenu contextMenu;

    private GraphMetaInfoAdapter metaInfo = new GraphMetaInfoAdapter(new GraphMetaInfo());
    private DependencyGraphGraphicTool tool = DependencyGraphGraphicTool.getInstance();
    private CanvasUtil canvasUtil = CanvasUtil.getInstance();
    private GraphBuilder graphBuilder = GraphBuilder.getInstance();
    private RelationshipSelectionDialog relationshipSelectionDialog;
    private PhraseSelectionDialog phraseSelectionDialog;
    private ReferenceSelectionDialog referenceSelectionDialog;
    private Pane canvasPane;
    private Node gridLines;

    public CanvasPane(DependencyGraphAdapter src) {
        relationshipSelectionDialog = new RelationshipSelectionDialog();
        phraseSelectionDialog = new PhraseSelectionDialog();
        referenceSelectionDialog = new ReferenceSelectionDialog();
        contextMenu = new ContextMenu();
        dependencyGraph = new SimpleObjectProperty<>();
        src = (src == null) ? new DependencyGraphAdapter(new DependencyGraph()) : src;
        metaInfo = src.getGraphMetaInfo();

        Double canvasWidth = metaInfo.getWidth();
        Double canvasHeight = metaInfo.getHeight();

        canvasPane = new Pane();
        canvasPane.setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        canvasPane.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        canvasPane.setPrefSize(canvasWidth, canvasHeight);
        canvasPane.setBackground(new Background(new BackgroundFill(BEIGE, null, null)));
        initListeners();
        setDependencyGraph(src);

        GraphMetaInfoAdapter graphMetaInfo = getDependencyGraph().getGraphMetaInfo();
        graphMetaInfo.widthProperty().addListener((observable, oldValue, newValue) -> {
            metaInfo.setWidth((Double) newValue);
            initCanvas();
        });
        graphMetaInfo.heightProperty().addListener((observable, oldValue, newValue) -> {
            metaInfo.setHeight((Double) newValue);
            initCanvas();
        });
        graphMetaInfo.showGridLinesProperty().addListener((observable, oldValue, newValue) -> {
            try {
                metaInfo.setShowGridLines(newValue);
            } catch (Exception e) {
            }
            toggleGridLines();
        });
        graphMetaInfo.showOutLinesProperty().addListener((observable, oldValue, newValue) -> {
            try {
                metaInfo.setShowGridLines(newValue);
            } catch (Exception e) {
            }
            toggleGridLines();
        });
        graphMetaInfo.debugModeProperty().addListener((observable, oldValue, newValue) -> {
            try {
                metaInfo.setDebugMode(newValue);
            } catch (Exception e) {
            }
            toggleGridLines();
        });

        getChildren().add(canvasPane);
        setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        setPrefSize(canvasWidth + 200, canvasHeight + 200);
    }

    private void initListeners() {
        dependencyGraphProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                metaInfo = newValue.getGraphMetaInfo();
                initCanvas();
            }
        });
    }

    private void initCanvas(Double width, Double height) {
        // clear canvas content
        removeAll(true);

        GraphMetaInfoAdapter metaInfo = getDependencyGraph().getGraphMetaInfo();

        // resize the canvas
        canvasPane.setPrefSize(width, height);
        setPrefSize(width + 200, height + 200);

        List<GraphNodeAdapter> nodes = getDependencyGraph().getGraphNodes();
        drawNodes(nodes, true);

        boolean showOutline = metaInfo.isShowOutLines();
        boolean showGridLines = metaInfo.isShowGridLines();
        if (showOutline || showGridLines) {
            gridLines = tool.drawGridLines(showGridLines, width.intValue(), height.intValue());
            canvasPane.getChildren().add(gridLines);
        }

        requestLayout();
    }

    private void initCanvas() {
        // clear canvas content
        removeAll(true);

        Double canvasWidth = metaInfo.getWidth();
        Double canvasHeight = metaInfo.getHeight();
        // resize the canvas
        canvasPane.setPrefSize(canvasWidth, canvasHeight);
        setPrefSize(canvasWidth + 200, canvasHeight + 200);

        List<GraphNodeAdapter> nodes = getDependencyGraph().getGraphNodes();
        drawNodes(nodes, true);

        boolean showOutline = metaInfo.isShowOutLines();
        boolean showGridLines = metaInfo.isShowGridLines();
        if (showOutline || showGridLines) {
            gridLines = tool.drawGridLines(showGridLines, canvasWidth.intValue(), canvasHeight.intValue());
            canvasPane.getChildren().add(gridLines);
        }

        requestLayout();
    }

    private void drawNodes(List<GraphNodeAdapter> nodes, boolean removeGridLines) {
        removeAll(removeGridLines);
        if (nodes != null && !nodes.isEmpty()) {
            nodes.forEach(graphNode -> {
                switch (graphNode.getGraphNodeType()) {
                    case TERMINAL:
                    case IMPLIED:
                    case HIDDEN:
                    case REFERENCE:
                        drawTerminalNode((TerminalNodeAdapter) graphNode);
                        break;
                    case PHRASE:
                        drawPhraseNode((PhraseNodeAdapter) graphNode);
                        break;
                    case RELATIONSHIP:
                        drawRelationshipNode((RelationshipNodeAdapter) graphNode);
                        break;
                    default:
                        break;
                }
            });
        }
    }

    /**
     * @param removeGridLines
     */
    private void removeAll(boolean removeGridLines) {
        ObservableList<Node> children = canvasPane.getChildren();
        Iterator<Node> iterator = children.iterator();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            String id = node.getId();
            if (GRID_LINES.equals(id) && !removeGridLines) {
                continue;
            }
            iterator.remove();
        }
    }

    private void toggleGridLines() {
        boolean showGridLines = metaInfo.isShowGridLines();
        boolean showOutline = metaInfo.isShowOutLines();
        canvasPane.getChildren().remove(gridLines);
        gridLines = null;
        if (showOutline || showGridLines) {
            int width = (int) metaInfo.getWidth();
            int height = (int) metaInfo.getHeight();
            gridLines = tool.drawGridLines(showGridLines, width, height);
            canvasPane.getChildren().add(gridLines);
        }
    }

    @SuppressWarnings({"unused"})
    private void resizeCanvas() {
        GraphMetaInfoAdapter metaInfo = getDependencyGraph().getGraphMetaInfo();
        Double width = metaInfo.getWidth();
        Double height = metaInfo.getHeight();
        canvasPane.setPrefSize(width, height);
        setPrefSize(width + 200, height + 200);

        requestLayout();
    }

    private void drawTerminalNode(TerminalNodeAdapter tn) {
        Line line = drawLine(tn);

        Token token = tn.getSrc().getToken();
        boolean nonTerminal = NON_TERMINALS.contains(tn.getGraphNodeType());
        Color color = nonTerminal ? NON_TERMINAL_COLOR : DEFAULT_COLOR;

        String tokenId = token.getDisplayName();

        final Text arabicText = drawText(tn, color);
        arabicText.setOnMouseClicked(event -> {
            if (event.isPopupTrigger()) {
                initTerminalContextMenu(arabicText);
                contextMenu.show(arabicText, event.getScreenX(), event.getScreenY());
            } else {
                // by selecting the node, panels on the right hand side should be get updated
                //getDependencyGraph().setSelectedNode(null);
                getDependencyGraph().setSelectedNode(tn);
            }
        });

        Color transColor = nonTerminal ? NON_TERMINAL_COLOR : DEFAULT_COLOR;
        Text translationText = drawTranslation(tn, transColor);

        Group group = new Group();

        group.setId(format("%s_%s", TERMINAL_GROUP_ID_PREFIX, tokenId));
        group.getChildren().addAll(translationText, arabicText, line);
        drawPartOfSpeechNodes(tn, group, nonTerminal);

        group.setTranslateX(tn.getTranslateX());
        group.setTranslateY(tn.getTranslateY());

        tn.translateXProperty().addListener((observable, oldValue, newValue) -> {
            Double x = (Double) newValue;
            group.setTranslateX(x);
            tn.getPartOfSpeeches().forEach(partOfSpeechNode -> partOfSpeechNode.setTranslateX(x));
        });
        tn.translateYProperty().addListener((observable, oldValue, newValue) -> {
            Double y = (Double) newValue;
            group.setTranslateY(y);
            tn.getPartOfSpeeches().forEach(partOfSpeechNode -> partOfSpeechNode.setTranslateY(y));
        });

        canvasPane.getChildren().add(group);
    }

    private void drawPartOfSpeechNodes(TerminalNodeAdapter tn, Group group, boolean nonTerminal) {
        tn.getPartOfSpeeches().forEach(pn -> {
            pn.setTranslateX(tn.getTranslateX());
            pn.setTranslateY(tn.getTranslateY());
            PartOfSpeech partOfSpeech = pn.getSrc().getLocation().getPartOfSpeech();
            Color color = nonTerminal ? NON_TERMINAL_COLOR :
                    web(partOfSpeech.getColorCode());
            Text posArabicText = drawText(pn, color);
            posArabicText.setOnMouseClicked(event -> {
                if (event.isPopupTrigger()) {
                    initPartOfSpeechContextMenu(pn);
                    contextMenu.show(posArabicText, event.getScreenX(), event.getScreenY());
                } else {
                    getDependencyGraph().setSelectedNode(pn);
                }
            });
            String id = format("c_%s", pn.getSrc().getDisplayName());
            Circle circle = tool.drawCircle(id, color, pn.getCx(), pn.getCy(), RADIUS);
            // bind coordinates
            circle.centerXProperty().bind(pn.cxProperty());
            circle.centerYProperty().bind(pn.cyProperty());
            group.getChildren().addAll(posArabicText, circle);
        });
    }

    private void drawPhraseNode(PhraseNodeAdapter pn) {
        Color color = web(pn.getRelationshipType().getColorCode());
        Line line = drawLine(pn);
        Text arabicText = drawText(pn, color);
        arabicText.setUserData(pn);
        arabicText.setOnMouseClicked(event -> {
            if (event.isPopupTrigger()) {
                initPhraseNodeContextMenu(pn);
                contextMenu.show(arabicText, event.getScreenX(), event.getScreenY());
            } else {
                getDependencyGraph().setSelectedNode(pn);
            }
        });
        Circle circle = tool.drawCircle(null, color, pn.getCx(), pn.getCy(), RADIUS);
        // bind coordinates
        circle.centerXProperty().bind(pn.cxProperty());
        circle.centerYProperty().bind(pn.cyProperty());

        Group group = new Group();
        group.getChildren().addAll(line, arabicText, circle);
        canvasPane.getChildren().add(group);
    }

    private void drawRelationshipNode(RelationshipNodeAdapter rn) {
        Color color = rn.getStroke();
        LinkSupportAdapter dependent = rn.getDependent();
        LinkSupportAdapter owner = rn.getOwner();
        CubicCurve cubicCurve = tool.drawCubicCurve(rn.getId(), dependent.getCx() + dependent.getTranslateX(),
                dependent.getCy() + dependent.getTranslateY(), rn.getControlX1(), rn.getControlY1(), rn.getControlX2(),
                rn.getControlY2(), owner.getCx() + owner.getTranslateX(), owner.getCy() + owner.getTranslateY(), color);

        // bind line co-ordinates
        cubicCurve.startXProperty().bind(dependent.cxProperty().add(dependent.translateXProperty()));
        cubicCurve.startYProperty().bind(dependent.cyProperty().add(dependent.translateYProperty()));
        cubicCurve.controlX1Property().bind(rn.controlX1Property());
        cubicCurve.controlY1Property().bind(rn.controlY1Property());
        cubicCurve.controlX2Property().bind(rn.controlX2Property());
        cubicCurve.controlY2Property().bind(rn.controlY2Property());
        cubicCurve.endXProperty().bind(owner.cxProperty().add(owner.translateXProperty()));
        cubicCurve.endYProperty().bind(owner.cyProperty().add(owner.translateYProperty()));
        cubicCurve.strokeProperty().bind(rn.strokeProperty());

        Text arabicText = drawText(rn, web(rn.getRelationshipType().getColorCode()));
        arabicText.fillProperty().bind(rn.strokeProperty());
        arabicText.setOnMouseClicked(event -> {
            if (event.isPopupTrigger()) {

            } else {
                getDependencyGraph().setSelectedNode(rn);
            }
        });

        // small arrow pointing towards the relationship direction
        double[] arrowPoints = arrowPoints(rn.getT1(), rn.getT2(), cubicCurve.getStartX(), cubicCurve.getStartY(),
                cubicCurve.getControlX1(), cubicCurve.getControlY1(), cubicCurve.getControlX2(),
                cubicCurve.getControlY2(), cubicCurve.getEndX(), cubicCurve.getEndY());
        Polyline arrow = tool.drawPolyline(color, arrowPoints);
        rn.t1Property().addListener((observable, oldValue, newValue) -> {
            updateArrawPoints((Double) newValue, rn.getT2(), cubicCurve.getStartX(),
                    cubicCurve.getStartY(), cubicCurve.getControlX1(), cubicCurve.getControlY1(),
                    cubicCurve.getControlX2(), cubicCurve.getControlY2(), cubicCurve.getEndX(), cubicCurve.getEndY(),
                    arrow);
        });
        rn.t2Property().addListener((observable, oldValue, newValue) -> {
            updateArrawPoints(rn.getT1(), (Double) newValue, cubicCurve.getStartX(),
                    cubicCurve.getStartY(), cubicCurve.getControlX1(), cubicCurve.getControlY1(),
                    cubicCurve.getControlX2(), cubicCurve.getControlY2(), cubicCurve.getEndX(), cubicCurve.getEndY(),
                    arrow);
        });
        cubicCurve.startXProperty().addListener((observable, oldValue, newValue) -> {
            updateArrawPoints(rn.getT1(), rn.getT2(), (Double) newValue,
                    cubicCurve.getStartY(), cubicCurve.getControlX1(), cubicCurve.getControlY1(),
                    cubicCurve.getControlX2(), cubicCurve.getControlY2(), cubicCurve.getEndX(), cubicCurve.getEndY(),
                    arrow);
        });
        cubicCurve.startYProperty().addListener((observable, oldValue, newValue) -> {
            updateArrawPoints(rn.getT1(), rn.getT2(), cubicCurve.getStartX(),
                    (Double) newValue, cubicCurve.getControlX1(), cubicCurve.getControlY1(),
                    cubicCurve.getControlX2(), cubicCurve.getControlY2(), cubicCurve.getEndX(), cubicCurve.getEndY(),
                    arrow);
        });
        cubicCurve.controlX1Property().addListener((observable, oldValue, newValue) -> {
            updateArrawPoints(rn.getT1(), rn.getT2(), cubicCurve.getStartX(),
                    cubicCurve.getStartY(), (Double) newValue, cubicCurve.getControlY1(),
                    cubicCurve.getControlX2(), cubicCurve.getControlY2(), cubicCurve.getEndX(), cubicCurve.getEndY(),
                    arrow);
        });
        cubicCurve.controlY1Property().addListener((observable, oldValue, newValue) -> {
            updateArrawPoints(rn.getT1(), rn.getT2(), cubicCurve.getStartX(),
                    cubicCurve.getStartY(), rn.getControlX1(), (Double) newValue,
                    cubicCurve.getControlX2(), cubicCurve.getControlY2(), cubicCurve.getEndX(), cubicCurve.getEndY(),
                    arrow);
        });
        cubicCurve.controlX2Property().addListener((observable, oldValue, newValue) -> {
            updateArrawPoints(rn.getT1(), rn.getT2(), cubicCurve.getStartX(),
                    cubicCurve.getStartY(), rn.getControlX1(), cubicCurve.getControlY1(),
                    (Double) newValue, cubicCurve.getControlY2(), cubicCurve.getEndX(), cubicCurve.getEndY(),
                    arrow);
        });
        cubicCurve.controlY2Property().addListener((observable, oldValue, newValue) -> {
            updateArrawPoints(rn.getT1(), rn.getT2(), cubicCurve.getStartX(),
                    cubicCurve.getStartY(), rn.getControlX1(), rn.getControlY1(),
                    cubicCurve.getControlX2(), (Double) newValue, cubicCurve.getEndX(), cubicCurve.getEndY(),
                    arrow);
        });
        cubicCurve.endXProperty().addListener((observable, oldValue, newValue) -> {
            updateArrawPoints(rn.getT1(), rn.getT2(), cubicCurve.getStartX(),
                    cubicCurve.getStartY(), cubicCurve.getControlX1(), cubicCurve.getControlY1(),
                    cubicCurve.getControlX2(), cubicCurve.getControlY2(), (Double) newValue, cubicCurve.getEndY(),
                    arrow);
        });
        cubicCurve.endYProperty().addListener((observable, oldValue, newValue) -> {
            updateArrawPoints(rn.getT1(), rn.getT2(), cubicCurve.getStartX(),
                    cubicCurve.getStartY(), cubicCurve.getControlX1(), cubicCurve.getControlY1(),
                    cubicCurve.getControlX2(), cubicCurve.getControlY2(), cubicCurve.getEndX(), (Double) newValue,
                    arrow);
        });

        Group group = new Group();
        group.setId(format("rln_%s", rn.getSrc().getDisplayName()));

        group.getChildren().addAll(cubicCurve, arabicText, arrow);
        if (getDependencyGraph().getGraphMetaInfo().isDebugMode()) {
            Path path = tool.drawCubicCurveBounds(cubicCurve.getStartX(), cubicCurve.getStartY(), rn.getControlX1(),
                    rn.getControlY1(), rn.getControlX2(), rn.getControlY2(), cubicCurve.getEndX(), cubicCurve.getEndY());
            group.getChildren().add(path);
        }
        canvasPane.getChildren().add(group);
    }

    private void updateArrawPoints(double t1, double t2, double startX, double startY, double controlX1,
                                   double controlY1, double controlX2, double controlY2, double endX, double endY,
                                   Polyline arrow) {
        double[] newPoints = arrowPoints(t1, t2, startX, startY, controlX1, controlY1, controlX2, controlY2,
                endX, endY);
        Double[] elements = new Double[newPoints.length];
        for (int i = 0; i < newPoints.length; i++) {
            elements[i] = newPoints[i];
        }
        arrow.getPoints().setAll(elements);
    }

    private Line drawLine(LineSupportAdapter node) {
        Line line = tool.drawLine(node.getId(), node.getX1(), node.getY1(), node.getX2(), node.getY2(),
                DARK_GRAY_CLOUD, 0.5);

        // bind line co-ordinates
        line.startXProperty().bind(node.x1Property());
        line.startYProperty().bind(node.y1Property());
        line.endXProperty().bind(node.x2Property());
        line.endYProperty().bind(node.y2Property());

        return line;
    }

    private Text drawText(GraphNodeAdapter node, Color color) {
        Text arabicText = tool.drawText(node.getId(), node.getText(), RIGHT, color, node.getX(), node.getY(),
                node.getFont());
        arabicText.setUserData(node);

        // bind text x and y locations
        arabicText.textProperty().bind(node.textProperty());
        arabicText.xProperty().bind(node.xProperty());
        arabicText.yProperty().bind(node.yProperty());

        return arabicText;
    }

    private Text drawTranslation(TerminalNodeAdapter tn, Color color) {
        Text translationText = tool.drawText(format("trans_%s", tn.getId()), tn.getSrc().getToken().getTranslation(),
                CENTER, color, tn.getTranslationX(), tn.getTranslationY(), tn.getTranslationFont());

        translationText.xProperty().bind(tn.translationXProperty());
        translationText.yProperty().bind(tn.translationYProperty());
        // TODO: FONT

        return translationText;
    }

    private void initTerminalContextMenu(Text source) {
        ObservableList<MenuItem> items = contextMenu.getItems();
        items.remove(0, items.size());

        Menu menu;
        MenuItem menuItem;
        TerminalNodeAdapter userData = (TerminalNodeAdapter) source.getUserData();
        int index = getIndex(userData);

        menuItem = new MenuItem("Select");
        menuItem.setOnAction(event -> {
            getDependencyGraph().setSelectedNode(userData);
        });
        items.add(menuItem);

        menu = new Menu("Add Impled Node to Left");
        menu.getItems().addAll(createAddEmptyNodeMenuItem(index, NOUN), createAddEmptyNodeMenuItem(index, VERB));
        contextMenu.getItems().add(menu);

        menu = new Menu("Add Impled Node to Right");
        menu.getItems().addAll(createAddEmptyNodeMenuItem(index + 1, NOUN), createAddEmptyNodeMenuItem(index + 1, VERB));
        contextMenu.getItems().add(menu);

        menuItem = new MenuItem("Add Reference Node ...");
        menuItem.setOnAction(event -> makeReference(index));

        contextMenu.getItems().add(menuItem);
    }

    private void initPartOfSpeechContextMenu(PartOfSpeechNodeAdapter currentNode) {
        ObservableList<MenuItem> items = contextMenu.getItems();
        items.remove(0, items.size());

        Menu menu = new Menu("Make Relationship");
        addRelationMenuItems(currentNode, menu);
        contextMenu.getItems().add(menu);

        TerminalNodeAdapter parent = (TerminalNodeAdapter) currentNode.getParent();
        GraphNodeType graphNodeType = parent.getGraphNodeType();
        // If current part of speech has parent which is Empty, Reference, or Hidden, we will not display menu
        if (graphNodeType.equals(TERMINAL)) {
            menu = new Menu("Make Phrase");
            addPhraseMenuItems(currentNode, menu);
            contextMenu.getItems().add(menu);
        }
    }

    private void initPhraseNodeContextMenu(PhraseNodeAdapter currentNode) {
        ObservableList<MenuItem> items = contextMenu.getItems();
        items.clear();

        Menu menu = new Menu("Make Relationship");
        addRelationMenuItems(currentNode, menu);
        contextMenu.getItems().add(menu);
    }

    private void addPhraseMenuItems(PartOfSpeechNodeAdapter currentNode, Menu menu) {
        Location location = currentNode.getSrc().getLocation();
        ObservableList<GraphNodeAdapter> nodes = copyGraphNodes();
        nodes.forEach(graphNode -> {
            GraphNodeType nodeType = graphNode.getGraphNodeType();
            switch (nodeType) {
                case TERMINAL:
                    TerminalNodeAdapter tn = (TerminalNodeAdapter) graphNode;
                    ObservableList<PartOfSpeechNodeAdapter> partOfSpeeches = copyPartOfSpeechNodes(tn);
                    for (PartOfSpeechNodeAdapter partOfSpeech : partOfSpeeches) {
                        Location other = partOfSpeech.getSrc().getLocation();
                        boolean self = currentNode.getId().equals(partOfSpeech.getId());
                        //TODO: figure out why before is not working
                        if (self || isFakeTerminal(tn) || other.before(location)) {
                            continue;
                        }
                        menu.getItems().add(createPhraseMenuItem(currentNode, partOfSpeech));
                    }
                    break;
            }
        });
    }

    private MenuItem createPhraseMenuItem(final PartOfSpeechNodeAdapter firstPartOfSpeech,
                                          final PartOfSpeechNodeAdapter lastPartOfSpeech) {
        Text text = new Text(getRelationshipMenuItemText(lastPartOfSpeech));
        text.setNodeOrientation(RIGHT_TO_LEFT);
        text.setFont(ARABIC_FONT_SMALL);
        MenuItem menuItem = new MenuItem("", text);
        menuItem.setUserData(lastPartOfSpeech);
        menuItem.setOnAction(event -> {
            makePhrase(firstPartOfSpeech, lastPartOfSpeech);
        });
        return menuItem;
    }

    private void makePhrase(final PartOfSpeechNodeAdapter firstPartOfSpeech,
                            final PartOfSpeechNodeAdapter lastPartOfSpeech) {
        boolean add = false;
        List<PartOfSpeechNodeAdapter> fragments = new ArrayList<>();
        ObservableList<GraphNodeAdapter> graphNodes = copyGraphNodes();
        outer:
        for (GraphNodeAdapter graphNode : graphNodes) {
            if (graphNode.getGraphNodeType().equals(TERMINAL)) {
                TerminalNodeAdapter node = (TerminalNodeAdapter) graphNode;
                ObservableList<PartOfSpeechNodeAdapter> partOfSpeeches = copyPartOfSpeechNodes(node);
                for (PartOfSpeechNodeAdapter partOfSpeech : partOfSpeeches) {
                    String id = partOfSpeech.getId();
                    if (id.equals(firstPartOfSpeech.getId())) {
                        add = true;
                    }
                    if (add) {
                        fragments.add(partOfSpeech);
                    }
                    if (id.equals(lastPartOfSpeech.getId())) {
                        break outer;
                    }
                }
            }
        }
        phraseSelectionDialog.reset(canvasUtil.getPhraseText(fragments));
        Optional<PhraseNode> result = phraseSelectionDialog.showAndWait();
        result.ifPresent(phraseNode -> addPhraseNode(phraseNode, fragments));
    }

    private void addPhraseNode(PhraseNode phraseNode, List<PartOfSpeechNodeAdapter> fragments) {
        graphBuilder.set(getDependencyGraph().getGraphMetaInfo().getGraphMetaInfo());
        graphBuilder.buildPhraseNode(phraseNode, fragments);
        PhraseNodeAdapter phraseNodeAdapter = new PhraseNodeAdapter();
        phraseNodeAdapter.setSrc(phraseNode);
        getDependencyGraph().getDependencyGraph().getNodes().add(phraseNode);
        getDependencyGraph().getGraphNodes().add(phraseNodeAdapter);
        DependencyGraphAdapter dependencyGraph = getDependencyGraph();
        setDependencyGraph(null);
        setDependencyGraph(dependencyGraph);
    }

    private void addRelationMenuItems(LinkSupportAdapter currentNode, Menu menu) {
        ObservableList<GraphNodeAdapter> target = copyGraphNodes();
        target.forEach(graphNode -> {
            GraphNodeType nodeType = graphNode.getGraphNodeType();
            switch (nodeType) {
                case TERMINAL:
                case IMPLIED:
                case REFERENCE:
                case HIDDEN:
                    TerminalNodeAdapter tn = (TerminalNodeAdapter) graphNode;
                    ObservableList<PartOfSpeechNodeAdapter> partOfSpeeches = copyPartOfSpeechNodes(tn);
                    for (PartOfSpeechNodeAdapter partOfSpeech : partOfSpeeches) {
                        boolean excluded = PART_OF_SPEECH_EXCLUDE_LIST.contains(
                                partOfSpeech.getSrc().getLocation().getPartOfSpeech());
                        boolean self = currentNode.getId().equals(partOfSpeech.getId());
                        if (excluded || self) {
                            continue;
                        }
                        menu.getItems().add(createRelationshipMenuItem(partOfSpeech, currentNode));
                    }
                    break;
                case PHRASE:
                    PhraseNodeAdapter pn = (PhraseNodeAdapter) graphNode;
                    menu.getItems().add(createRelationshipMenuItem(pn, currentNode));
                    break;
            }
        });
    }

    private String getRelationshipMenuItemText(final LinkSupportAdapter linkSupportAdapter) {
        String text = null;
        if (isGivenType(PartOfSpeechNodeAdapter.class, linkSupportAdapter)) {
            PartOfSpeechNodeAdapter partOfSpeechNodeAdapter = (PartOfSpeechNodeAdapter) linkSupportAdapter;
            TerminalNodeAdapter terminalNode = (TerminalNodeAdapter) partOfSpeechNodeAdapter.getParent();
            Location location = partOfSpeechNodeAdapter.getSrc().getLocation();
            Token token = terminalNode.getSrc().getToken();
            ArabicWord locationWord = getLocationWord(token, location);
            text = format("%s (%s)", locationWord.toUnicode(), location.getPartOfSpeech().getLabel().toUnicode());
        } else if (isGivenType(PhraseNodeAdapter.class, linkSupportAdapter)) {
            PhraseNodeAdapter phraseNodeAdapter = (PhraseNodeAdapter) linkSupportAdapter;
            text = canvasUtil.getPhraseMenuItemText(phraseNodeAdapter);
        }
        return text;
    }

    private MenuItem createRelationshipMenuItem(final LinkSupportAdapter dependentNode,
                                                final LinkSupportAdapter currentNode) {
        Text text = new Text(getRelationshipMenuItemText(dependentNode));
        text.setNodeOrientation(RIGHT_TO_LEFT);
        text.setFont(ARABIC_FONT_SMALL);
        MenuItem menuItem = new MenuItem("", text);
        menuItem.setUserData(dependentNode);
        menuItem.setOnAction(event -> {
            String dependentNodeText = getRelationshipMenuItemText(currentNode);
            MenuItem source = (MenuItem) event.getSource();
            PartOfSpeechNodeAdapter userData = (PartOfSpeechNodeAdapter) source.getUserData();
            makeRelationship(currentNode, userData, dependentNodeText, ((Text) source.getGraphic()).getText());
        });
        return menuItem;
    }

    /*private MenuItem createRelationshipMenuItem(PhraseNodeAdapter pn, LinkSupportAdapter currentNode) {
        Text text = new Text(canvasUtil.getPhraseMenuItemText(pn));
        text.setNodeOrientation(RIGHT_TO_LEFT);
        text.setFont(ARABIC_FONT_SMALL);
        MenuItem menuItem = new MenuItem("", text);
        menuItem.setUserData(pn);
        menuItem.setOnAction(event -> {
            System.out.println("HERE");
        });
        return menuItem;
    }*/

    private void makeReference(int index) {
        DependencyGraph dependencyGraph = getDependencyGraph().getDependencyGraph();
        referenceSelectionDialog.reset(dependencyGraph.getChapterNumber(), dependencyGraph.getVerseNumber());
        Optional<Token> result = referenceSelectionDialog.showAndWait();
        result.ifPresent(token -> addReference(token, index));
    }

    private void addReference(Token token, int index) {
        graphBuilder.set(getDependencyGraph().getGraphMetaInfo().getGraphMetaInfo());
        Group group = (Group) canvasPane.getChildren().get(index);
        Line referenceLine = getReferenceLine(group);
        ReferenceNode referenceNode = graphBuilder.buildReferenceNode(token, referenceLine);
        ReferenceNodeAdapter referenceNodeAdapter = new ReferenceNodeAdapter();
        referenceNodeAdapter.setSrc(referenceNode);
        getDependencyGraph().getDependencyGraph().getNodes().add(referenceNode);
        getDependencyGraph().getGraphNodes().add(referenceNodeAdapter);
        DependencyGraphAdapter dependencyGraph = getDependencyGraph();
        setDependencyGraph(null);
        setDependencyGraph(dependencyGraph);
    }

    private void makeRelationship(final LinkSupportAdapter dependentNode, final LinkSupportAdapter ownerNode,
                                  String dependentNodeText, String ownerNodeText) {
        relationshipSelectionDialog.reset(dependentNodeText, ownerNodeText);
        Optional<RelationshipNode> result = relationshipSelectionDialog.showAndWait();
        result.ifPresent(relationshipNode -> addRelationship(relationshipNode, dependentNode, ownerNode));
    }

    private void addRelationship(RelationshipNode relationshipNode,
                                 LinkSupportAdapter dependentNode,
                                 LinkSupportAdapter ownerNode) {
        graphBuilder.set(getDependencyGraph().getGraphMetaInfo().getGraphMetaInfo());
        graphBuilder.buildRelationshipNode(relationshipNode, dependentNode, ownerNode);
        RelationshipNodeAdapter relationshipNodeAdapter = new RelationshipNodeAdapter();
        relationshipNodeAdapter.setSrc(relationshipNode);
        getDependencyGraph().getDependencyGraph().getNodes().add(relationshipNode);
        getDependencyGraph().getGraphNodes().add(relationshipNodeAdapter);
        DependencyGraphAdapter dependencyGraph = getDependencyGraph();
        setDependencyGraph(null);
        setDependencyGraph(dependencyGraph);
    }

    private MenuItem createAddEmptyNodeMenuItem(int index, PartOfSpeech partOfSpeech) {
        Text text = new Text(partOfSpeech.getLabel().toUnicode());
        text.setFont(ARABIC_FONT_SMALL_BOLD);
        MenuItem menuItem = new MenuItem("", text);
        menuItem.setOnAction(event -> addEmptyNode(index, partOfSpeech));
        return menuItem;
    }

    private void addEmptyNode(int index, PartOfSpeech partOfSpeech) {
        GraphMetaInfoAdapter graphMetaInfo = getDependencyGraph().getGraphMetaInfo();
        shiftNodes(index, graphMetaInfo);
        Node node = canvasPane.getChildren().get(index - 1);
        if (!isGivenType(Group.class, node)) {
            // this should not happen, but we still need to handle
            //TODO:
            return;
        }
        Group group = (Group) node;
        Line referenceLine = getReferenceLine(group);
        graphBuilder.set(graphMetaInfo.getGraphMetaInfo());
        ImpliedNodeAdapter impliedNodeAdapter = createEmptyNodeAdapter(partOfSpeech, referenceLine);
        getDependencyGraph().getDependencyGraph().getNodes().add(index, impliedNodeAdapter.getSrc());
        getDependencyGraph().getGraphNodes().add(index, impliedNodeAdapter);
        DependencyGraphAdapter dependencyGraph = getDependencyGraph();
        setDependencyGraph(null);
        setDependencyGraph(dependencyGraph);
    }

    private ImpliedNodeAdapter createEmptyNodeAdapter(PartOfSpeech partOfSpeech, Line referenceLine) {
        ImpliedNodeAdapter impliedNodeAdapter = new ImpliedNodeAdapter();
        ImpliedNode impliedNode = graphBuilder.buildEmptyNode(partOfSpeech, referenceLine);
        impliedNodeAdapter.setSrc(impliedNode);
        return impliedNodeAdapter;
    }

    /**
     * When adding an {@link GraphNodeType#IMPLIED}, {@link GraphNodeType#HIDDEN}, or {@link GraphNodeType#REFERENCE}
     * node, move all nodes by {@link GraphMetaInfo#gapBetweenTokens} plus {@link GraphMetaInfo#tokenWidth} to
     * the right.
     *
     * @param index
     * @param graphMetaInfo
     */
    private void shiftNodes(int index, GraphMetaInfoAdapter graphMetaInfo) {
        ObservableList<GraphNodeAdapter> graphNodes = getDependencyGraph().getGraphNodes();
        for (int i = index; i < graphNodes.size(); i++) {
            GraphNodeAdapter node = graphNodes.get(i);
            if (TERMINALS.contains(node.getGraphNodeType())) {
                double translateX = graphMetaInfo.getGapBetweenTokens() + graphMetaInfo.getTokenWidth()
                        + node.getTranslateX();
                node.setTranslateX(translateX);
            }
        }
    }

    private ObservableList<GraphNodeAdapter> copyGraphNodes() {
        ObservableList<GraphNodeAdapter> nodes = getDependencyGraph().getGraphNodes();
        ObservableList<GraphNodeAdapter> target = observableArrayList();
        nodes.stream().filter(node -> TERMINALS.contains(node.getGraphNodeType())).forEach(node -> target.add(node));
        reverse(target);
        nodes.stream().filter(node -> node.getGraphNodeType().equals(PHRASE)).forEach(node -> target.add(node));
        return target;
    }

    private ObservableList<PartOfSpeechNodeAdapter> copyPartOfSpeechNodes(TerminalNodeAdapter tn) {
        ObservableList<PartOfSpeechNodeAdapter> partOfSpeeches = tn.getPartOfSpeeches();
        ObservableList<PartOfSpeechNodeAdapter> target = observableArrayList();
        target.setAll(partOfSpeeches);
        reverse(target);
        return target;
    }

    private Line getReferenceLine(Group parent) {
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
            return null;
        }
        return line;
    }

    private Optional<ButtonType> showAlert(Alert.AlertType type, String contentText, String headerText) {
        Alert alert = new Alert(type);
        if (headerText != null) {
            alert.setHeaderText(headerText);
        }
        alert.setContentText(contentText);
        return alert.showAndWait();
    }

    private int getIndex(TerminalNodeAdapter src) {
        int index = -1;
        ObservableList<GraphNodeAdapter> graphNodes = getDependencyGraph().getGraphNodes();
        for (int i = 0; i < graphNodes.size(); i++) {
            GraphNodeAdapter node = graphNodes.get(i);
            if (node.getId().equals(src.getId())) {
                index = i;
                break;
            }
        }
        return index;
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

    public Pane getCanvasPane() {
        return canvasPane;
    }
}
