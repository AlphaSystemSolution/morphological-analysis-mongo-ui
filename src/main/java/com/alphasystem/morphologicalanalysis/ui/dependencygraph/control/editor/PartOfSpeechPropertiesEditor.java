package com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor;

import com.alphasystem.morphologicalanalysis.graph.model.PartOfSpeechNode;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin.EditorSkin;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.PartOfSpeechNodeAdapter;
import javafx.scene.control.Skin;

/**
 * @author sali
 */
public class PartOfSpeechPropertiesEditor extends PropertiesEditor<PartOfSpeechNode, PartOfSpeechNodeAdapter> {

    @Override
    protected void initSkin() {
        setSkin(new EditorSkin<>(this));
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new EditorSkin<>(this);
    }
}
