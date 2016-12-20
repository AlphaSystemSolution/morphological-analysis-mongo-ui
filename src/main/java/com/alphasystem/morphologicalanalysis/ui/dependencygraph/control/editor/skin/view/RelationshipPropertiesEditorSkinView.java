package com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin.view;

import com.alphasystem.morphologicalanalysis.graph.model.RelationshipNode;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.RelationshipPropertiesEditor;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.RelationshipNodeAdapter;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.DecimalFormatStringConverter;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.PropertyAccessor;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

/**
 * @author sali
 */
public class RelationshipPropertiesEditorSkinView extends PropertiesEditorSkinView<RelationshipNode, RelationshipNodeAdapter,
        RelationshipPropertiesEditor> {

    @FXML private Spinner<Double> x1Spinner;
    @FXML private Slider x1Slider;
    @FXML private Spinner<Double> y1Spinner;
    @FXML private Slider y1Slider;
    @FXML private Spinner<Double> x2Spinner;
    @FXML private Slider x2Slider;
    @FXML private Spinner<Double> y2Spinner;
    @FXML private Slider y2Slider;
    @FXML private Spinner<Double> t1Spinner;
    @FXML private Spinner<Double> t2Spinner;

    public RelationshipPropertiesEditorSkinView(RelationshipPropertiesEditor control) {
        super(control);
    }

    @Override
    void initialize(RelationshipNodeAdapter node) {
        super.initialize(node);
        x1Slider.setValue(node.getControlX1());
        x2Slider.setValue(node.getControlX2());
        y1Slider.setValue(node.getControlY1());
        y2Slider.setValue(node.getControlY2());
        t1Spinner.getValueFactory().setValue(node.getT1());
        t2Spinner.getValueFactory().setValue(node.getT2());
    }

    @Override
    void initializeValues() {
        super.initializeValues();
        setupSpinnerSliderField(control.x1Property(), x1Spinner, x1Slider, true);
        setupSpinnerSliderField(control.y1Property(), y1Spinner, y1Slider, false);
        setupSpinnerSliderField(control.x2Property(), x2Spinner, x2Slider, true);
        setupSpinnerSliderField(control.y2Property(), y2Spinner, y2Slider, false);
        setupField(control.t1Property(), t1Spinner);
        setupField(control.t2Property(), t2Spinner);
    }

    private void setupField(ObjectProperty<? extends PropertyAccessor<RelationshipNode, RelationshipNodeAdapter>> property, Spinner<Double> spinner) {
        final SpinnerValueFactory.DoubleSpinnerValueFactory valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1.000, property.get().get(), 0.005);
        valueFactory.setConverter(DecimalFormatStringConverter.THREE_DECIMAL_PLACE_CONVERTER);
        spinner.setValueFactory(valueFactory);
        spinner.setOnMouseClicked(event -> property.get().set(spinner.getValue()));
        property.addListener((observable, oldValue, newValue) -> spinner.getValueFactory().setValue(newValue.get()));
    }
}
