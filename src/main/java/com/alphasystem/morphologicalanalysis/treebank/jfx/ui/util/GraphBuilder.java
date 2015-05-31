package com.alphasystem.morphologicalanalysis.treebank.jfx.ui.util;

import com.alphasystem.morphologicalanalysis.model.Location;
import com.alphasystem.morphologicalanalysis.model.Token;
import com.alphasystem.morphologicalanalysis.model.support.PartOfSpeech;
import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.GraphNode;
import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.PartOfSpeechNode;
import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.RelationshipNode;
import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.TerminalNode;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;

import static com.alphasystem.morphologicalanalysis.model.support.GrammaticalRelationship.NONE;
import static com.alphasystem.morphologicalanalysis.model.support.PartOfSpeech.DEFINITE_ARTICLE;
import static java.lang.String.format;
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
    private static List<PartOfSpeech> EXCLUDE_LIST = singletonList(DEFINITE_ARTICLE);
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
    private double x3 = rectX + 30;
    private double y3 = 50;

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
        for (int i = tokens.size() - 1; i >= 0; i--) {
            results.add(buildTerminalNode(tokens.get(i)));
        }

        reset();
        textY = 160;

        // second pass, part of speech tag
        List<GraphNode> posList = new ArrayList<>();
        for (GraphNode graphNode : results) {
            TerminalNode terminalNode = (TerminalNode) graphNode;
            posList.addAll(buildPartOfSpeechNode(terminalNode));
        }
        results.addAll(posList);

        return results;
    }

    public RelationshipNode buildRelationshipNode(String id, Point2D startPoint, Point2D endPoint) {
        double startX = startPoint.getX();
        double startY = startPoint.getY();
        double endX = endPoint.getX();
        double endY = endPoint.getY();
        double controlX1 = startX;
        double controlY1 = startY + 100;
        double controlX2 = endX;
        double controlY2 = endY + 100;
        double x = (controlX1 + controlX2) / 2;
        double y = (controlY1 + controlY2) / 2;
        RelationshipNode relationshipNode = new RelationshipNode(NONE, id, x, y, startX, startY, controlX1,
                controlY1, controlX2, controlY2, endX, endY, 0.500, 0.550);

        return relationshipNode;
    }

    private List<PartOfSpeechNode> buildPartOfSpeechNode(TerminalNode terminalNode) {
        Token token = terminalNode.getToken();
        List<Location> locations = token.getLocations();
        if (locations == null || locations.isEmpty()) {
            return new ArrayList<>();
        }
        List<PartOfSpeechNode> list = new ArrayList<>();
        double x1 = terminalNode.getX1();
        for (int i = locations.size() - 1; i >= 0; i--) {
            Location location = locations.get(i);
            PartOfSpeech partOfSpeech = location.getPartOfSpeech();
            boolean excluded = EXCLUDE_LIST.contains(partOfSpeech);
            if (excluded) {
                continue;
            }
            String id = format("%s_%s", partOfSpeech, location.getDisplayName());
            PartOfSpeechNode posNode = null;

            posNode = new PartOfSpeechNode(partOfSpeech, location, id, x1, textY,
                    (x1 + 15), (textY + 15), excluded);
            list.add(posNode);
            x1 += 50;
        }

        return list;
    }

    private TerminalNode buildTerminalNode(Token token) {
        TerminalNode terminalNode = new TerminalNode(token, token.getDisplayName(), textX, textY, x1, y1, x2,
                y2, x3, y3);
        // update counters
        rectX = x2 + GAP_BETWEEN_TOKENS;
        textX = rectX + 30;
        x1 = rectX;
        x2 = RECTANGLE_WIDTH + rectX;
        x3 = rectX + 30;
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
        x3 = rectX + 30;
        y3 = 50;
    }
}
