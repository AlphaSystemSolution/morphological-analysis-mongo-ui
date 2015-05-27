package com.alphasystem.morphologicalanalysis.treebank.jfx.ui.util;

import com.alphasystem.morphologicalanalysis.model.Token;
import com.alphasystem.morphologicalanalysis.model.support.PartOfSpeech;
import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.GraphNode;
import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.TerminalNode;
import javafx.collections.ObservableList;

import java.util.List;

import static com.alphasystem.morphologicalanalysis.model.support.PartOfSpeech.DEFINITE_ARTICLE;
import static java.util.Collections.singletonList;
import static javafx.collections.FXCollections.observableArrayList;

/**
 * @author sali
 */
public class GraphBuilder {

    public static final double RECTANGLE_WIDTH = 100;
    public static final double RECTANGLE_HEIGHT = 100;
    public static final double GAP_BETWEEN_TOKENS = 60;
    public static final double INITIAL_X = 0;
    public static final double INITIAL_Y = 40;
    private static List<PartOfSpeech> IGNORE_LIST = singletonList(DEFINITE_ARTICLE);
    private static GraphBuilder instance;
    // initial values
    private double rectX = INITIAL_X;
    private double rectY = INITIAL_Y;
    private double textX = rectX + 10;
    private double textY = 105;
    private double x1 = rectX;
    private double y1 = RECTANGLE_HEIGHT + rectY;
    private double x2 = RECTANGLE_WIDTH + rectX;
    private double y2 = y1;

    private GraphBuilder() {
    }

    public synchronized static GraphBuilder getInstance() {
        if (instance == null) {
            instance = new GraphBuilder();
        }
        return instance;
    }

    public ObservableList<GraphNode> toGraphNodes(List<Token> tokens) {
        reset();
        ObservableList<GraphNode> results = observableArrayList();
        // first pass add the tokens first
        for (Token token : tokens) {
            results.add(buildTerminalNode(token));
        }
        return results;
    }

    private TerminalNode buildTerminalNode(Token token) {
        TerminalNode terminalNode = new TerminalNode(token, token.getDisplayName(), textX, textY, x1, y1, x2, y2);
        // update counters
        rectX = x2 + GAP_BETWEEN_TOKENS;
        textX = rectX + 30;
        x1 = rectX;
        x2 = RECTANGLE_WIDTH + rectX;
        return terminalNode;
    }

    private void reset() {
        rectX = INITIAL_X;
        rectY = INITIAL_Y;
        textX = rectX + 10;
        textY = 105;
        x1 = rectX;
        y1 = RECTANGLE_HEIGHT + rectY;
        x2 = RECTANGLE_WIDTH + rectX;
        y2 = y1;
    }
}
