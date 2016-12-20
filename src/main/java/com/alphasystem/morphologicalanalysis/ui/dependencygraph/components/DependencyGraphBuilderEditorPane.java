package com.alphasystem.morphologicalanalysis.ui.dependencygraph.components;

import com.alphasystem.morphologicalanalysis.graph.model.GraphNode;
import com.alphasystem.morphologicalanalysis.graph.model.LineSupport;
import com.alphasystem.morphologicalanalysis.graph.model.LinkSupport;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.*;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.DecimalFormatStringConverter;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.PropertyAccessor;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.accessor.GraphNodePropertyAccessors.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.SpinnerValueFactory.DoubleSpinnerValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import static com.alphasystem.fx.ui.util.FontConstants.ARABIC_FONT_20;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.RESOURCE_BUNDLE;
import static java.lang.String.format;
import static javafx.collections.FXCollections.observableArrayList;
import static javafx.geometry.Pos.TOP_CENTER;
import static javafx.scene.text.Font.font;

/**
 * @author sali
 */
class DependencyGraphBuilderEditorPane extends BorderPane {

    private static final int DEFAULT_OFFSET = 3;
    private static final Insets DEFAULT_PADDING = new Insets(DEFAULT_OFFSET, DEFAULT_OFFSET,
            DEFAULT_OFFSET, DEFAULT_OFFSET);

    private final ObjectProperty<GraphNodeAdapter> graphNode = new SimpleObjectProperty<>();
    private final DoubleProperty canvasWidth = new SimpleDoubleProperty();
    private final DoubleProperty canvasHeight = new SimpleDoubleProperty();
    private final Accordion accordion;
    private int row = 0;

    DependencyGraphBuilderEditorPane(GraphNodeAdapter graphNode, int width, int height) {
        canvasWidthProperty().addListener((observable, oldValue, newValue) -> initPane());
        canvasHeightProperty().addListener((observable, oldValue, newValue) -> initPane());
        graphNodeProperty().addListener((observable, oldValue, newValue) -> initPane());

        setCanvasWidth(width);
        setCanvasHeight(height);
        setGraphNode(graphNode);

        accordion = new Accordion();
        accordion.setMinSize(100, 100);
        accordion.setPrefSize(250, 650);
        setCenter(accordion);
    }

    private void initPane() {
        ObservableList<TitledPane> panes = accordion.getPanes();
        panes.remove(0, panes.size());

        GraphNodeAdapter node = getGraphNode();
        if (node != null) {
            TitledPane commonPane = addCommonProperties(node);
            TitledPane[] titledPanes = null;
            switch (node.getGraphNodeType()) {
                case TERMINAL:
                case IMPLIED:
                case REFERENCE:
                case HIDDEN:
                    titledPanes = addTerminalNodeProperties((TerminalNodeAdapter) node);
                    break;
                case PART_OF_SPEECH:
                    titledPanes = addPartOfSpeechProperties((PartOfSpeechNodeAdapter) node);
                    break;
                case RELATIONSHIP:
                    titledPanes = addRelationshipNodeProperties((RelationshipNodeAdapter) node);
                    break;
                case PHRASE:
                    titledPanes = addPhraseNodeProperties((PhraseNodeAdapter) node);
                    break;
                default:
                    break;
            }
            panes.add(commonPane);
            panes.addAll(titledPanes);
            accordion.setExpandedPane(accordion.getPanes().get(0));
        }

        requestLayout();
    }

    private <N extends GraphNode, A extends GraphNodeAdapter<N>> TitledPane addCommonProperties(A node) {
        GridPane gridPane = getGridPane();

        row = 0;
        Label label = new Label(RESOURCE_BUNDLE.getString("text.label"));
        gridPane.add(label, 0, row);
        String value = node.getText();
        value = value == null ? "" : value;
        TextField textField = new TextField(value);
        textField.setOnAction(event -> {
            TextField source = (TextField) event.getSource();
            node.setText(source.getText());
        });
        row++;
        textField.setFont(ARABIC_FONT_20);
        label.setLabelFor(textField);
        gridPane.add(textField, 0, row);

        row++;
        addFields(gridPane, "xIndex.label", new XPropertyAccessor<>(node), getCanvasWidth());
        addFields(gridPane, "yIndex.label", new YPropertyAccessor<>(node), getCanvasHeight());

        row++;
        final Font font = node.getFont();
        label = new Label(RESOURCE_BUNDLE.getString("font.family.label"));
        gridPane.add(label, 0, row);
        textField = new TextField(font.getFamily());
        textField.setEditable(false);
        label.setLabelFor(textField);
        gridPane.add(textField, 0, ++row);

        label = new Label(RESOURCE_BUNDLE.getString("font.size.label"));
        gridPane.add(label, 0, ++row);
        ComboBox<Integer> sizeComboBox = createSizeComboBox(node.fontProperty());
        label.setLabelFor(sizeComboBox);
        gridPane.add(sizeComboBox, 0, ++row);

        String labelKey = format("%s_node.label", node.getGraphNodeType().name());
        return createTitledPane(labelKey, gridPane, true);
    }

