package com.alphasystem.morphologicalanalysis.ui.wordbyword.control.skin;

import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.AbstractPropertiesView;
import com.alphasystem.morphologicalanalysis.wordbyword.model.AbstractProperties;
import javafx.scene.control.SkinBase;

/**
 * @author sali
 */
public abstract class AbstractPropertiesSkin<V extends AbstractProperties, T extends AbstractPropertiesView<V>>
        extends SkinBase<T> {

    protected AbstractPropertiesSkin(T control) {
        super(control);
        initializeSkin();
    }

    protected abstract void initializeSkin();
}
