package com.alphasystem.morphologicalanalysis.wordbyword.ui.component;

import com.alphasystem.morphologicalanalysis.ui.common.ComboBoxFactory;
import com.alphasystem.morphologicalanalysis.wordbyword.ui.model.TokenAdapter;
import javafx.scene.layout.GridPane;

import static com.alphasystem.morphologicalanalysis.wordbyword.ui.component.TokenEditorDialog.DEFAULT_PADDING;

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
        setPadding(DEFAULT_PADDING);

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
