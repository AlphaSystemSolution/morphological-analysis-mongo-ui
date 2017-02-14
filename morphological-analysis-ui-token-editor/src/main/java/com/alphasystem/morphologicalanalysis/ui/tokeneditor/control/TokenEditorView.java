package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control;

import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.skin.TokenEditorSkin;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import com.alphasystem.util.AppUtil;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author sali
 */
@Component
public class TokenEditorView extends Control {

    private final ObjectProperty<Token> token = new SimpleObjectProperty<>(this, "token");

    @PostConstruct
    void postConstruct() {
        setSkin(createDefaultSkin());
        tokenProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("NEW TOKEN: " + newValue);
        });
    }

    @Override
    public String getUserAgentStylesheet() {
        return AppUtil.getResource("styles/application.css").toExternalForm();
    }

    public final Token getToken() {
        return token.get();
    }

    public final ObjectProperty<Token> tokenProperty() {
        return token;
    }

    public final void setToken(Token token) {
        this.token.set(token);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new TokenEditorSkin(this);
    }
}
