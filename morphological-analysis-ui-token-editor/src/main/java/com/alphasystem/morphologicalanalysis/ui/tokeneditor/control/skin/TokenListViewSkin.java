package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.skin;

import com.alphasystem.fx.ui.util.UiUtilities;
import com.alphasystem.morphologicalanalysis.common.model.VerseTokenPairGroup;
import com.alphasystem.morphologicalanalysis.ui.model.TokenCellModel;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.TokenCheckBoxListCell;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.TokenListView;
import com.alphasystem.morphologicalanalysis.ui.util.ApplicationHelper;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.BorderPane;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sali
 */
public class TokenListViewSkin extends SkinBase<TokenListView> {

    private final SkinView skinView;

    /**
     * Constructor for all SkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    public TokenListViewSkin(TokenListView control) {
        super(control);
        skinView = new SkinView(control);
        getChildren().setAll(skinView);
    }

    public List<Token> getSelectedTokens() {
        return skinView.getSelectedTokens();
    }

    private class SkinView extends BorderPane {

        private final TokenListView control;
        private final ListView<TokenCellModel> listView;

        private SkinView(TokenListView control) {
            this.control = control;
            listView = new ListView<>();
            listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            listView.setCellFactory(param -> new TokenCheckBoxListCell());
            setCenter(UiUtilities.wrapInScrollPane(listView));

            listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                    control.setSelectedToken((newValue == null) ? null : newValue.getToken()));
            control.refreshProperty().addListener((observable, oldValue, newValue) -> refreshList(control.getVerseTokenPairGroup(), newValue));
            control.verseTokenPairGroupProperty().addListener((observable, oldValue, newValue) -> refreshList(newValue, control.isRefresh()));
            refreshList(control.getVerseTokenPairGroup(), control.isRefresh());
        }

        private void refreshList(VerseTokenPairGroup group, boolean refresh) {
            listView.getItems().remove(0, listView.getItems().size());
            if (group != null) {
                final List<Token> tokens = control.getRestClient().getTokens(group, refresh);
                if (tokens != null && !tokens.isEmpty()) {
                    tokens.forEach(token -> listView.getItems().add(new TokenCellModel(token)));
                    listView.getSelectionModel().selectFirst();
                    control.setSelectedToken(null);
                    control.setSelectedToken(tokens.get(0));
                    listView.setPrefHeight(ApplicationHelper.calculateTableHeight(listView.getItems().size()));
                }
            }
        }

        private List<Token> getSelectedTokens() {
            List<Token> tokens = new ArrayList<>();
            final ObservableList<TokenCellModel> items = listView.getItems();
            if (items != null && !items.isEmpty()) {
                items.forEach(model -> {
                    if (model.isChecked()) {
                        tokens.add(model.getToken());
                    }
                });
            }
            return tokens;
        }
    }
}
