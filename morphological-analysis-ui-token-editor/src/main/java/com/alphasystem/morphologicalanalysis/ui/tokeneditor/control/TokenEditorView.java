package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control;

import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.skin.TokenEditorSkin;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import org.springframework.stereotype.Component;

/**
 * @author sali
 */
@Component
public class TokenEditorView extends Control {

    @Override
    protected Skin<?> createDefaultSkin() {
        return new TokenEditorSkin(this);
    }
}
