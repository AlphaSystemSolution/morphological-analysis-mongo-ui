package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.skin;

import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.TokenEditorView;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.TokenPropertiesView;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.spring.support.ApplicationContextProvider;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;

/**
 * @author sali
 */
public class TokenEditorSkin extends SkinBase<TokenEditorView> {

    /**
     * Constructor for all SkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    public TokenEditorSkin(TokenEditorView control) {
        super(control);
        getChildren().setAll(new SkinView(control));
    }

    private static String getTokenPropertiesViewPaneTitle(Token token) {
        String title = "Token Properties - %s";
        String tokenDisplayName = token == null ? "Unknown" : token.getDisplayName();
        return String.format(title, tokenDisplayName);
    }

    private class SkinView extends BorderPane {

        private final TokenEditorView control;
        private final TokenPropertiesView tokenPropertiesView = ApplicationContextProvider.getBean(TokenPropertiesView.class);
        private final TitledPane tokenPropertiesViewPane = new TitledPane();

        private SkinView(TokenEditorView control) {
            this.control = control;
            tokenPropertiesViewPane.setText(getTokenPropertiesViewPaneTitle(this.control.getToken()));
            this.control.tokenProperty().addListener((observable, oldValue, newValue) -> {
                tokenPropertiesViewPane.setText(getTokenPropertiesViewPaneTitle(newValue));
                tokenPropertiesView.setToken(newValue);
            });
            initializeSkin();
        }

        private void initializeSkin() {
            tokenPropertiesViewPane.setContent(tokenPropertiesView);
            setTop(tokenPropertiesViewPane);
        }
    }
}
