package com.alphasystem.morphologicalanalysis.treebank.jfx.ui.components;

import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.*;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.DoubleSpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import static com.alphasystem.morphologicalanalysis.treebank.jfx.ui.Global.ARABIC_FONT_NAME;
import static com.alphasystem.morphologicalanalysis.treebank.jfx.ui.Global.RESOURCE_BUNDLE;
import static com.alphasystem.morphologicalanalysis.treebank.jfx.ui.util.DecimalFormatStringConverter.THREE_DECIMAL_PLACE_CONVERTER;
import static java.lang.Double.parseDouble;
import static java.lang.String.format;
import static javafx.geometry.Pos.CENTER;
import static javafx.scene.paint.Color.BLUE;
import static javafx.scene.text.Font.font;
import static javafx.scene.text.FontPosture.REGULAR;
import static javafx.scene.text.FontWeight.BOLD;
import static javafx.scene.text.FontWeight.NORMAL;

/**
 * @author sali
 */
public class DependencyGraphBuilderEditorPane extends GridPane {

    private static final double DEFAULT_AMOUNT_TO_STEP_BY = 0.5;
    private double canvasWidth;
    private double canvasHeight;
    private GraphNode graphNode;

    public DependencyGraphBuilderEditorPane(GraphNode graphNode) {
        setAlignment(CENTER);
        setHgap(10);
        setVgap(10);
        setPadding(new Insets(25, 25, 25, 25));

        this.graphNode = graphNode;
        canvasWidth = 800;
        canvasHeight = 400;

        initPane(this.graphNode);
    }

    private static String getPaneTitle(GraphNode node) {
        return RESOURCE_BUNDLE.getString(format("%s_node.label", node.getNodeType().name()));
    }

    private static String getString(GraphNode node, String key) {
        return RESOURCE_BUNDLE.getString(format("%s.%s.%s.label", DependencyGraphBuilderEditorPane.class.getSimpleName(),
                node.getClass().getSimpleName(), key));
    }

    void updateWidth(double width) {
        this.canvasWidth = width;
        initPane(graphNode);
    }

    void updateHeight(double height) {
        this.canvasHeight = height;
        initPane(graphNode);
    }

