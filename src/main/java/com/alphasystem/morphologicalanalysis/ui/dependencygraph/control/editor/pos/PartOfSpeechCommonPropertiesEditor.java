package com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.pos;

import com.alphasystem.morphologicalanalysis.graph.model.PartOfSpeechNode;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.LinkSupportControlPropertiesEditor;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin.PropertiesEditorSkin;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin.view.PartOfSpeechCommonPropertiesEditorSkinView;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.PartOfSpeechNodeAdapter;
import javafx.scene.control.Skin;

/**
 * @author sali
 */
public class PartOfSpeechCommonPropertiesEditor extends LinkSupportControlPropertiesEditor<PartOfSpeechNode, PartOfSpeechNodeAdapter> {

    public PartOfSpeechCommonPropertiesEditor() {
        super();
        setSkin(createDefaultSkin());
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new PropertiesEditorSkin<>(this, new PartOfSpeechCommonPropertiesEditorSkinView(this));
    }
}
