package com.alphasystem.morphologicalanalysis.ui.wordbyword.component;

import com.alphasystem.morphologicalanalysis.ui.common.ComboBoxFactory;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.model.TokenAdapter;
import javafx.scene.layout.GridPane;

/**
 * @author sali
 */
public abstract class AbstractPropertiesPane extends GridPane {

    protected final ComboBoxFactory instance = ComboBoxFactory.getInstance();
    protected final TokenAdapter tokenAdapter;

    public AbstractPropertiesPane(TokenAdapter tokenAdapter) {
        super();
        setHgap(10);
        setVgap(10);
        setPadding(TokenEditorDialog.DEFAULT_PADDING);

        this.tokenAdapter = tokenAdapter;

        initComboBoxes();
        initListeners();
        initPane();
    }

    protected abstract void initComboBoxes();

    protected abstract void initListeners();

    protected abstract void initPane();

    public abstract void updateComboBoxes();
}
