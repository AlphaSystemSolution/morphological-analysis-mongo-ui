package com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor;

import com.alphasystem.morphologicalanalysis.graph.model.PhraseNode;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin.EditorSkin;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.PhraseNodeAdapter;
import javafx.scene.control.Skin;

/**
 * @author sali
 */
public class PhrasePropertiesEditor extends PropertiesEditor<PhraseNode, PhraseNodeAdapter> {

    @Override
   protected void initSkin() {
        setSkin(new EditorSkin<>(this));
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new EditorSkin<>(this);
    }
}
