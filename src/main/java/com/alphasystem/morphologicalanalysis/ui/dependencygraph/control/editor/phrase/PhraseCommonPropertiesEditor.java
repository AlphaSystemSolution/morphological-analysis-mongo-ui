package com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.phrase;

import com.alphasystem.morphologicalanalysis.graph.model.PhraseNode;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.LinkSupportControlPropertiesEditor;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin.PropertiesEditorSkin;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin.view.PhraseCommonPropertiesEditorSkinView;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.PhraseNodeAdapter;
import javafx.scene.control.Skin;

/**
 * @author sali
 */
public class PhraseCommonPropertiesEditor extends LinkSupportControlPropertiesEditor<PhraseNode, PhraseNodeAdapter> {

    public PhraseCommonPropertiesEditor() {
        super();
        setSkin(createDefaultSkin());
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new PropertiesEditorSkin<>(this, new PhraseCommonPropertiesEditorSkinView(this));
    }
}
