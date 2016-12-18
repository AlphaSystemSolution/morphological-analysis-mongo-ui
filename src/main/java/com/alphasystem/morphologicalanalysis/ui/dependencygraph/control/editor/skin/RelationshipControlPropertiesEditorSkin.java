package com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin;

import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.RelationshipControlPropertiesEditor;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin.view.RelationshipControlPropertiesEditorSkinView;
import javafx.scene.control.SkinBase;

/**
 * @author sali
 */
public class RelationshipControlPropertiesEditorSkin extends SkinBase<RelationshipControlPropertiesEditor> {

    /**
     * Constructor for all SkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    public RelationshipControlPropertiesEditorSkin(RelationshipControlPropertiesEditor control) {
        super(control);
        getChildren().setAll(new RelationshipControlPropertiesEditorSkinView(getSkinnable()));
    }
}
