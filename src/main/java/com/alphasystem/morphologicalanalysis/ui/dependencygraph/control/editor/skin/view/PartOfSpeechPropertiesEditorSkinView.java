package com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin.view;

import com.alphasystem.morphologicalanalysis.graph.model.PartOfSpeechNode;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.PartOfSpeechPropertiesEditor;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.PartOfSpeechNodeAdapter;

/**
 * @author sali
 */
public class PartOfSpeechPropertiesEditorSkinView extends LinkSupportPropertiesEditorSkinView<PartOfSpeechNode,
        PartOfSpeechNodeAdapter, PartOfSpeechPropertiesEditor> {

    public PartOfSpeechPropertiesEditorSkinView(PartOfSpeechPropertiesEditor control) {
        super(control);
    }
}