    private <N extends LineSupport, A extends LineSupportAdapter<N>> TitledPane addLineProperties(A node) {
        GridPane gridPane = getGridPane();

        row = 0;
        //
        addFields(gridPane, "startXIndex.label", new X1PropertyAccessor<>(node), getCanvasWidth());
        addFields(gridPane, "startYIndex.label", new Y1PropertyAccessor<>(node), getCanvasHeight());
        addFields(gridPane, "endXIndex.label", new X2PropertyAccessor<>(node), getCanvasWidth());
        addFields(gridPane, "endYIndex.label", new Y2PropertyAccessor<>(node), getCanvasHeight());

        return createTitledPane("lineProperties.label", gridPane);
    }

    private TitledPane addTranslationProperties(TerminalNodeAdapter node) {
        GridPane gridPane = getGridPane();

        row = 0;
        addFields(gridPane, "translationXIndex.label", new TranslationXPropertyAccessor<>(node), getCanvasWidth());
        addFields(gridPane, "translationYIndex.label", new TranslationYPropertyAccessor<>(node), getCanvasHeight());

        row++;
        final Font font = node.getFont();
        Label label = new Label(RESOURCE_BUNDLE.getString("translationFont.family.label"));
        gridPane.add(label, 0, ++row);
        TextField textField = new TextField(font.getFamily());
        textField.setEditable(false);
        label.setLabelFor(textField);
        gridPane.add(textField, 0, ++row);

        label = new Label(RESOURCE_BUNDLE.getString("translationFont.size.label"));
        gridPane.add(label, 0, ++row);
        ComboBox<Integer> sizeComboBox = createSizeComboBox(node.translationFontProperty());
        label.setLabelFor(sizeComboBox);
        gridPane.add(sizeComboBox, 0, ++row);

        return createTitledPane("translationProperties.label", gridPane);
    }

    private TitledPane addGroupTranslateProperties(TerminalNodeAdapter node) {
        GridPane gridPane = getGridPane();

        row = 0;
        Spinner<Double> xSpinner = addFields(gridPane, "groupTranslateX.label", new TranslateXPropertyAccessor<>(node),
                -1 * getCanvasWidth(), getCanvasWidth());
        node.translateXProperty().addListener((observable, oldValue, newValue) -> xSpinner.getValueFactory().setValue((Double) newValue));
        Spinner<Double> ySpinner = addFields(gridPane, "groupTranslateY.label", new TranslateYPropertyAccessor<>(node),
                -1 * getCanvasHeight(), getCanvasHeight());
        node.translateYProperty().addListener((observable, oldValue, newValue) -> ySpinner.getValueFactory().setValue((Double) newValue));

        return createTitledPane("groupTranslate.label", gridPane);
    }

    private <N extends LinkSupport, A extends LinkSupportAdapter<N>> TitledPane addRelationshipControlPointProperties(A node) {
        GridPane gridPane = getGridPane();

        row = 0;
        addFields(gridPane, "posCx.label", new CxPropertyAccessor<>(node), getCanvasWidth());
        addFields(gridPane, "posCy.label", new CyPropertyAccessor<>(node), getCanvasHeight());

        return createTitledPane("controlPointProperties.label", gridPane);
    }

    private TitledPane[] addTerminalNodeProperties(TerminalNodeAdapter node) {
        return new TitledPane[]{addLineProperties(node), addTranslationProperties(node), addGroupTranslateProperties(node)};
    }

    private TitledPane[] addPartOfSpeechProperties(PartOfSpeechNodeAdapter node) {
        return new TitledPane[]{addRelationshipControlPointProperties(node)};
    }

