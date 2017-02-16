package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control;

import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.skin.TokenPropertiesSkin;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import com.alphasystem.util.AppUtil;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author sali
 */
@Component
public class TokenPropertiesView extends Control {

    private final ObjectProperty<Token> token = new SimpleObjectProperty<>(this, "token");
    private final StringProperty translationText = new SimpleStringProperty(this, "translationText");
    private final ObjectProperty<Location> selectedLocation = new SimpleObjectProperty<>(this, "selectedLocation");

    @PostConstruct
    void postConstruct() {
        setSkin(createDefaultSkin());
        tokenProperty().addListener((observable, oldValue, newValue) -> initValues(newValue));
        translationTextProperty().addListener((observable, oldValue, newValue) -> setTokenTextInternal(newValue));
        getStyleClass().add("border");
    }

    private void initValues(Token token) {
        setTranslationText((token == null) ? null : token.getTranslation());
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

    private void setTokenTextInternal(String tokenText) {
        final Token token = getToken();
        if (token != null) {
            token.setToken(tokenText);
        }
    }

    public final StringProperty translationTextProperty() {
        return translationText;
    }

    public final void setTranslationText(String translationText) {
        this.translationText.set(translationText);
    }

    public final Location getSelectedLocation() {
        return selectedLocation.get();
    }

    public final ObjectProperty<Location> selectedLocationProperty() {
        return selectedLocation;
    }

    public final void setSelectedLocation(Location selectedLocation) {
        this.selectedLocation.set(selectedLocation);
    }

    private void setTranslationTextInternal(String translationText) {
        final Token token = getToken();
        if (token != null) {
            token.setTranslation(translationText);
        }
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new TokenPropertiesSkin(this);
    }
}
