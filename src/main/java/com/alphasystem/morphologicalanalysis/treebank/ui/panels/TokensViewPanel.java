package com.alphasystem.morphologicalanalysis.treebank.ui.panels;

import com.alphasystem.morphologicalanalysis.model.Token;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author sali
 */
public class TokensViewPanel extends JPanel {

    private List<Token> tokens;

    public TokensViewPanel(List<Token> tokens){
        super(new BorderLayout());

        updateUI(tokens);
    }

    public void updateUI(List<Token> tokens){
        this.tokens = tokens;

        updateUI();
    }
}
