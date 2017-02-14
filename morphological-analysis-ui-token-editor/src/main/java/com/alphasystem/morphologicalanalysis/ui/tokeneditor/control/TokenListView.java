package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control;

import com.alphasystem.fx.ui.util.UiUtilities;
import com.alphasystem.morphologicalanalysis.common.model.VerseTokenPairGroup;
import com.alphasystem.morphologicalanalysis.common.model.VerseTokensPair;
import com.alphasystem.morphologicalanalysis.ui.model.TokenCellModel;
import com.alphasystem.morphologicalanalysis.ui.util.ApplicationHelper;
import com.alphasystem.morphologicalanalysis.ui.util.RestClient;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.BorderPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sali
 */
@Component
public class TokenListView extends BorderPane {

    private static VerseTokenPairGroup initialGroup = new VerseTokenPairGroup();

    static {
        initialGroup.setChapterNumber(1);
        initialGroup.getPairs().add(new VerseTokensPair(1, 1));
        initialGroup.getPairs().add(new VerseTokensPair(2, 1));
        initialGroup.getPairs().add(new VerseTokensPair(3, 1));
        initialGroup.getPairs().add(new VerseTokensPair(4, 1));
    }

    @Autowired private RestClient restClient;
    private final ObjectProperty<VerseTokenPairGroup> verseTokenPairGroup = new SimpleObjectProperty<>(this, "verseTokenPairGroup", initialGroup);
    private final BooleanProperty refresh = new SimpleBooleanProperty(this, "refresh", true);
    private final ListView<TokenCellModel> listView;

    public TokenListView() {
        listView = new ListView<>();
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listView.setCellFactory(param -> new TokenCheckBoxListCell());
        setCenter(UiUtilities.wrapInScrollPane(listView));
    }

    @PostConstruct
    public void postConstruct() {
        refresh.addListener((observable, oldValue, newValue) -> refreshList(verseTokenPairGroup.get(), newValue));
        verseTokenPairGroup.addListener((observable, oldValue, newValue) -> refreshList(newValue, refresh.get()));
        refreshList(verseTokenPairGroup.get(), refresh.get());
    }

    private void refreshList(VerseTokenPairGroup group, boolean refresh) {
        listView.getItems().remove(0, listView.getItems().size());
        if (group != null) {
            final List<Token> tokens = restClient.getTokens(group, refresh);
            if (tokens != null && !tokens.isEmpty()) {
                tokens.forEach(token -> listView.getItems().add(new TokenCellModel(token)));
                listView.getSelectionModel().selectFirst();
                listView.setPrefHeight(ApplicationHelper.calculateTableHeight(listView.getItems().size()));
            }
        }
    }

    public void setVerseTokenPairGroup(VerseTokenPairGroup verseTokenPairGroup) {
        this.verseTokenPairGroup.set(verseTokenPairGroup);
    }

    public void setRefresh(boolean refresh) {
        this.refresh.set(refresh);
    }

    public List<Token> getSelectedTokens() {
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
