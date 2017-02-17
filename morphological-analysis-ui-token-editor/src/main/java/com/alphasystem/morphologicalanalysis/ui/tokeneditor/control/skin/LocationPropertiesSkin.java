package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.skin;

import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.LocationPropertiesView;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.controller.LocationPropertiesController;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.spring.support.ApplicationContextProvider;
import javafx.scene.control.SkinBase;

/**
 * @author sali
 */
public class LocationPropertiesSkin extends SkinBase<LocationPropertiesView> {

    /**
     * Constructor for all SkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    public LocationPropertiesSkin(LocationPropertiesView control) {
        super(control);
        getChildren().setAll(ApplicationContextProvider.getBean(LocationPropertiesController.class));
    }
}
