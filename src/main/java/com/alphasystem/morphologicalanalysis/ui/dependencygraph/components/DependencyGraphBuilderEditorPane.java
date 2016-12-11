package com.alphasystem.morphologicalanalysis.ui.dependencygraph.components;

import com.alphasystem.morphologicalanalysis.graph.model.GraphNode;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.*;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.DecimalFormatStringConverter;
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
import static com.alphasystem.util.AppUtil.isInstanceOf;
import static java.lang.String.format;
import static javafx.collections.FXCollections.observableArrayList;
import static javafx.geometry.Pos.TOP_CENTER;
import static javafx.scene.text.Font.font;

/**
 * @author sali
 */
public class DependencyGraphBuilderEditorPane extends BorderPane {

    private static final int DEFAULT_OFFSET = 3;
    public static final Insets DEFAULT_PADDING = new Insets(DEFAULT_OFFSET, DEFAULT_OFFSET,
            DEFAULT_OFFSET, DEFAULT_OFFSET);

    private final ObjectProperty<GraphNodeAdapter> graphNode = new SimpleObjectProperty<>();
    private final DoubleProperty canvasWidth = new SimpleDoubleProperty();
    private final DoubleProperty canvasHeight = new SimpleDoubleProperty();
    private final Accordion accordion;
    private int row = 0;

