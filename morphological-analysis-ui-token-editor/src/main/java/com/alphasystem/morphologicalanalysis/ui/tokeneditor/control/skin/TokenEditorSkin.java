package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.skin;

import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.TokenEditorView;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.controller.TokenEditorController;
import com.alphasystem.morphologicalanalysis.ui.util.ApplicationContextProvider;
import javafx.scene.control.SkinBase;

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
        getChildren().setAll(ApplicationContextProvider.getBean(TokenEditorController.class));
    }

}
