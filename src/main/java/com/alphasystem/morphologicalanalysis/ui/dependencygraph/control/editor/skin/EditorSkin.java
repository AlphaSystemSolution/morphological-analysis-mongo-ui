package com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin;

import com.alphasystem.morphologicalanalysis.graph.model.GraphNode;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.PropertiesEditor;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin.view.EditorSkinView;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.GraphNodeAdapter;
import javafx.scene.control.SkinBase;

/**
 * @author sali
 */
public class EditorSkin<N extends GraphNode, A extends GraphNodeAdapter<N>> extends SkinBase<PropertiesEditor<N, A>> {

    /**
     * Constructor for all SkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    public EditorSkin(PropertiesEditor<N, A> control) {
        super(control);
        getChildren().setAll(new EditorSkinView<N, A>(getSkinnable()));
    }
}
