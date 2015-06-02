package com.alphasystem.morphologicalanalysis.treebank.jfx.ui.components;

import com.alphasystem.morphologicalanalysis.model.Location;
import com.alphasystem.morphologicalanalysis.model.Token;
import com.alphasystem.morphologicalanalysis.model.support.GrammaticalRelationship;
import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.*;
import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.util.DependencyGraphGraphicTool;
import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.util.GraphBuilder;
import com.alphasystem.svg.jfx.SVGGraphicsContext;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.List;
import java.util.Optional;

import static com.alphasystem.morphologicalanalysis.treebank.jfx.ui.Global.ARABIC_FONT_BIG;
import static com.alphasystem.morphologicalanalysis.treebank.jfx.ui.Global.ARABIC_FONT_SMALL;
import static com.alphasystem.morphologicalanalysis.treebank.jfx.ui.util.DependencyGraphGraphicTool.DARK_GRAY_CLOUD;
import static java.lang.String.format;
import static javafx.application.Platform.runLater;
import static javafx.scene.control.Alert.AlertType.WARNING;
import static javafx.scene.paint.Color.*;
import static javafx.scene.text.Font.font;
import static javafx.scene.text.FontPosture.REGULAR;
import static javafx.scene.text.TextAlignment.CENTER;
import static javafx.scene.text.TextAlignment.RIGHT;

/**
 * @author sali
 */
public class CanvasPane extends Pane {

    public static final String TERMINAL_GROUP_ID = "terminalGroup";
    public static final String PART_OF_SPEECH_GROUP_ID = "partOfSpeechGroup";
    private final ObjectProperty<CanvasData> canvasDataObject;
    private RelationshipSelectionDialog relationshipSelectionDialog;
    private PhraseSelectionDialog phraseSelectionDialog;
    private SVGGraphicsContext svgGraphicsContext;
    private DependencyGraphGraphicTool tool = DependencyGraphGraphicTool.getInstance();
    private GraphBuilder graphBuilder = GraphBuilder.getInstance();
    private Pane canvasPane;
    private Point2D startPoint;
    private Point2D endPoint;
    private Rectangle bound;


