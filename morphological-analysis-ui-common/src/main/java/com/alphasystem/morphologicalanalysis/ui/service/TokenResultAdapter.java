package com.alphasystem.morphologicalanalysis.ui.service;

import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;

import java.util.List;

/**
 * @author sali
 */
public final class TokenResultAdapter {

    private final List<Token> tokens;

    TokenResultAdapter(List<Token> tokens) {
        this.tokens = tokens;
    }

    public List<Token> getTokens() {
        return tokens;
    }
}
