package com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin;

import com.alphasystem.morphologicalanalysis.graph.model.LinkSupport;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.RelationshipControlPropertiesEditor;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin.view.RelationshipControlPropertiesEditorSkinView;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.LinkSupportAdapter;
import javafx.scene.control.SkinBase;

/**
 * @author sali
 */
public class RelationshipControlPropertiesEditorSkin<N extends LinkSupport, A extends LinkSupportAdapter<N>>
        extends SkinBase<RelationshipControlPropertiesEditor<N, A>> {

    /**
     * Constructor for all SkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    public RelationshipControlPropertiesEditorSkin(RelationshipControlPropertiesEditor<N, A> control) {
        super(control);
        getChildren().setAll(new RelationshipControlPropertiesEditorSkinView<>(getSkinnable()));
    }
}
