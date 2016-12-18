package com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor;

import com.alphasystem.morphologicalanalysis.graph.model.TerminalNode;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin.EditorSkin;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.TerminalNodeAdapter;
import javafx.scene.control.Skin;

/**
 * @author sali
 */
public class TerminalPropertiesEditor extends PropertiesEditor<TerminalNode, TerminalNodeAdapter> {

    public TerminalPropertiesEditor(){
        super();
        setSkin(new EditorSkin<>(this));
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new EditorSkin<>(this);
    }
}
