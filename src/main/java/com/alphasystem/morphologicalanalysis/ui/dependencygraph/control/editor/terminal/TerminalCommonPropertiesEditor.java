package com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.terminal;

import com.alphasystem.morphologicalanalysis.graph.model.TerminalNode;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.CommonPropertiesEditor;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin.PropertiesEditorSkin;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin.view.TerminalCommonPropertiesEditorSkinView;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.TerminalNodeAdapter;
import javafx.scene.control.Skin;

/**
 * @author sali
 */
public class TerminalCommonPropertiesEditor extends CommonPropertiesEditor<TerminalNode, TerminalNodeAdapter> {

    public TerminalCommonPropertiesEditor() {
        super();
        setSkin(createDefaultSkin());
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new PropertiesEditorSkin<>(this, new TerminalCommonPropertiesEditorSkinView(this));
    }
}
