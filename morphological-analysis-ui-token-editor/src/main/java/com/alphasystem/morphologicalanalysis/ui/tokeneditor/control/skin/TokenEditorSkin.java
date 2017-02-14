package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.skin;

import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.TokenEditorView;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.BorderPane;

/**
 * @author sali
 */
public class TokenEditorSkin extends SkinBase<TokenEditorView> {

    /**
     * Constructor for all SkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    public TokenEditorSkin(TokenEditorView control) {
        super(control);
        getChildren().setAll(new SkinView(control));
    }

    private class SkinView extends BorderPane {

        private final TokenEditorView control;

        private SkinView(TokenEditorView control) {
            this.control = control;
        }
    }
}
