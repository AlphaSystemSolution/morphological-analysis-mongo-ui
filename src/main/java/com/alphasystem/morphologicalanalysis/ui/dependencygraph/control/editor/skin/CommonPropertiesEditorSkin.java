package com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin;

import com.alphasystem.morphologicalanalysis.graph.model.GraphNode;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.CommonPropertiesEditor;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin.view.CommonPropertiesEditorSkinView;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.GraphNodeAdapter;
import javafx.scene.control.SkinBase;

/**
 * @author sali
 */
public class CommonPropertiesEditorSkin<N extends GraphNode, A extends GraphNodeAdapter<N>> extends SkinBase<CommonPropertiesEditor<N, A>> {

    /**
     * Constructor for all SkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    public CommonPropertiesEditorSkin(CommonPropertiesEditor<N, A> control) {
        super(control);
        getChildren().setAll(new CommonPropertiesEditorSkinView<>(getSkinnable()));
    }

}