    private TitledPane[] addRelationshipNodeProperties(RelationshipNodeAdapter node) {
        GridPane gridPane = getGridPane();

        row = 0;
        addFields(gridPane, "controlX1.label", new ControlX1PropertyAccessor(node), getCanvasWidth());
        addFields(gridPane, "controlY1.label", new ControlY1PropertyAccessor(node), getCanvasHeight());
        addFields(gridPane, "controlX2.label", new ControlX2PropertyAccessor(node), getCanvasWidth());
        addFields(gridPane, "controlY2.label", new ControlY2PropertyAccessor(node), getCanvasHeight());

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
        gridPane.add(spinner, 0, ++row);

        row++;
        label = new Label(RESOURCE_BUNDLE.getString("t2.label"));
        gridPane.add(label, 0, row);
        spinner = getSpinnerThreeDecimalPlace(0, 1.000, node.getT2(), 0.005);
        label.setLabelFor(spinner);
        spinner.setOnMouseClicked(event -> {
            Spinner source = (Spinner) event.getSource();
            node.setT2((Double) source.getValue());
        });
        gridPane.add(spinner, 0, ++row);

        return new TitledPane[]{createTitledPane("controlPointProperties.label", gridPane)};
    }

    private TitledPane[] addPhraseNodeProperties(PhraseNodeAdapter node) {
        return new TitledPane[]{addLineProperties(node), addRelationshipControlPointProperties(node)};
    }

    private TitledPane createTitledPane(String labelKey, GridPane gridPane) {
        return createTitledPane(labelKey, gridPane, false);
    }

    private TitledPane createTitledPane(String labelKey, GridPane gridPane, boolean expanded) {
        TitledPane pane = new TitledPane(RESOURCE_BUNDLE.getString(labelKey), gridPane);
        pane.setExpanded(expanded);
        return pane;
    }

    private double getCanvasWidth() {
        return canvasWidth.get();
    }

    final void setCanvasWidth(double canvasWidth) {
        this.canvasWidth.set(canvasWidth);
    }

    private DoubleProperty canvasWidthProperty() {
        return canvasWidth;
    }

    private double getCanvasHeight() {
        return canvasHeight.get();
    }

    final void setCanvasHeight(double canvasHeight) {
        this.canvasHeight.set(canvasHeight);
    }

    private DoubleProperty canvasHeightProperty() {
        return canvasHeight;
    }

    private GraphNodeAdapter getGraphNode() {
        return graphNode.get();
    }

    final void setGraphNode(GraphNodeAdapter graphNode) {
        this.graphNode.set(graphNode);
    }

    private ObjectProperty<GraphNodeAdapter> graphNodeProperty() {
        return graphNode;
    }

    private Spinner<Double> getSpinner(double min, double max, double initialValue) {
        Spinner<Double> spinner = new Spinner<>();
        spinner.setValueFactory(new DoubleSpinnerValueFactory(min, max, initialValue, 0.5));
        return spinner;
    }

    private ComboBox<Integer> createSizeComboBox(ObjectProperty<Font> fontProperty) {
        final Font font = fontProperty.get();
        final ObservableList<Integer> values = observableArrayList(8, 9, 10, 11, 12, 14, 16, 18, 20, 22,
                24, 26, 28, 30, 36, 48, 72);
        ComboBox<Integer> comboBox = new ComboBox<>(values);
        comboBox.getSelectionModel().select(new Integer(new Double(font.getSize()).intValue()));
        comboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                fontProperty.setValue(font(font.getFamily(), FontWeight.NORMAL, FontPosture.REGULAR, newValue)));
        return comboBox;
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

    private <N extends GraphNode, A extends GraphNodeAdapter<N>, P extends PropertyAccessor<N, A>> Spinner<Double>
    addFields(GridPane gridPane, String key, P accessor, double max) {
        return addFields(gridPane, key, accessor, 0, max);
    }

    private <N extends GraphNode, A extends GraphNodeAdapter<N>, P extends PropertyAccessor<N, A>> Spinner<Double>
    addFields(GridPane gridPane, String key, P accessor, double min, double max) {
        Label label = new Label(RESOURCE_BUNDLE.getString(key));
        gridPane.add(label, 0, row);

        row++;
        Double initialValue = accessor.get();
        Spinner<Double> spinner = getSpinner(min, max, initialValue);
        spinner.setOnMouseClicked(event -> accessor.set(spinner.getValue()));
        gridPane.add(spinner, 0, row);

        row++;
        Slider slider = createSlider(min, max, initialValue);
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            Double d = (Double) newValue;
            if (d % 1 != 0.0 && d % 0.5 != 0.0) {
                d = StrictMath.ceil(d);
            }
            slider.setValue(d);
            spinner.getValueFactory().setValue(d);
            accessor.set(d);
        });
        spinner.valueProperty().addListener((observable, oldValue, newValue) -> slider.setValue(newValue));
        gridPane.add(slider, 0, row);

        row++;

        return spinner;
    }

    private GridPane getGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(TOP_CENTER);
        gridPane.setHgap(DEFAULT_OFFSET);
        gridPane.setVgap(DEFAULT_OFFSET);
        gridPane.setPadding(DEFAULT_PADDING);
        return gridPane;
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

}