    void initPane(GraphNode graphNode) {
        ObservableList<Node> children = getChildren();
        children.remove(0, children.size());

        addTextControls(graphNode);
        NodeType nodeType = graphNode.getNodeType();
        switch (nodeType) {
            case TERMINAL:
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

        requestLayout();
    }

    private void addTextControls(GraphNode node) {
        int row = 0;

        Label label = new Label(getPaneTitle(node));
        label.setFont(font("Georgia", BOLD, REGULAR, 16));
        label.setUnderline(true);
        label.setTextFill(BLUE);
        add(label, 0, row, 2, 1);

        row++; // 1
        label = new Label(RESOURCE_BUNDLE.getString("text.label"));
        add(label, 0, row);
        String value = node.getText();
        value = value == null ? "" : value;
        TextField textField = new TextField(value);
        textField.setOnAction(event -> {
            TextField source = (TextField) event.getSource();
            node.setText(source.getText());
        });
        textField.setFont(font(ARABIC_FONT_NAME, NORMAL, REGULAR, 20));
        label.setLabelFor(textField);
        add(textField, 1, row);

        row++; //2
        label = new Label(RESOURCE_BUNDLE.getString("xIndex.label"));
        add(label, 0, row);
        Spinner<Double> spinner = getSpinner(0, canvasWidth, node.getX(), 0.5);
        spinner.setOnMouseClicked(event -> {
            Spinner source = (Spinner) event.getSource();
            node.setX((Double) source.getValue());
        });
        add(spinner, 1, row);

        row++; //3
        label = new Label(RESOURCE_BUNDLE.getString("yIndex.label"));
        add(label, 0, row);
        spinner = getSpinner(0, canvasHeight, node.getY(), DEFAULT_AMOUNT_TO_STEP_BY);
        spinner.setOnMouseClicked(event -> {
            Spinner source = (Spinner) event.getSource();
            node.setY((Double) source.getValue());
        });
        add(spinner, 1, row);

        row++;
        add(new Separator(), 0, row, 2, 1);
    }

    private void addTerminalNodeProperties(TerminalNode node) {
        int row = 5;

        Label label;
        Spinner<Double> spinner;
        TextField textField;

        label = new Label(RESOURCE_BUNDLE.getString("startXIndex.label"));
        add(label, 0, row);
        spinner = getSpinner(0, canvasWidth, node.getX1(), DEFAULT_AMOUNT_TO_STEP_BY);
        spinner.setOnMouseClicked(event -> {
            Spinner source = (Spinner) event.getSource();
            node.setX1((Double) source.getValue());
        });
        add(spinner, 1, row);

        row++; //6
        label = new Label(RESOURCE_BUNDLE.getString("startYIndex.label"));
        add(label, 0, row);
        spinner = getSpinner(0, canvasHeight, node.getY1(), DEFAULT_AMOUNT_TO_STEP_BY);
        spinner.setOnMouseClicked(event -> {
            Spinner source = (Spinner) event.getSource();
            node.setY1((Double) source.getValue());
        });
        add(spinner, 1, row);

        row++; //7
        label = new Label(RESOURCE_BUNDLE.getString("endXIndex.label"));
        add(label, 0, row);
        spinner = getSpinner(0, canvasWidth, node.getX2(), DEFAULT_AMOUNT_TO_STEP_BY);
        spinner.setOnMouseClicked(event -> {
            Spinner source = (Spinner) event.getSource();
            node.setX2((Double) source.getValue());
        });
        add(spinner, 1, row);

        row++; //8
        label = new Label(RESOURCE_BUNDLE.getString("endYIndex.label"));
        add(label, 0, row);
        spinner = getSpinner(0, canvasHeight, node.getY2(), DEFAULT_AMOUNT_TO_STEP_BY);
        spinner.setOnMouseClicked(event -> {
            Spinner source = (Spinner) event.getSource();
            node.setY2((Double) source.getValue());
        });
        add(spinner, 1, row);

        row++; //9
        label = new Label(RESOURCE_BUNDLE.getString("tanslationXIndex.label"));
        add(label, 0, row);
        spinner = getSpinner(0, canvasWidth, node.getX3(), DEFAULT_AMOUNT_TO_STEP_BY);
        spinner.setOnMouseClicked(event -> {
            Spinner source = (Spinner) event.getSource();
            node.setX3((Double) source.getValue());
        });
        add(spinner, 1, row);

        row++; //10
        label = new Label(RESOURCE_BUNDLE.getString("tanslationYIndex.label"));
        add(label, 0, row);
        spinner = getSpinner(0, canvasHeight, node.getY3(), DEFAULT_AMOUNT_TO_STEP_BY);
        spinner.setOnMouseClicked(event -> {
            Spinner source = (Spinner) event.getSource();
            node.setY3((Double) source.getValue());
        });
        add(spinner, 1, row);

        row++; //11
        add(new Separator(), 0, row, 2, 1);

        row++; //12
        label = new Label(RESOURCE_BUNDLE.getString("groupTranslate.label"));
        label.setFont(font("Georgia", BOLD, REGULAR, 12));
        label.setUnderline(true);
        label.setTextFill(BLUE);
        add(label, 0, row, 2, 1);

        row++; //13
        label = new Label(RESOURCE_BUNDLE.getString("groupTranslateX.label"));
        add(label, 0, row);
        textField = new DoubleTextField(String.valueOf(node.getTranslateX()));
        textField.setOnAction(event -> {
            TextField source = (TextField) event.getSource();
            node.setTranslateX(parseDouble(source.getText()));
        });
        add(textField, 1, row);

        row++; //14
        label = new Label(RESOURCE_BUNDLE.getString("groupTranslateY.label"));
        add(label, 0, row);
        spinner = getSpinner(0, canvasHeight, node.getTranslateY(), DEFAULT_AMOUNT_TO_STEP_BY);
        spinner.setOnMouseClicked(event -> {
            Spinner source = (Spinner) event.getSource();
            node.setTranslateY((Double) source.getValue());
        });
        add(spinner, 1, row);
    }

    private void addPartOfSpeechProperties(PartOfSpeechNode node) {
        int row = 5;

        Label label = new Label(RESOURCE_BUNDLE.getString("posCx.label"));
        add(label, 0, row);
        Spinner<Double> spinner = getSpinner(0, canvasWidth, node.getCx(), DEFAULT_AMOUNT_TO_STEP_BY);
        spinner.setOnMouseClicked(event -> {
            Spinner source = (Spinner) event.getSource();
            node.setCx((Double) source.getValue());
        });
        add(spinner, 1, row);

        row++; //6
        label = new Label(RESOURCE_BUNDLE.getString("posCy.label"));
        add(label, 0, row);
        spinner = getSpinner(0, canvasHeight, node.getCy(), DEFAULT_AMOUNT_TO_STEP_BY);
        spinner.setOnMouseClicked(event -> {
            Spinner source = (Spinner) event.getSource();
            node.setCy((Double) source.getValue());
        });
        add(spinner, 1, row);
    }

    private void addRelationshipNodeProperties(RelationshipNode node) {
        int row = 5;

        Label label;
        Spinner<Double> spinner;

        label = new Label(getString(node, "controlX1"));
        add(label, 0, row);
        spinner = getSpinner(0, canvasWidth, node.getControlX1(), DEFAULT_AMOUNT_TO_STEP_BY);
        label.setLabelFor(spinner);
        spinner.setOnMouseClicked(event -> {
            Spinner source = (Spinner) event.getSource();
            node.setControlX1((Double) source.getValue());
        });
        add(spinner, 1, row);

        row++; //6
        label = new Label(getString(node, "controlY1"));
        add(label, 0, row);
        spinner = getSpinner(0, canvasHeight, node.getControlY1(), DEFAULT_AMOUNT_TO_STEP_BY);
        label.setLabelFor(spinner);
        spinner.setOnMouseClicked(event -> {
            Spinner source = (Spinner) event.getSource();
            node.setControlY1((Double) source.getValue());
        });
        add(spinner, 1, row);

        row++; //7
        label = new Label(getString(node, "controlX2"));
        add(label, 0, row);
        spinner = getSpinner(0, canvasWidth, node.getControlX2(), DEFAULT_AMOUNT_TO_STEP_BY);
        label.setLabelFor(spinner);
        spinner.setOnMouseClicked(event -> {
            Spinner source = (Spinner) event.getSource();
            node.setControlX2((Double) source.getValue());
        });
        add(spinner, 1, row);

        row++; //8
        label = new Label(getString(node, "controlY2"));
        add(label, 0, row);
        spinner = getSpinner(0, canvasHeight, node.getControlY2(), DEFAULT_AMOUNT_TO_STEP_BY);
        label.setLabelFor(spinner);
        spinner.setOnMouseClicked(event -> {
            Spinner source = (Spinner) event.getSource();
            node.setControlY2((Double) source.getValue());
        });
        add(spinner, 1, row);

        row++; //9
        label = new Label(getString(node, "t1"));
        add(label, 0, row);
        spinner = getSpinnerThreeDecimalPlace(0, 1.000, node.getT1(), 0.005);
        label.setLabelFor(spinner);
        spinner.setOnMouseClicked(event -> {
            Spinner source = (Spinner) event.getSource();
            node.setT1((Double) source.getValue());
        });
        add(spinner, 1, row);

        row++; //10
        label = new Label(getString(node, "t2"));
        add(label, 0, row);
        spinner = getSpinnerThreeDecimalPlace(0, 1.000, node.getT2(), 0.005);
        label.setLabelFor(spinner);
        spinner.setOnMouseClicked(event -> {
            Spinner source = (Spinner) event.getSource();
            node.setT2((Double) source.getValue());
        });
        add(spinner, 1, row);
    }

    private void addPhraseNode(PhraseNode node) {
        int row = 5;

        Label label;
        Spinner<Double> spinner;

        label = new Label(RESOURCE_BUNDLE.getString("startXIndex.label"));
        add(label, 0, row);
        spinner = getSpinner(0, canvasWidth, node.getX1(), DEFAULT_AMOUNT_TO_STEP_BY);
        spinner.setOnMouseClicked(event -> {
            Spinner source = (Spinner) event.getSource();
            node.setX1((Double) source.getValue());
        });
        add(spinner, 1, row);

        row++; //6
        label = new Label(RESOURCE_BUNDLE.getString("startYIndex.label"));
        add(label, 0, row);
        spinner = getSpinner(0, canvasHeight, node.getY1(), DEFAULT_AMOUNT_TO_STEP_BY);
        spinner.setOnMouseClicked(event -> {
            Spinner source = (Spinner) event.getSource();
            node.setY1((Double) source.getValue());
        });
        add(spinner, 1, row);

        row++; //7
        label = new Label(RESOURCE_BUNDLE.getString("endXIndex.label"));
        add(label, 0, row);
        spinner = getSpinner(0, canvasWidth, node.getX2(), DEFAULT_AMOUNT_TO_STEP_BY);
        spinner.setOnMouseClicked(event -> {
            Spinner source = (Spinner) event.getSource();
            node.setX2((Double) source.getValue());
        });
        add(spinner, 1, row);

        row++; //8
        label = new Label(RESOURCE_BUNDLE.getString("endYIndex.label"));
        add(label, 0, row);
        spinner = getSpinner(0, canvasHeight, node.getY2(), DEFAULT_AMOUNT_TO_STEP_BY);
        spinner.setOnMouseClicked(event -> {
            Spinner source = (Spinner) event.getSource();
            node.setY2((Double) source.getValue());
        });
        add(spinner, 1, row);

        row++; //9
        label = new Label(RESOURCE_BUNDLE.getString("posCx.label"));
        add(label, 0, row);
        spinner = getSpinner(0, canvasWidth, node.getCx(), DEFAULT_AMOUNT_TO_STEP_BY);
        spinner.setOnMouseClicked(event -> {
            Spinner source = (Spinner) event.getSource();
            node.setCx((Double) source.getValue());
        });
        add(spinner, 1, row);

        row++; //10
        label = new Label(RESOURCE_BUNDLE.getString("posCy.label"));
        add(label, 0, row);
        spinner = getSpinner(0, canvasHeight, node.getCy(), DEFAULT_AMOUNT_TO_STEP_BY);
        spinner.setOnMouseClicked(event -> {
            Spinner source = (Spinner) event.getSource();
            node.setCy((Double) source.getValue());
        });
        add(spinner, 1, row);
    }

    private Spinner<Double> getSpinner(double min, double max, double initialValue, double amountToStepBy) {
        Spinner<Double> spinner = new Spinner<>();
        spinner.setValueFactory(new DoubleSpinnerValueFactory(min, max, initialValue, amountToStepBy));
        return spinner;
    }

    private Spinner<Double> getSpinnerThreeDecimalPlace(double min, double max,
                                                        double initialValue, double amountToStepBy) {
        Spinner<Double> spinner = new Spinner<>();
        DoubleSpinnerValueFactory valueFactory = new DoubleSpinnerValueFactory(min, max,
                initialValue, amountToStepBy);
        valueFactory.setConverter(THREE_DECIMAL_PLACE_CONVERTER);
        spinner.setValueFactory(valueFactory);
        return spinner;
    }
}
