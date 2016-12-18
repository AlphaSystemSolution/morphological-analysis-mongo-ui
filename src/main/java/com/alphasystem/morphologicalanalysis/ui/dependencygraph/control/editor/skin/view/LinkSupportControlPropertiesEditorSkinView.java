package com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin.view;

import com.alphasystem.morphologicalanalysis.graph.model.LinkSupport;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.LinkSupportControlPropertiesEditor;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.LinkSupportAdapter;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;

/**
 * @author sali
 */
public class LinkSupportControlPropertiesEditorSkinView<N extends LinkSupport, A extends LinkSupportAdapter<N>> extends CommonPropertiesEditorSkinView<N, A> {

    @FXML private Spinner<Double> cxSpinner;
    @FXML private Slider cxSlider;
    @FXML private Spinner<Double> cySpinner;
    @FXML private Slider cySlider;

    public LinkSupportControlPropertiesEditorSkinView(LinkSupportControlPropertiesEditor<N, A> control) {
        super(control);
    }

    @Override
    void initialize() {
        super.initialize();
        final LinkSupportControlPropertiesEditor<N, A> control = (LinkSupportControlPropertiesEditor<N, A>) this.control;
        setupField(control.cxProperty(), cxSpinner, cxSlider);
        setupField(control.cyProperty(), cySpinner, cySlider);
    }
}
