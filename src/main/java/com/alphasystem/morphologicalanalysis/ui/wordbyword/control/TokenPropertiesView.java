package com.alphasystem.morphologicalanalysis.ui.wordbyword.control;

import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.skin.TokenPropertiesSkin;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

/**
 * @author sali
 */
public class TokenPropertiesView extends Control {

    private final ObjectProperty<Token> token = new SimpleObjectProperty<>(null, "token");

    public TokenPropertiesView() {
    }

    public final Token getToken() {
        return token.get();
    }

    public final void setToken(Token token) {
        this.token.set(token);
    }

    public final ObjectProperty<Token> tokenProperty() {
        return token;
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new TokenPropertiesSkin(this);
    }
}
