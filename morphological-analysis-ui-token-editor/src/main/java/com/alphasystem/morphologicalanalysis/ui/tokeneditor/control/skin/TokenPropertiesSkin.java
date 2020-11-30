package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.skin;

import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.TokenPropertiesView;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.controller.TokenPropertiesController;
import com.alphasystem.morphologicalanalysis.ui.util.ApplicationContextProvider;
import javafx.scene.control.SkinBase;

/**
 * @author sali
 */
public class TokenPropertiesSkin extends SkinBase<TokenPropertiesView> {

    /**
     * Constructor for all SkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    public TokenPropertiesSkin(TokenPropertiesView control) {
        super(control);
        getChildren().setAll(ApplicationContextProvider.getBean(TokenPropertiesController.class));
    }
}
