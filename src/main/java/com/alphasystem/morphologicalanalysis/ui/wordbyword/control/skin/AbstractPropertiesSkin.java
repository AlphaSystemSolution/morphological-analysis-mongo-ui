package com.alphasystem.morphologicalanalysis.ui.wordbyword.control.skin;

import com.alphasystem.morphologicalanalysis.ui.common.ComboBoxFactory;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.AbstractPropertiesView;
import com.alphasystem.morphologicalanalysis.wordbyword.model.AbstractProperties;
import javafx.scene.control.SkinBase;

/**
 * @author sali
 */
public abstract class AbstractPropertiesSkin<V extends AbstractProperties, T extends AbstractPropertiesView<V>>
        extends SkinBase<T> {

    protected static final ComboBoxFactory COMBO_BOX_FACTORY = ComboBoxFactory.getInstance();

    protected AbstractPropertiesSkin(T control) {
        super(control);
        initializeSkin();
    }

    protected abstract void initializeSkin();
}
