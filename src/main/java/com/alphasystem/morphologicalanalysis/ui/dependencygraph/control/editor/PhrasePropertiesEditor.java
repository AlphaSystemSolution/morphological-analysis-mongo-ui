package com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor;

import com.alphasystem.morphologicalanalysis.graph.model.PhraseNode;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin.view.PhrasePropertiesEditorSkinView;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.PhraseNodeAdapter;
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;

/**
 * @author sali
 */
public class PhrasePropertiesEditor extends LinkSupportPropertiesEditor<PhraseNode, PhraseNodeAdapter> {

    public PhrasePropertiesEditor() {
        super();
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new DefaultSkin(this);
    }

    private class DefaultSkin extends SkinBase<PhrasePropertiesEditor> {

        /**
         * Constructor for all SkinBase instances.
         *
         * @param control The control for which this Skin should attach to.
         */
        DefaultSkin(PhrasePropertiesEditor control) {
            super(control);
            getChildren().setAll(new PhrasePropertiesEditorSkinView(control));
        }
    }
}
