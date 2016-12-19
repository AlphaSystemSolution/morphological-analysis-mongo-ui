package com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin.view;

import com.alphasystem.morphologicalanalysis.graph.model.LineSupport;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.LineSupportPropertiesEditor;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.LineSupportAdapter;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;

/**
 * @author sali
 */
public abstract class LineSupportPropertiesEditorSkinView<N extends LineSupport, A extends LineSupportAdapter<N>,
        P extends LineSupportPropertiesEditor<N, A>> extends PropertiesEditorSkinView<N, A, P> {

    @FXML private Spinner<Double> x1Spinner;
    @FXML private Slider x1Slider;
    @FXML private Spinner<Double> y1Spinner;
    @FXML private Slider y1Slider;
    @FXML private Spinner<Double> x2Spinner;
    @FXML private Slider x2Slider;
    @FXML private Spinner<Double> y2Spinner;
    @FXML private Slider y2Slider;

    LineSupportPropertiesEditorSkinView(P control) {
        super(control);
    }

    @Override
    void initializeValues() {
        super.initializeValues();
        setupSpinnerSliderField(control.x1Property(), x1Spinner, x1Slider, true);
        setupSpinnerSliderField(control.y1Property(), y1Spinner, y1Slider, false);
        setupSpinnerSliderField(control.x2Property(), x2Spinner, x2Slider, true);
        setupSpinnerSliderField(control.y2Property(), y2Spinner, y2Slider, false);
    }
}
