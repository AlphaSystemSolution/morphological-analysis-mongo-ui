package com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin;

import com.alphasystem.morphologicalanalysis.graph.model.TerminalNode;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.TranslationPropertiesEditor;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin.view.TranslationPropertiesEditorSkinView;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.GraphNodeAdapter;
import javafx.scene.control.SkinBase;

/**
 * @author sali
 */
public class TranslationPropertiesEditorSkin<N extends TerminalNode, A extends GraphNodeAdapter<N>> extends
        SkinBase<TranslationPropertiesEditor<N, A>> {

    /**
     * Constructor for all SkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    public TranslationPropertiesEditorSkin(TranslationPropertiesEditor<N, A> control) {
        super(control);
        getChildren().setAll(new TranslationPropertiesEditorSkinView<>(getSkinnable()));
    }
}
