package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.skin;

import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.NounPropertiesView;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.controller.NounPropertiesController;
import com.alphasystem.morphologicalanalysis.ui.util.ApplicationContextProvider;
import javafx.scene.control.SkinBase;

/**
 * @author sali
 */
public class NounPropertiesSkin extends SkinBase<NounPropertiesView> {

    /**
     * Constructor for all SkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    public NounPropertiesSkin(NounPropertiesView control) {
        super(control);
        getChildren().setAll(ApplicationContextProvider.getBean(NounPropertiesController.class));
    }
}
