package com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin;

import com.alphasystem.morphologicalanalysis.graph.model.TerminalNode;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.GroupTranslateEditor;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin.view.GroupTranslateEditorSkinView;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.GraphNodeAdapter;
import javafx.scene.control.SkinBase;

/**
 * @author sali
 */
public class GroupTranslateEditorSkin<N extends TerminalNode, A extends GraphNodeAdapter<N>> extends
        SkinBase<GroupTranslateEditor<N, A>> {

    /**
     * Constructor for all SkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    public GroupTranslateEditorSkin(GroupTranslateEditor<N, A> control) {
        super(control);
        getChildren().setAll(new GroupTranslateEditorSkinView<>(getSkinnable()));
    }
}
