package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control;

import com.alphasystem.morphologicalanalysis.common.model.VerseTokenPairGroup;
import com.alphasystem.morphologicalanalysis.common.model.VerseTokensPair;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.skin.TokenListViewSkin;
import com.alphasystem.morphologicalanalysis.ui.util.RestClient;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import com.alphasystem.util.AppUtil;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sali
 */
@Component
public class TokenListView extends Control {

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
    private final ObjectProperty<Token> selectedToken = new SimpleObjectProperty<>(this, "selectedToken");

    @PostConstruct
    public void postConstruct() {
        setSkin(createDefaultSkin());
    }

    @Override
    public String getUserAgentStylesheet() {
        return AppUtil.getResource("styles/application.css").toExternalForm();
    }

    public final VerseTokenPairGroup getVerseTokenPairGroup() {
        return verseTokenPairGroup.get();
    }

    public final ObjectProperty<VerseTokenPairGroup> verseTokenPairGroupProperty() {
        return verseTokenPairGroup;
    }

    public final void setVerseTokenPairGroup(VerseTokenPairGroup verseTokenPairGroup) {
        this.verseTokenPairGroup.set(verseTokenPairGroup);
    }

    public final boolean isRefresh() {
        return refresh.get();
    }

    public final BooleanProperty refreshProperty() {
        return refresh;
    }

    public final void setRefresh(boolean refresh) {
        this.refresh.set(refresh);
    }

    public final Token getSelectedToken() {
        return selectedToken.get();
    }

    public final ObjectProperty<Token> selectedTokenProperty() {
        return selectedToken;
    }

    public final void setSelectedToken(Token selectedToken) {
        this.selectedToken.set(selectedToken);
    }

    public final RestClient getRestClient() {
        return restClient;
    }

    public List<Token> getSelectedTokens() {
        final TokenListViewSkin skin = (TokenListViewSkin) getSkin();
        return (skin == null) ? new ArrayList<>() : skin.getSelectedTokens();
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new TokenListViewSkin(this);
    }
}
