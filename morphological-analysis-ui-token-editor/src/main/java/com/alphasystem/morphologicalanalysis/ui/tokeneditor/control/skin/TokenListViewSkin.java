package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.skin;

import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.TokenListView;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.controller.TokenListViewController;
import com.alphasystem.morphologicalanalysis.ui.util.ApplicationContextProvider;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import javafx.scene.control.SkinBase;

import java.util.List;

/**
 * @author sali
 */
public class TokenListViewSkin extends SkinBase<TokenListView> {

    private final TokenListViewController skinView;

    /**
     * Constructor for all SkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    public TokenListViewSkin(TokenListView control) {
        super(control);
        skinView = ApplicationContextProvider.getBean(TokenListViewController.class);
        getChildren().setAll(skinView);
    }

    public List<Token> getSelectedTokens() {
        return skinView.getSelectedTokens();
    }

}
