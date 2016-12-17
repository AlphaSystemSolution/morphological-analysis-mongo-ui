package com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin.view;

import com.alphasystem.morphologicalanalysis.graph.model.TerminalNode;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.GroupTranslateEditor;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.GraphNodeAdapter;
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
public class GroupTranslateEditorSkinView<N extends TerminalNode, A extends GraphNodeAdapter<N>> extends GridPane {

    private final GroupTranslateEditor<N, A> control;

    @FXML private Spinner<Double> xSpinner;
    @FXML private Slider xSlider;
    @FXML private Spinner<Double> ySpinner;
    @FXML private Slider ySlider;

    public GroupTranslateEditorSkinView(GroupTranslateEditor<N, A> control) {
        this.control = control;
        try {
            loadFXML(this, getPath("fxml.editor.GroupTranslateEditor.fxml").toUri().toURL(), RESOURCE_BUNDLE);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void initialize() {
        setupField(control.xProperty(), xSpinner, xSlider);
        setupField(control.yProperty(), ySpinner, ySlider);
    }
}
