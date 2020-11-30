package com.alphasystem.morphologicalengine.ui.control.skin;

import com.alphasystem.app.morphologicalengine.spring.MorphologicalEngineFactory;
import com.alphasystem.morphologicalengine.ui.control.MorphologicalEngineView;
import com.alphasystem.morphologicalengine.ui.control.controller.MorphologicalEngineController;
import javafx.scene.control.SkinBase;

/**
 * @author sali
 */
public class MorphologicalEngineSkin extends SkinBase<MorphologicalEngineView> {

    /**
     * Constructor for all SkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    public MorphologicalEngineSkin(MorphologicalEngineView control) {
        super(control);
        getChildren().setAll(MorphologicalEngineFactory.getBean(MorphologicalEngineController.class));
    }
}
