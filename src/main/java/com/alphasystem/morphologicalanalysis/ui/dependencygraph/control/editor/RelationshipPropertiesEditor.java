package com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor;

import com.alphasystem.morphologicalanalysis.graph.model.RelationshipNode;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin.EditorSkin;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.RelationshipNodeAdapter;
import javafx.scene.control.Skin;

/**
 * @author sali
 */
public class RelationshipPropertiesEditor extends PropertiesEditor<RelationshipNode, RelationshipNodeAdapter> {

    public RelationshipPropertiesEditor(){
        super();
        setSkin(new EditorSkin<>(this));
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new EditorSkin<>(this);
    }
}
