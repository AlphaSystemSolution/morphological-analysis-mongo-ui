package com.alphasystem.morphologicalanalysis.ui.access.control;

import com.alphasystem.morphologicalanalysis.ui.access.control.controller.DataExportController;
import com.alphasystem.morphologicalanalysis.ui.util.ApplicationContextProvider;
import javafx.scene.control.SkinBase;

/**
 * @author sali
 */
class DataExportSkin extends SkinBase<DataExportView> {

    /**
     * Constructor for all SkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    DataExportSkin(DataExportView control) {
        super(control);
        getChildren().setAll(ApplicationContextProvider.getBean(DataExportController.class));
    }
}
