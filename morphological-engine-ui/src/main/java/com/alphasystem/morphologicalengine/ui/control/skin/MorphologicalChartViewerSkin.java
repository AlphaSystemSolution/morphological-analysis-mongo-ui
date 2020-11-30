package com.alphasystem.morphologicalengine.ui.control.skin;

import com.alphasystem.app.morphologicalengine.spring.MorphologicalEngineFactory;
import com.alphasystem.morphologicalengine.ui.control.MorphologicalChartViewerControl;
import com.alphasystem.morphologicalengine.ui.control.controller.MorphologicalChartViewerController;
import javafx.scene.control.SkinBase;

/**
 * @author sali
 */
public class MorphologicalChartViewerSkin extends SkinBase<MorphologicalChartViewerControl> {

    /**
     * Constructor for all SkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    public MorphologicalChartViewerSkin(MorphologicalChartViewerControl control) {
        super(control);
        getChildren().setAll(MorphologicalEngineFactory.getBean(MorphologicalChartViewerController.class));
    }
}
