package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.skin;

import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.DetailEditorView;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.controller.DetailEditorController;
import com.alphasystem.morphologicalanalysis.ui.util.ApplicationContextProvider;
import javafx.scene.control.SkinBase;

/**
 * @author sali
 */
public class DetailEditorSkin extends SkinBase<DetailEditorView>{

    /**
     * Constructor for all SkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    public DetailEditorSkin(DetailEditorView control) {
        super(control);
        getChildren().setAll(ApplicationContextProvider.getBean(DetailEditorController.class));
    }
}
