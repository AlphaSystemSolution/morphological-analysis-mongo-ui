package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.skin;

import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.MorphologicalEntryView;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.controller.MorphologicalEntryController;
import com.alphasystem.morphologicalanalysis.ui.util.ApplicationContextProvider;
import javafx.scene.control.SkinBase;

/**
 * @author sali
 */
public class MorphologicalEntrySkin extends SkinBase<MorphologicalEntryView> {

    private MorphologicalEntryController controller;

    /**
     * Constructor for all SkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    public MorphologicalEntrySkin(MorphologicalEntryView control) {
        super(control);
        controller = ApplicationContextProvider.getBean(MorphologicalEntryController.class);
        getChildren().setAll(controller);
    }

    public void updateVerbalNouns() {
        controller.updateVerbalNouns();
    }
}
