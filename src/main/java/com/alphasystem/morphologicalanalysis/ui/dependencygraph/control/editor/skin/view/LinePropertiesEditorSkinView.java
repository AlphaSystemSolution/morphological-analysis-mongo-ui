package com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin.view;

import com.alphasystem.morphologicalanalysis.graph.model.LineSupport;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.LinePropertiesEditor;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.LineSupportAdapter;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.alphasystem.fx.ui.util.UiUtilities.loadFXML;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.RESOURCE_BUNDLE;
import static com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.PropertiesEditor.setupField;
import static com.alphasystem.util.AppUtil.getPath;

/**
 * @author sali
 */
public class LinePropertiesEditorSkinView<N extends LineSupport, A extends LineSupportAdapter<N>> extends GridPane {

    private final LinePropertiesEditor<N, A> control;

    @FXML private Spinner<Double> x1Spinner;
    @FXML private Slider x1Slider;
    @FXML private Spinner<Double> y1Spinner;
    @FXML private Slider y1Slider;
    @FXML private Spinner<Double> x2Spinner;
    @FXML private Slider x2Slider;
    @FXML private Spinner<Double> y2Spinner;
    @FXML private Slider y2Slider;

    public LinePropertiesEditorSkinView(LinePropertiesEditor<N, A> control) {
        this.control = control;
        try {
            loadFXML(this, getPath("fxml.editor.LinePropertiesEditor.fxml").toUri().toURL(), RESOURCE_BUNDLE);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings({"unchecked"})
    @FXML
    void initialize() {
        setupField(control.x1Property(), x1Spinner, x1Slider);
        setupField(control.y1Property(), y1Spinner, y1Slider);
        setupField(control.x2Property(), x2Spinner, x2Slider);
        setupField(control.y2Property(), y2Spinner, y2Slider);
    }
}
