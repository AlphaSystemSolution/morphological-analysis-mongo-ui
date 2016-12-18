package com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.relationship;

import com.alphasystem.morphologicalanalysis.graph.model.RelationshipNode;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.CommonPropertiesEditor;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin.PropertiesEditorSkin;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin.view.RelationshipCommonPropertiesEditorSkinView;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.RelationshipNodeAdapter;
import javafx.scene.control.Skin;

/**
 * @author sali
 */
public class RelationshipCommonPropertiesEditor extends CommonPropertiesEditor<RelationshipNode, RelationshipNodeAdapter> {

    public RelationshipCommonPropertiesEditor() {
        super();
        setSkin(createDefaultSkin());
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new PropertiesEditorSkin<>(this, new RelationshipCommonPropertiesEditorSkinView(this));
    }

}
