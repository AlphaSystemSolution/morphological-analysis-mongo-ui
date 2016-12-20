package com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin.view;

import com.alphasystem.morphologicalanalysis.graph.model.LinkSupport;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.LinkSupportPropertiesEditor;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.LinkSupportAdapter;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;

/**
 * @author sali
 */
class LinkSupportPropertiesEditorSkinView<N extends LinkSupport, A extends LinkSupportAdapter<N>,
        P extends LinkSupportPropertiesEditor<N, A>> extends LineSupportPropertiesEditorSkinView<N, A, P> {

    @FXML private Spinner<Double> cxSpinner;
    @FXML private Slider cxSlider;
    @FXML private Spinner<Double> cySpinner;
    @FXML private Slider cySlider;

    LinkSupportPropertiesEditorSkinView(P control) {
        super(control);
    }

    @Override
    void initialize(A node) {
        super.initialize(node);
        cxSlider.setValue(node.getCx());
        cySlider.setValue(node.getCy());
    }

    @Override
    void initializeValues() {
        super.initializeValues();
        setupSpinnerSliderField(control.cxProperty(), cxSpinner, cxSlider, true);
        setupSpinnerSliderField(control.cyProperty(), cySpinner, cySlider, false);
    }
}