    public CanvasPane(CanvasData data) {
        this.canvasDataObject = new SimpleObjectProperty<>(data);

        CanvasMetaData metaData = data.getCanvasMetaData();
        int width = metaData.getWidth();
        int height = metaData.getHeight();

        relationshipSelectionDialog = new RelationshipSelectionDialog();
        phraseSelectionDialog = new PhraseSelectionDialog();
        initListeners();

        canvasPane = new Pane();
        svgGraphicsContext = new SVGGraphicsContext(canvasPane, width, height);
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

    private void initListeners() {
        CanvasMetaData metaData = canvasDataObject.get().getCanvasMetaData();
        metaData.widthProperty().addListener((observable, oldValue, newValue) -> {
            initCanvas();
        });
        metaData.heightProperty().addListener((observable, oldValue, newValue) -> {
            initCanvas();
        });
        metaData.showGridLinesProperty().addListener((observable, oldValue, newValue) -> {
            initCanvas();
        });
        metaData.showOutLinesProperty().addListener((observable, oldValue, newValue) -> {
            initCanvas();
        });
        metaData.debugModeProperty().addListener((observable, oldValue, newValue) -> {
            initCanvas();
        });
        canvasDataObject.addListener((observable, oldValue, newData) -> {
            if (newData != null) {
                drawNodes(newData.getNodes(), false);
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
        if (nodes != null && !nodes.isEmpty()) {
            drawNodes(nodes, true);
        }

        if (showOutline || showGridLines) {
            canvasPane.getChildren().add(tool.drawGridLines(showGridLines, width, height));
        }

        setPrefSize(width + 200, height + 200);
        requestLayout();
    }

    private void drawNodes(ObservableList<GraphNode> nodes, boolean removeGridLines) {
        svgGraphicsContext.removeAll(removeGridLines);
        for (GraphNode node : nodes) {
            NodeType nodeType = node.getNodeType();
            switch (nodeType) {
                case TERMINAL:
                    buildTerminalNode((TerminalNode) node);
                    break;
                case PART_OF_SPEECH:
                    buildPartOfSpeechNode((PartOfSpeechNode) node);
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

    private void addRelationship(GrammaticalRelationship grammaticalRelationship) {
        // Step 1: call GraphBuilder to create a relationship
        RelationshipNode relationshipNode = graphBuilder.buildRelationshipNode(null, grammaticalRelationship,
                startPoint, endPoint);

        // add this node into existing list of nodes and
        // update canvasDataObject for changes to take effect
        CanvasData canvasData = canvasDataObject.get();
        canvasData.getNodes().add(relationshipNode);
        canvasDataObject.setValue(null);
        canvasDataObject.setValue(canvasData);
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

        Token token = tn.getToken();
        Color color = BLACK;
        List<Location> locations = token.getLocations();
        if (token.getLocationCount() == 1) {
            Location location = locations.get(0);
            color = web(location.getPartOfSpeech().getColorCode());
        }

        Text arabicText = drawText(tn, color, ARABIC_FONT_BIG);

        arabicText.setOnMouseClicked(event -> {
            Text text = (Text) event.getSource();
            TerminalNode userData = (TerminalNode) text.getUserData();
            runLater(() -> updateSelectedShape(text));

            phraseSelectionDialog.getPhraseSelectionModel().setFirstNode(userData);
            Optional<PhraseSelectionModel> result = phraseSelectionDialog.showAndWait();
            result.ifPresent(psm -> {
                addPhrase(psm);
                phraseSelectionDialog.reset();
                updateSelectedShape(null);
            });
        });

        String id = format("trans_%s", tn.getId());
        Text englishText = tool.drawText(id, token.getTranslation(), CENTER, BLACK,
                tn.getX3(), tn.getY3(), font("Candara", REGULAR, 16));
        // bind text x and y locations
        englishText.xProperty().bind(tn.x3Property());
        englishText.yProperty().bind(tn.y3Property());

        Group group = new Group();
        group.setId(TERMINAL_GROUP_ID);
        group.getChildren().addAll(englishText, arabicText, line);
        canvasPane.getChildren().add(group);
    }

    private void buildPartOfSpeechNode(PartOfSpeechNode pn) {
        Color color = web(pn.getPartOfSpeech().getColorCode());
        Text arabicText = drawText(pn, color, ARABIC_FONT_SMALL);
        arabicText.setOnMouseClicked(event -> selectRelationship(pn.getText(),
                pn.getCx(), pn.getCy(), (Text) event.getSource()));

        String id = format("circle_%s", pn.getId());
        Circle circle = tool.drawCircle(id, color, pn.getCx(), pn.getCy(), 2.0);
        // bind coordinates
        circle.centerXProperty().bind(pn.cxProperty());
        circle.centerYProperty().bind(pn.cyProperty());

        Group group = new Group();
        group.setId(PART_OF_SPEECH_GROUP_ID);
        group.getChildren().addAll(arabicText, circle);
        canvasPane.getChildren().add(group);
    }

    private void selectRelationship(String text, double cx, double cy, Text arabicText) {
        if (startPoint == null) {
            startPoint = new Point2D(cx, cy);
            updateSelectedShape(arabicText);
            relationshipSelectionDialog.setFirstPartOfSpeech(text);
            relationshipSelectionDialog.showAndWait();
        } else {
            if (endPoint == null) {
                endPoint = new Point2D(cx, cy);
                if (endPoint.equals(startPoint)) {
                    showAlert(WARNING, "You must select different part of speech.", null);
                    endPoint = null;
                } else {
                    relationshipSelectionDialog.setSecondPartOfSpeech(text);
                    Optional<GrammaticalRelationship> result = relationshipSelectionDialog.showAndWait();
                    result.ifPresent(gr -> {
                        if (result.isPresent() && !gr.equals(GrammaticalRelationship.NONE)) {
                            addRelationship(gr);
                        }
                        startPoint = null;
                        endPoint = null;
                        updateSelectedShape(null);
                        relationshipSelectionDialog.reset();
                    });
                }
            } else {
                relationshipSelectionDialog.reset();
                updateSelectedShape(null);
                startPoint = null;
                endPoint = null;
            }
        } // end of else
    }

    private void buildRelationshipNode(RelationshipNode rn) {
        Color color = web(rn.getGrammaticalRelationship().getColorCode());
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
        rn.t1Property().addListener((observable, oldValue, newValue) -> {
            runLater(this::initCanvas);
        });
        rn.t2Property().addListener((observable, oldValue, newValue) -> {
            runLater(this::initCanvas);
        });

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
                pn.getCx(), pn.getCy(), (Text) event.getSource()));
        Circle circle = tool.drawCircle(null, color, pn.getCx(), pn.getCy(), 2.0);
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

    private void updateSelectedShape(Shape shape) {
        if (shape == null) {
            canvasPane.getChildren().remove(bound);
        } else {
            if (bound == null) {
                bound = tool.drawBounds(shape);
                canvasPane.getChildren().add(bound);
            } else {
                Bounds boundsInLocal = shape.getBoundsInLocal();
                bound.setX(boundsInLocal.getMinX());
                bound.setY(boundsInLocal.getMinY());
                bound.setWidth(boundsInLocal.getWidth());
                bound.setHeight(boundsInLocal.getHeight());
            }
        }
    }

    public Pane getCanvasPane() {
        return canvasPane;
    }
}
