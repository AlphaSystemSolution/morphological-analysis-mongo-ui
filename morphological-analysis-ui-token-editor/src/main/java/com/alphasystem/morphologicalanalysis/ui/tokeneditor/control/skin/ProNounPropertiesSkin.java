package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.skin;

import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.ProNounPropertiesView;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.controller.ParticlePropertiesController;
import com.alphasystem.morphologicalanalysis.ui.util.ApplicationContextProvider;
import javafx.scene.control.SkinBase;

/**
 * @author sali
 */
public class ProNounPropertiesSkin extends SkinBase<ProNounPropertiesView> {

    /**
     * Constructor for all SkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    public ProNounPropertiesSkin(ProNounPropertiesView control) {
        super(control);
        getChildren().setAll(ApplicationContextProvider.getBean(ParticlePropertiesController.class));
    }
}
