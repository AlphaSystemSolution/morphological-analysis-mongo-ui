package com.alphasystem.access.model;

import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;

/**
 * @author sali
 */
public final class TokenAdapter {

    private final Token token;
    private final boolean highlight;

    public TokenAdapter(Token token, boolean highlight) {
        this.token = token;
        this.highlight = highlight;
    }

    public Token getToken() {
        return token;
    }

    public boolean isHighlight() {
        return highlight;
    }

    @Override
    public String toString() {
        return String.format("%s:%s", getToken(), isHighlight());
    }
}
