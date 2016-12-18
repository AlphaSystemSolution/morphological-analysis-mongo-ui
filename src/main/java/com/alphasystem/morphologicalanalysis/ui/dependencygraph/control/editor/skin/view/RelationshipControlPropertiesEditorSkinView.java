package com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin.view;

import com.alphasystem.morphologicalanalysis.graph.model.RelationshipNode;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.RelationshipControlPropertiesEditor;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.RelationshipNodeAdapter;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.DecimalFormatStringConverter;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.PropertyAccessor;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.DoubleSpinnerValueFactory;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.alphasystem.fx.ui.util.UiUtilities.loadFXML;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.RESOURCE_BUNDLE;
import static com.alphasystem.util.AppUtil.getPath;

/**
 * @author sali
 */
public class RelationshipControlPropertiesEditorSkinView extends SkinViewBase<RelationshipNode, RelationshipNodeAdapter> {

    private final RelationshipControlPropertiesEditor control;

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

    public RelationshipControlPropertiesEditorSkinView(RelationshipControlPropertiesEditor control) {
        this.control = control;
        try {
            loadFXML(this, getPath("fxml.editor.RelationshipControlPropertiesEditor.fxml").toUri().toURL(), RESOURCE_BUNDLE);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void initialize() {
        setupField(control.x1Property(), x1Spinner, x1Slider, 40, 200);
        setupField(control.y1Property(), y1Spinner, y1Slider, 40, 200);
        setupField(control.x2Property(), x2Spinner, x2Slider, 40, 200);
        setupField(control.y2Property(), y2Spinner, y2Slider, 40, 200);
        setupField(control.t1Property(), t1Spinner);
        setupField(control.t2Property(), t2Spinner);
    }

    private void setupField(ObjectProperty<? extends PropertyAccessor<RelationshipNode, RelationshipNodeAdapter>> property, Spinner<Double> spinner) {
        final DoubleSpinnerValueFactory valueFactory = new DoubleSpinnerValueFactory(0, 1.000, property.get().get(), 0.005);
        valueFactory.setConverter(DecimalFormatStringConverter.THREE_DECIMAL_PLACE_CONVERTER);
        spinner.setValueFactory(valueFactory);
        spinner.setOnMouseClicked(event -> property.get().set(spinner.getValue()));
        property.addListener((observable, oldValue, newValue) -> spinner.getValueFactory().setValue(newValue.get()));
    }
}
