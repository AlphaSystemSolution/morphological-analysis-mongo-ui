package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.controller;

import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.TokenEditorView;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.TokenPropertiesView;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author sali
 */
@Component
public class TokenEditorController extends BorderPane {

    @Autowired private TokenEditorView control;
    @Autowired TokenPropertiesView tokenPropertiesView;

    private static String getTokenPropertiesViewPaneTitle(Token token) {
        String title = "Token Properties - %s";
        String tokenDisplayName = token == null ? "Unknown" : token.getDisplayName();
        return String.format(title, tokenDisplayName);
    }

    @PostConstruct
    void postConstruct() {
        TitledPane tokenPropertiesViewPane = new TitledPane(getTokenPropertiesViewPaneTitle(this.control.getToken()), tokenPropertiesView);
        control.tokenProperty().addListener((observable, oldValue, newValue) -> {
            tokenPropertiesViewPane.setText(getTokenPropertiesViewPaneTitle(newValue));
            tokenPropertiesView.setToken(newValue);
        });
        setTop(tokenPropertiesViewPane);
    }
}
