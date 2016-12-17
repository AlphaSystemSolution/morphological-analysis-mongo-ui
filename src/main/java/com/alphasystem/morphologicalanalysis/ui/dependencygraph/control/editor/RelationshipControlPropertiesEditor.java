package com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor;

import com.alphasystem.morphologicalanalysis.graph.model.LinkSupport;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin.RelationshipControlPropertiesEditorSkin;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.LinkSupportAdapter;
import javafx.scene.control.Skin;

/**
 * @author sali
 */
public class RelationshipControlPropertiesEditor<N extends LinkSupport, A extends LinkSupportAdapter<N>> extends PropertiesEditor<N, A> {

    @Override
    protected void initSkin() {
        setSkin(new RelationshipControlPropertiesEditorSkin<>(this));
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new RelationshipControlPropertiesEditorSkin<>(this);
    }
}
