package com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin.view;

import com.alphasystem.morphologicalanalysis.graph.model.LinkSupport;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.RelationshipControlPropertiesEditor;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.LinkSupportAdapter;
import javafx.scene.layout.GridPane;

/**
 * @author sali
 */
public class RelationshipControlPropertiesEditorSkinView<N extends LinkSupport, A extends LinkSupportAdapter<N>> extends GridPane {

    private final RelationshipControlPropertiesEditor<N, A> control;

    public RelationshipControlPropertiesEditorSkinView(RelationshipControlPropertiesEditor<N, A> control) {
        this.control = control;
    }
}
