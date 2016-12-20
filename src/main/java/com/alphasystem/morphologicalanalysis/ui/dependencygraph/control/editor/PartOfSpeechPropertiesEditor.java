package com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor;

import com.alphasystem.morphologicalanalysis.graph.model.PartOfSpeechNode;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin.view.PartOfSpeechPropertiesEditorSkinView;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.PartOfSpeechNodeAdapter;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.TerminalNodeAdapter;
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;

/**
 * @author sali
 */
public class PartOfSpeechPropertiesEditor extends LinkSupportPropertiesEditor<PartOfSpeechNode, PartOfSpeechNodeAdapter> {

    public PartOfSpeechPropertiesEditor(PartOfSpeechNodeAdapter node) {
        super(node);
        setSkin(createDefaultSkin());
    }

    @Override
    protected void updateBounds(PartOfSpeechNodeAdapter node) {
        super.updateBounds(node);
        final TerminalNodeAdapter parent = (TerminalNodeAdapter) node.getParent();
        setLowerXBound(parent.getX1());
        setUpperXBound(parent.getX2());
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new DefaultSkin(this);
    }

    private class DefaultSkin extends SkinBase<PartOfSpeechPropertiesEditor> {

        /**
         * Constructor for all SkinBase instances.
         *
         * @param control The control for which this Skin should attach to.
         */
        DefaultSkin(PartOfSpeechPropertiesEditor control) {
            super(control);
            getChildren().setAll(new PartOfSpeechPropertiesEditorSkinView(control));
        }
    }
}
