package com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin.view;

import com.alphasystem.morphologicalanalysis.graph.model.GraphNode;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.CommonPropertiesEditor;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.PropertiesEditor;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.GraphNodeAdapter;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.alphasystem.fx.ui.util.UiUtilities.loadFXML;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.RESOURCE_BUNDLE;
import static com.alphasystem.util.AppUtil.getPath;
import static java.lang.String.format;

/**
 * @author sali
 */
public class EditorSkinView<N extends GraphNode, A extends GraphNodeAdapter<N>> extends BorderPane {

    protected final PropertiesEditor<N, A> control;

    @FXML protected Accordion accordion;
    @FXML protected TitledPane commonPropertiesPane;
    @FXML protected CommonPropertiesEditor<N, A> commonPropertiesEditor;

    public EditorSkinView(PropertiesEditor<N, A> control) {
        this.control = control;
        try {
            loadFXML(this, getPath(format("fxml.editor.%s.fxml", control.getClass().getSimpleName())).toUri().toURL(), RESOURCE_BUNDLE);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    protected void initialize() {
        accordion.setMinSize(100, 100);
        accordion.setPrefSize(250, 650);
        accordion.setExpandedPane(accordion.getPanes().get(0));

        control.nodeProperty().addListener((observable, oldValue, newValue) -> initialize(newValue));

    }

    @SuppressWarnings({"unchecked"})
    protected void initialize(A node) {
        if (node == null) {
            return;
        }
        accordion.getPanes().forEach(titledPane -> ((PropertiesEditor<N, A>) titledPane.getContent()).setNode(node));
        final String labelKey = format("%s_node.label", node.getGraphNodeType().name());
        commonPropertiesPane.setText(RESOURCE_BUNDLE.getString(labelKey));
    }

}
