package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.skin;

import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.VerbPropertiesView;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.controller.VerbPropertiesController;
import com.alphasystem.morphologicalanalysis.ui.util.ApplicationContextProvider;
import javafx.scene.control.SkinBase;

/**
 * @author sali
 */
public class VerbPropertiesSkin extends SkinBase<VerbPropertiesView> {

    /**
     * Constructor for all SkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    public VerbPropertiesSkin(VerbPropertiesView control) {
        super(control);
        getChildren().setAll(ApplicationContextProvider.getBean(VerbPropertiesController.class));
    }
}
