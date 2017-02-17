package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.skin;

import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.CommonPropertiesView;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.controller.CommonPropertiesController;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.spring.support.ApplicationContextProvider;
import javafx.scene.control.SkinBase;

/**
 * @author sali
 */
public class CommonPropertiesSkin extends SkinBase<CommonPropertiesView> {

    /**
     * Constructor for all SkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    public CommonPropertiesSkin(CommonPropertiesView control) {
        super(control);
        getChildren().setAll(ApplicationContextProvider.getBean(CommonPropertiesController.class));
    }
}
