package com.alphasystem.morphologicalanalysis.ui.dependencygraph.components;

import com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.*;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.DecimalFormatStringConverter;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.SpinnerValueFactory.DoubleSpinnerValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import static com.alphasystem.morphologicalanalysis.ui.common.Global.ARABIC_FONT_NAME;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.RESOURCE_BUNDLE;
import static java.lang.String.format;
import static javafx.geometry.Pos.CENTER;
import static javafx.scene.control.ScrollPane.ScrollBarPolicy.ALWAYS;
import static javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED;
import static javafx.scene.paint.Color.BLUE;
import static javafx.scene.text.Font.font;
import static javafx.scene.text.FontPosture.REGULAR;
import static javafx.scene.text.FontWeight.BOLD;
import static javafx.scene.text.FontWeight.NORMAL;

/**
 * @author sali
 */
public class DependencyGraphBuilderEditorPane extends BorderPane {


    private final DoubleProperty canvasWidth;
    private final DoubleProperty canvasHeight;
    private GraphNode graphNode;
    private GridPane gridPane;
    private int row = 0;

    public DependencyGraphBuilderEditorPane(GraphNode graphNode) {
        this.graphNode = graphNode;
        canvasWidth = new SimpleDoubleProperty();
        canvasHeight = new SimpleDoubleProperty();
        canvasWidthProperty().addListener((observable, oldValue, newValue) -> {
            initPane(this.graphNode);
        });
        canvasHeightProperty().addListener((observable, oldValue, newValue) -> {
            initPane(this.graphNode);
        });

        gridPane = new GridPane();
        gridPane.setAlignment(CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(5, 5, 5, 5));

        setCanvasWidth(800);
        setCanvasHeight(400);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ALWAYS);
        scrollPane.setHbarPolicy(AS_NEEDED);
        scrollPane.setContent(gridPane);
        setCenter(scrollPane);

        setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        setPrefHeight(650);
    }

    private static String getPaneTitle(GraphNode node) {
        return RESOURCE_BUNDLE.getString(format("%s_node.label", node.getNodeType().name()));
    }

    public final double getCanvasWidth() {
        return canvasWidth.get();
    }

    public final void setCanvasWidth(double canvasWidth) {
        this.canvasWidth.set(canvasWidth);
    }

    public final DoubleProperty canvasWidthProperty() {
        return canvasWidth;
    }

    public final double getCanvasHeight() {
        return canvasHeight.get();
    }

    public final void setCanvasHeight(double canvasHeight) {
        this.canvasHeight.set(canvasHeight);
    }

    public final DoubleProperty canvasHeightProperty() {
        return canvasHeight;
    }

    void initPane(GraphNode graphNode) {
        row = 0;
        ObservableList<Node> children = gridPane.getChildren();
        children.remove(0, children.size());

        addTextControls(graphNode);
        GraphNodeType nodeType = graphNode.getNodeType();
        switch (nodeType) {
            case TERMINAL:
            case EMPTY:
            case REFERENCE:
                addTerminalNodeProperties((TerminalNode) graphNode);
                break;
            case PART_OF_SPEECH:
                addPartOfSpeechProperties((PartOfSpeechNode) graphNode);
                break;
            case RELATIONSHIP:
                addRelationshipNodeProperties((RelationshipNode) graphNode);
                break;
            case PHRASE:
                addPhraseNode((PhraseNode) graphNode);
                break;
            default:
                break;
        }

        gridPane.requestLayout();
    }

    private void addTextControls(GraphNode node) {
        Label label = new Label(getPaneTitle(node));
        label.setFont(font("Georgia", BOLD, REGULAR, 16));
        label.setUnderline(true);
        label.setTextFill(BLUE);
        gridPane.add(label, 0, row, 2, 1);

        row++; // 1
        label = new Label(RESOURCE_BUNDLE.getString("text.label"));
        gridPane.add(label, 0, row);
        String value = node.getText();
        value = value == null ? "" : value;
        TextField textField = new TextField(value);
        textField.setOnAction(event -> {
            TextField source = (TextField) event.getSource();
            node.setText(source.getText());
        });
        textField.setFont(font(ARABIC_FONT_NAME, NORMAL, REGULAR, 20));
        label.setLabelFor(textField);
        gridPane.add(textField, 1, row);

        row++;

        addFields("xIndex.label", GraphNode::getX, GraphNode::setX, node, getCanvasWidth());
        addFields("yIndex.label", GraphNode::getY, GraphNode::setY, node, getCanvasHeight());
        gridPane.add(new Separator(), 0, row, 2, 1);
    }

    private void addTerminalNodeProperties(TerminalNode node) {
        row++;
        addFields("startXIndex.label", TerminalNode::getX1, TerminalNode::setX1, node, getCanvasWidth());
        addFields("startYIndex.label", TerminalNode::getY1, TerminalNode::setY1, node, getCanvasHeight());
        addFields("endXIndex.label", TerminalNode::getX2, TerminalNode::setX2, node, getCanvasWidth());
        addFields("endYIndex.label", TerminalNode::getY2, TerminalNode::setY2, node, getCanvasHeight());
        addFields("tanslationXIndex.label", TerminalNode::getX3, TerminalNode::setX3, node, getCanvasWidth());
        addFields("tanslationYIndex.label", TerminalNode::getY3, TerminalNode::setY3, node, getCanvasHeight());
        gridPane.add(new Separator(), 0, row, 2, 1);

        row++;
        Label label = new Label(RESOURCE_BUNDLE.getString("groupTranslate.label"));
        label.setFont(font("Georgia", BOLD, REGULAR, 12));
        label.setUnderline(true);
        label.setTextFill(BLUE);
        gridPane.add(label, 0, row, 2, 1);

        row++;
        addFields("groupTranslateX.label", TerminalNode::getTranslateX, TerminalNode::setTranslateX,
                node, getCanvasWidth());
        addFields("groupTranslateY.label", TerminalNode::getTranslateY, TerminalNode::setTranslateY,
                node, getCanvasHeight());
    }

    private void addPartOfSpeechProperties(PartOfSpeechNode node) {
        row++;
        addFields("posCx.label", PartOfSpeechNode::getCx, PartOfSpeechNode::setCx, node, getCanvasWidth());
        addFields("posCy.label", PartOfSpeechNode::getCy, PartOfSpeechNode::setCy, node, getCanvasHeight());
    }

    private void addRelationshipNodeProperties(RelationshipNode node) {
        row++;

        // addFields("startX.label", RelationshipNode::getStartX, RelationshipNode::setStartX, node, canvasWidth);
        // addFields("startY.label", RelationshipNode::getStartY, RelationshipNode::setStartY, node, canvasHeight);
        addFields("controlX1.label", RelationshipNode::getControlX1, RelationshipNode::setControlX1,
                node, getCanvasWidth());
        addFields("controlY1.label", RelationshipNode::getControlY1, RelationshipNode::setControlY1,
                node, getCanvasHeight());
        addFields("controlX2.label", RelationshipNode::getControlX2, RelationshipNode::setControlX2,
                node, getCanvasWidth());
        addFields("controlY2.label", RelationshipNode::getControlY2, RelationshipNode::setControlY2,
                node, getCanvasHeight());

        Label label;
        Spinner<Double> spinner;

        label = new Label(RESOURCE_BUNDLE.getString("t1.label"));
        gridPane.add(label, 0, row);
        spinner = getSpinnerThreeDecimalPlace(0, 1.000, node.getT1(), 0.005);
        label.setLabelFor(spinner);
        spinner.setOnMouseClicked(event -> {
            Spinner source = (Spinner) event.getSource();
            node.setT1((Double) source.getValue());
        });
        gridPane.add(spinner, 1, row);

        row++;
        label = new Label(RESOURCE_BUNDLE.getString("t2.label"));
        gridPane.add(label, 0, row);
        spinner = getSpinnerThreeDecimalPlace(0, 1.000, node.getT2(), 0.005);
        label.setLabelFor(spinner);
        spinner.setOnMouseClicked(event -> {
            Spinner source = (Spinner) event.getSource();
            node.setT2((Double) source.getValue());
        });
        gridPane.add(spinner, 1, row);
    }

    private void addPhraseNode(PhraseNode node) {
        row++;
        addFields("startXIndex.label", PhraseNode::getX1, PhraseNode::setX1, node, getCanvasWidth());
        addFields("startYIndex.label", PhraseNode::getY1, PhraseNode::setY1, node, getCanvasHeight());
        addFields("endXIndex.label", PhraseNode::getX2, PhraseNode::setX2, node, getCanvasWidth());
        addFields("endYIndex.label", PhraseNode::getY2, PhraseNode::setY2, node, getCanvasHeight());
        addFields("posCx.label", PhraseNode::getCx, PhraseNode::setCx, node, getCanvasWidth());
        addFields("posCy.label", PhraseNode::getCy, PhraseNode::setCy, node, getCanvasHeight());
    }

    private Spinner<Double> getSpinner(double min, double max, double initialValue) {
        Spinner<Double> spinner = new Spinner<>();
        spinner.setValueFactory(new DoubleSpinnerValueFactory(min, max, initialValue, 0.5));
        return spinner;
    }

    private Spinner<Double> getSpinnerThreeDecimalPlace(double min, double max,
                                                        double initialValue, double amountToStepBy) {
        Spinner<Double> spinner = new Spinner<>();
        DoubleSpinnerValueFactory valueFactory = new DoubleSpinnerValueFactory(min, max,
                initialValue, amountToStepBy);
        valueFactory.setConverter(DecimalFormatStringConverter.THREE_DECIMAL_PLACE_CONVERTER);
        spinner.setValueFactory(valueFactory);
        return spinner;
    }

    private Slider createSlider(double min, double max, double value) {
        Slider slider = new Slider();
        slider.setMin(min);
        slider.setMax(max);
        slider.setValue(value);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(100);
        slider.setMinorTickCount(20);
        slider.setBlockIncrement(20);
        return slider;
    }

    private <T extends GraphNode, G extends GetterAdapter<T>, S extends SetterAdapter<T>> void addFields(
            String key, G g, S s, T node, double max) {
        Label label = new Label(RESOURCE_BUNDLE.getString(key));
        gridPane.add(label, 0, row);

        Double initialValue = g.get(node);
        Spinner<Double> spinner = getSpinner(0, max, initialValue);
        spinner.setOnMouseClicked(event -> s.set(node, spinner.getValue()));
        gridPane.add(spinner, 1, row);

        row++;
        Slider slider = createSlider(0, max, initialValue);
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            Double d = (Double) newValue;
            if (d % 1 != 0.0 && d % 0.5 != 0.0) {
                d = StrictMath.ceil(d);
            }
            slider.setValue(d);
            spinner.getValueFactory().setValue(d);
            s.set(node, d);
        });
        spinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            slider.setValue(newValue);
        });
        gridPane.add(slider, 1, row);

        row++;
    }

    private interface GetterAdapter<T extends GraphNode> {
        Double get(T node);
    }

    private interface SetterAdapter<T extends GraphNode> {
        void set(T node, Double value);
    }
}
