package com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin;

import com.alphasystem.morphologicalanalysis.graph.model.GraphNode;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.PropertiesEditor;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin.view.SkinViewBase;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.GraphNodeAdapter;
import javafx.scene.control.SkinBase;

/**
 * @author sali
 */
public class PropertiesEditorSkin<N extends GraphNode, A extends GraphNodeAdapter<N>, S extends SkinViewBase<N, A>> extends SkinBase<PropertiesEditor<N, A>> {

    /**
     * Constructor for all SkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     * @param view view for this skin
     */
    public PropertiesEditorSkin(PropertiesEditor<N, A> control, S view) {
        super(control);
        getChildren().setAll(view);
    }
}