    public DependencyGraphBuilderEditorPane(GraphNodeAdapter graphNode, int width, int height) {
        canvasWidthProperty().addListener((observable, oldValue, newValue) -> {
            initPane();
        });
        canvasHeightProperty().addListener((observable, oldValue, newValue) -> {
            initPane();
        });
        graphNodeProperty().addListener((observable, oldValue, newValue) -> {
            initPane();
        });

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

    private TitledPane addCommonProperties(GraphNodeAdapter node) {
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
        addFields(gridPane, "xIndex.label", GraphNodeAdapter::getX, GraphNodeAdapter::setX, node,
                getCanvasWidth());
        addFields(gridPane, "yIndex.label", GraphNodeAdapter::getY, GraphNodeAdapter::setY, node,
                getCanvasHeight());

        String labelKey = format("%s_node.label", node.getGraphNodeType().name());
        return createTitledPane(labelKey, gridPane, true);
    }

    private TitledPane addLineProperties(LineSupportAdapter node) {
        GridPane gridPane = getGridPane();

        row = 0;
        addFields(gridPane, "startXIndex.label", LineSupportAdapter::getX1, LineSupportAdapter::setX1,
                node, getCanvasWidth());
        addFields(gridPane, "startYIndex.label", LineSupportAdapter::getY1, LineSupportAdapter::setY1,
                node, getCanvasHeight());
        addFields(gridPane, "endXIndex.label", LineSupportAdapter::getX2, LineSupportAdapter::setX2,
                node, getCanvasWidth());
        addFields(gridPane, "endYIndex.label", LineSupportAdapter::getY2, LineSupportAdapter::setY2,
                node, getCanvasHeight());

        return createTitledPane("lineProperties.label", gridPane);
    }

    private TitledPane addTranslationProperties(TerminalNodeAdapter node) {
        GridPane gridPane = getGridPane();

        row = 0;
        addFields(gridPane, "tanslationXIndex.label", TerminalNodeAdapter::getTranslationX,
                TerminalNodeAdapter::setTranslationX, node, getCanvasWidth());
        addFields(gridPane, "tanslationYIndex.label", TerminalNodeAdapter::getTranslationY,
                TerminalNodeAdapter::setTranslationY, node, getCanvasHeight());
        //TODO: add Translation Font properties

        return createTitledPane("translationProperties.label", gridPane);
    }

    private TitledPane addGroupTranslateProperties(TerminalNodeAdapter node) {
        GridPane gridPane = getGridPane();

        row = 0;
        Spinner<Double> xSpinner = addFields(gridPane, "groupTranslateX.label", TerminalNodeAdapter::getTranslateX,
                TerminalNodeAdapter::setTranslateX, node, -1 * getCanvasWidth(), getCanvasWidth());
        node.translateXProperty().addListener((observable, oldValue, newValue) -> {
            xSpinner.getValueFactory().setValue((Double) newValue);
        });
        Spinner<Double> ySpinner = addFields(gridPane, "groupTranslateY.label", TerminalNodeAdapter::getTranslateY,
                TerminalNodeAdapter::setTranslateY, node, -1 * getCanvasHeight(), getCanvasHeight());
        node.translateYProperty().addListener((observable, oldValue, newValue) -> {
            ySpinner.getValueFactory().setValue((Double) newValue);
        });

        return createTitledPane("groupTranslate.label", gridPane);
    }

    private TitledPane addRelationshipControlPointProperties(LinkSupportAdapter node) {
        GridPane gridPane = getGridPane();

        row = 0;
        addFields(gridPane, "posCx.label", LinkSupportAdapter::getCx, LinkSupportAdapter::setCx,
                node, getCanvasWidth());
        addFields(gridPane, "posCy.label", LinkSupportAdapter::getCy, LinkSupportAdapter::setCy,
                node, getCanvasHeight());

        return createTitledPane("controlPointProperties.label", gridPane);
    }

    private TitledPane[] addTerminalNodeProperties(TerminalNodeAdapter node) {
        return new TitledPane[]{addLineProperties(node), addTranslationProperties(node), addFontProperties(node),
                addGroupTranslateProperties(node)};
    }

    private TitledPane[] addPartOfSpeechProperties(PartOfSpeechNodeAdapter node) {
        return new TitledPane[]{addRelationshipControlPointProperties(node), addFontProperties(node)};
    }

    private TitledPane[] addRelationshipNodeProperties(RelationshipNodeAdapter node) {
        GridPane gridPane = getGridPane();

        row = 0;
        addFields(gridPane, "controlX1.label", RelationshipNodeAdapter::getControlX1,
                RelationshipNodeAdapter::setControlX1, node, getCanvasWidth());
        addFields(gridPane, "controlY1.label", RelationshipNodeAdapter::getControlY1,
                RelationshipNodeAdapter::setControlY1, node, getCanvasHeight());
        addFields(gridPane, "controlX2.label", RelationshipNodeAdapter::getControlX2,
                RelationshipNodeAdapter::setControlX2, node, getCanvasWidth());
        addFields(gridPane, "controlY2.label", RelationshipNodeAdapter::getControlY2,
                RelationshipNodeAdapter::setControlY2, node, getCanvasHeight());

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

        return new TitledPane[]{createTitledPane("controlPointProperties.label", gridPane), addFontProperties(node)};
    }

    private <T extends GraphNode> TitledPane addFontProperties(GraphNodeAdapter<T> node) {
        final Font font = node.getFont();
        GridPane gridPane = getGridPane();

        row = 0;
        Label label = new Label(RESOURCE_BUNDLE.getString("font.family.label"));
        gridPane.add(label, 0, row);
        TextField textField = new TextField(font.getFamily());
        textField.setEditable(false);
        label.setLabelFor(textField);
        gridPane.add(textField, 0, ++row);

        label = new Label(RESOURCE_BUNDLE.getString("font.size.label"));
        gridPane.add(label, 0, ++row);
        ComboBox<Integer> sizeComboBox = createSizeComboBox(node.fontProperty());
        label.setLabelFor(sizeComboBox);
        gridPane.add(sizeComboBox, 0, ++row);

        if (isInstanceOf(TerminalNodeAdapter.class, node)) {
            TerminalNodeAdapter tna = (TerminalNodeAdapter) node;
            label = new Label(RESOURCE_BUNDLE.getString("translationFont.family.label"));
            gridPane.add(label, 0, ++row);
            textField = new TextField(font.getFamily());
            textField.setEditable(false);
            label.setLabelFor(textField);
            gridPane.add(textField, 0, ++row);

            label = new Label(RESOURCE_BUNDLE.getString("translationFont.size.label"));
            gridPane.add(label, 0, ++row);
            sizeComboBox = createSizeComboBox(tna.translationFontProperty());
            label.setLabelFor(sizeComboBox);
            gridPane.add(sizeComboBox, 0, ++row);
        }

        return createTitledPane("fontProperties.label", gridPane);
    }

    private TitledPane[] addPhraseNodeProperties(PhraseNodeAdapter node) {
        return new TitledPane[]{addLineProperties(node), addRelationshipControlPointProperties(node), addFontProperties(node)};
    }

    private TitledPane createTitledPane(String labelKey, GridPane gridPane) {
        return createTitledPane(labelKey, gridPane, false);
    }

    private TitledPane createTitledPane(String labelKey, GridPane gridPane, boolean expanded) {
        TitledPane pane = new TitledPane(RESOURCE_BUNDLE.getString(labelKey), gridPane);
        pane.setExpanded(expanded);
        return pane;
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

    public final GraphNodeAdapter getGraphNode() {
        return graphNode.get();
    }

    public final void setGraphNode(GraphNodeAdapter graphNode) {
        this.graphNode.set(graphNode);
    }

    public final ObjectProperty<GraphNodeAdapter> graphNodeProperty() {
        return graphNode;
    }

    private Spinner<Double> getSpinner(double min, double max, double initialValue) {
        Spinner<Double> spinner = new Spinner<>();
        spinner.setValueFactory(new DoubleSpinnerValueFactory(min, max, initialValue, 0.5));
        return spinner;
    }

    private <T extends GraphNode> ComboBox<Integer> createSizeComboBox(ObjectProperty<Font> fontProperty) {
        final Font font = fontProperty.get();
        final ObservableList<Integer> values = observableArrayList(8, 9, 10, 11, 12, 14, 16, 18, 20, 22,
                24, 26, 28, 30, 36, 48, 72);
        ComboBox<Integer> comboBox = new ComboBox<>(values);
        comboBox.getSelectionModel().select(new Integer(new Double(font.getSize()).intValue()));
        comboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            fontProperty.setValue(font(font.getFamily(), FontWeight.NORMAL, FontPosture.REGULAR, newValue));
        });
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

    private <T extends GraphNodeAdapter, G extends GetterAdapter<T>, S extends SetterAdapter<T>> Spinner<Double> addFields(
            GridPane gridPane, String key, G g, S s, T node, double max) {
        return addFields(gridPane, key, g, s, node, 0, max);
    }

    private <T extends GraphNodeAdapter, G extends GetterAdapter<T>, S extends SetterAdapter<T>> Spinner<Double> addFields(
            GridPane gridPane, String key, G g, S s, T node, double min, double max) {
        Label label = new Label(RESOURCE_BUNDLE.getString(key));
        gridPane.add(label, 0, row);

        row++;
        Double initialValue = g.get(node);
        Spinner<Double> spinner = getSpinner(min, max, initialValue);
        spinner.setOnMouseClicked(event -> s.set(node, spinner.getValue()));
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
            s.set(node, d);
        });
        spinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            slider.setValue(newValue);
        });
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

    private interface GetterAdapter<T extends GraphNodeAdapter> {
        Double get(T node);
    }

    private interface SetterAdapter<T extends GraphNodeAdapter> {
        void set(T node, Double value);
    }
}
