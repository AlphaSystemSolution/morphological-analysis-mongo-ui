package com.alphasystem.morphologicalanalysis.ui.access.model;

import com.alphasystem.morphologicalanalysis.ui.model.TokenCellModel;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * @author sali
 */
public class AccessTokenCellModel extends TokenCellModel {

    private final BooleanProperty highlighted = new SimpleBooleanProperty(this, "highlighted", false);

    public AccessTokenCellModel(Token token) {
        super(token);
    }

    public final boolean isHighlighted() {
        return highlighted.get();
    }

    public final BooleanProperty highlightedProperty() {
        return highlighted;
    }

    public final void setHighlighted(boolean highlighted) {
        this.highlighted.set(highlighted);
    }
}
