package com.alphasystem.morphologicalanalysis.treebank.jfx.ui.components;

import com.alphasystem.morphologicalanalysis.model.Location;
import com.alphasystem.morphologicalanalysis.model.Token;
import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.*;
import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.util.DependencyGraphGraphicTool;
import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.util.GraphBuilder;
import com.alphasystem.svg.jfx.SVGGraphicsContext;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.List;
import java.util.Optional;

import static com.alphasystem.morphologicalanalysis.treebank.jfx.ui.Global.ARABIC_FONT_NAME;
import static com.alphasystem.morphologicalanalysis.treebank.jfx.ui.util.DependencyGraphGraphicTool.DARK_GRAY_CLOUD;
import static java.lang.String.format;
import static javafx.scene.control.Alert.AlertType.INFORMATION;
import static javafx.scene.control.Alert.AlertType.WARNING;
import static javafx.scene.control.ButtonBar.ButtonData.OK_DONE;
import static javafx.scene.input.MouseEvent.MOUSE_CLICKED;
import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.web;
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
    private static final Font ARABIC_FONT_BIG = font(ARABIC_FONT_NAME, REGULAR, 48);
    private static final Font ARABIC_FONT_SMALL = font(ARABIC_FONT_NAME, REGULAR, 24);
    private final ObjectProperty<CanvasData> canvasDataObject;
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

        initListeners();

        canvasPane = new Pane();
        svgGraphicsContext = new SVGGraphicsContext(canvasPane, width, height);
        initCanvas();

        canvasPane.setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        canvasPane.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        canvasPane.setPrefSize(width, height);
        canvasPane.setBackground(new Background(new BackgroundFill(Color.BEIGE, null, null)));

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

    public CanvasData getCanvasDataObject() {
        return canvasDataObject.get();
    }

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
            svgGraphicsContext.draw(tool.drawGridLines(showGridLines, width, height));
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
                default:
                    break;
            }
        }
    }

    private void buildPartOfSpeechNode(PartOfSpeechNode node) {
        Color color = web(node.getPartOfSpeech().getColorCode());
        Text arabicText = tool.drawText(node.getId(), node.getText(), RIGHT,
                color, node.getX(), node.getY(), ARABIC_FONT_SMALL);
        // bind coordinates
        arabicText.textProperty().bind(node.textProperty());
        arabicText.xProperty().bind(node.xProperty());
        arabicText.yProperty().bind(node.yProperty());
        arabicText.addEventFilter(MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (startPoint == null) {
                    startPoint = new Point2D(node.getCx(), node.getCy());
                    bound = tool.drawBounds(arabicText);
                    canvasPane.getChildren().add(bound);
                    showAlert(INFORMATION,
                            format("Part of Speech (%s) selected.\nChoose other part of speech to connect.",
                                    node.getText()), null);
                } else {
                    if (endPoint == null) {
                        endPoint = new Point2D(node.getCx(), node.getCy());
                        if (endPoint.equals(startPoint)) {
                            showAlert(WARNING, "You must select different part of speech.", null);
                            endPoint = null;
                        } else {
                            Optional<ButtonType> result = showAlert(Alert.AlertType.CONFIRMATION,
                                    format("Part of Speech (%s) selected." +
                                                    "\nWould you like to connect these part of speeches",
                                            node.getText()), null);
                            result.ifPresent(buttonType -> {
                                ButtonBar.ButtonData buttonData = buttonType.getButtonData();
                                if (buttonData.equals(OK_DONE)) {
                                    canvasPane.getChildren().remove(bound);
                                    addRelationship();
                                }
                                startPoint = null;
                                endPoint = null;
                            });
                        }
                    } else {
                        startPoint = null;
                        endPoint = null;
                    }
                }
            }
        });

        String id = format("circle_%s", node.getId());
        Circle circle = tool.drawCircle(id, color, node.getCx(), node.getCy(), 2.0);
        // bind coordinates
        circle.centerXProperty().bind(node.cxProperty());
        circle.centerYProperty().bind(node.cyProperty());

        Group group = new Group();

        group.setId(PART_OF_SPEECH_GROUP_ID);
        group.getChildren().addAll(arabicText, circle);
        canvasPane.getChildren().add(group);
    }

    private void addRelationship() {
        // TODO: make a connection, update tree
        // Step 1: call GraphBuilder to create a relationship
        RelationshipNode relationshipNode = graphBuilder.buildRelationshipNode(null, startPoint, endPoint);

        // add this node into existing list of nodes and
        // update canvasDataObject for changes to take effect
        CanvasData canvasData = canvasDataObject.get();
        canvasData.getNodes().add(relationshipNode);
        canvasDataObject.setValue(null);
        canvasDataObject.setValue(canvasData);
    }

    private void buildRelationshipNode(RelationshipNode rn) {
        Color color = web(rn.getGrammaticalRelationship().getColorCode());
        CubicCurve cubicCurve = tool.drawCubicCurve(rn.getId(), rn.getStartX(), rn.getStartY(), rn.getControlX1(),
                rn.getControlY1(), rn.getControlX2(), rn.getControlY2(), rn.getEndX(), rn.getEndY(),
                color);

        // bind line co-ordinates
        cubicCurve.startYProperty().bind(rn.startXProperty());
        cubicCurve.startYProperty().bind(rn.startYProperty());
        cubicCurve.controlX1Property().bind(rn.controlX1Property());
        cubicCurve.controlY1Property().bind(rn.controlY1Property());
        cubicCurve.controlX2Property().bind(rn.controlX2Property());
        cubicCurve.controlY2Property().bind(rn.controlY2Property());
        cubicCurve.endYProperty().bind(rn.endXProperty());
        cubicCurve.endYProperty().bind(rn.endYProperty());
        cubicCurve.strokeProperty().bind(rn.strokeProperty());

        Text arabicText = tool.drawText(null, rn.getText(), RIGHT, color, rn.getX(), rn.getY(), ARABIC_FONT_SMALL);

        // bind line co-ordinates
        arabicText.textProperty().bind(rn.textProperty());
        arabicText.fillProperty().bind(rn.strokeProperty());
        arabicText.xProperty().bind(rn.xProperty());
        arabicText.yProperty().bind(rn.yProperty());

        // small arrow poiting towards the relationship direction
        Polyline triangle = tool.drawTriangleOnCubicCurve(rn.getT1(), rn.getT2(), rn.getStartX(), rn.getStartY(),
                rn.getControlX1(), rn.getControlY1(), rn.getControlX2(), rn.getControlY2(), rn.getEndX(),
                rn.getEndY(), color);
        // bind line co-ordinates
        //TODO:

        Group group = new Group();

        group.getChildren().addAll(cubicCurve, arabicText, triangle);

        if (canvasDataObject.get().getCanvasMetaData().isDebugMode()) {
            group.getChildren().add(tool.drawCubicCurveBounds(rn.getStartX(), rn.getStartY(), rn.getControlX1(),
                    rn.getControlY1(), rn.getControlX2(), rn.getControlY2(), rn.getEndX(), rn.getEndY()));
        }
        canvasPane.getChildren().add(group);
    }

    private void buildTerminalNode(TerminalNode tn) {
        Line line = tool.drawLine(tn.getId(), tn.getX1(), tn.getY1(), tn.getX2(), tn.getY2(),
                DARK_GRAY_CLOUD, 0.5);

        // bind line co-ordinates
        line.startXProperty().bind(tn.x1Property());
        line.startYProperty().bind(tn.y1Property());
        line.endXProperty().bind(tn.x2Property());
        line.endYProperty().bind(tn.y2Property());

        //svgGraphicsContext.draw(line);
        Token token = tn.getToken();
        Color color = BLACK;
        List<Location> locations = token.getLocations();
        if (token.getLocationCount() == 1) {
            Location location = locations.get(0);
            color = web(location.getPartOfSpeech().getColorCode());
        }

        Text arabicText = tool.drawText(tn.getId(), tn.getText(), RIGHT,
                color, tn.getX(), tn.getY(), ARABIC_FONT_BIG);

        // bind text x and y locations
        arabicText.textProperty().bind(tn.textProperty());
        arabicText.xProperty().bind(tn.xProperty());
        arabicText.yProperty().bind(tn.yProperty());

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
