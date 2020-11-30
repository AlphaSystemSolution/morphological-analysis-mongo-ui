package com.alphasystem.morphologicalanalysis.ui.model;

import com.alphasystem.morphologicalanalysis.common.model.VerseTokenPairGroup;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Chapter;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import org.springframework.stereotype.Component;

/**
 * @author sali
 */
@Component
public final class ApplicationState {

    private Chapter chapter;
    private VerseTokenPairGroup verseTokenPairGroup;
    private Token token;
    private Location location;

    public Chapter getChapter() {
        return chapter;
    }

    public void setChapter(Chapter chapter) {
        this.chapter = chapter;
    }

    public VerseTokenPairGroup getVerseTokenPairGroup() {
        return verseTokenPairGroup;
    }

    public void setVerseTokenPairGroup(VerseTokenPairGroup verseTokenPairGroup) {
        this.verseTokenPairGroup = verseTokenPairGroup;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
