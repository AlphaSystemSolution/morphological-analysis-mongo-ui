package com.alphasystem.morphologicalanalysis.ui.dependencygraph.util;

import com.alphasystem.morphologicalanalysis.graph.model.DependencyGraph;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.*;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.PartOfSpeech;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.RelationshipType;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.shape.Line;

import java.util.List;

import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.PartOfSpeech.NOUN;
import static javafx.collections.FXCollections.observableArrayList;
import static javafx.collections.FXCollections.reverse;

/**
 * @author sali
 */
public class GraphBuilder {

    public static final double RECTANGLE_WIDTH = 100;
    public static final double RECTANGLE_HEIGHT = 100;
    public static final double GAP_BETWEEN_TOKENS = 60;
    public static final double INITIAL_X = 0;
    public static final double INITIAL_Y = 40;
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

    public ObservableList<GraphNode> toGraphNodes(DependencyGraph dependencyGraph) {
        return toGraphNodes(dependencyGraph.getTokens());
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
        for (GraphNode graphNode : results) {
            TerminalNode terminalNode = (TerminalNode) graphNode;
            double posX = terminalNode.getX1();
            ObservableList<PartOfSpeechNode> partOfSpeeches = terminalNode.getPartOfSpeeches();
            reverse(partOfSpeeches);
            for (PartOfSpeechNode posNode : partOfSpeeches) {
                updatePartOfSpeechNode(posNode, posX);
                posX += 50;
            }
        }

        return results;
    }

    /**
     * @param id
     * @param relationshipType
     * @param startPoint
     * @param endPoint
     * @return
     */
    public RelationshipNode buildRelationshipNode(String id, RelationshipType relationshipType,
                                                  Point2D startPoint, Point2D endPoint) {
        double startX = startPoint.getX();
        double startY = startPoint.getY();
        double endX = endPoint.getX();
        double endY = endPoint.getY();
        double controlY1 = startY + 100;
        double controlY2 = endY + 100;
        double x = (startX + endX) / 2;
        double y = (controlY1 + controlY2) / 2;

        return new RelationshipNode(relationshipType, id, x, y, startX, startY,
                startX, controlY1, endX, controlY2, endX, endY, 0.500, 0.550);
    }

    public PhraseNode buildPhraseNode(String id, PhraseSelectionModel model) {
        TerminalNode firstNode = model.getFirstNode();
        TerminalNode lastNode = model.getLastNode();
        double yOffset = 150.0;
        Double x1 = firstNode.getX1();
        Double y1 = firstNode.getY1() + yOffset;
        Double x2 = firstNode.getX2();
        Double y2 = firstNode.getY2() + yOffset;
        if (lastNode != null) {
            x2 = lastNode.getX2();
            y2 = lastNode.getY2() + yOffset;
        }
        Double x = (x1 + x2) / 2;
        Double y = (y1 + y2) / 2;
        Double cx = x + 15;
        Double cy = y + 15;

        return new PhraseNode(model.getRelationship(), id, x, y, x1, y1, x2, y2, cx, cy);
    }

    public EmptyNode buildEmptyNode(Line referenceLine) {
        reset();
        rectX = referenceLine.getEndX() + GAP_BETWEEN_TOKENS;
        textX = rectX + 30;
        x1 = rectX;
        x2 = RECTANGLE_WIDTH + rectX;
        x3 = rectX + 30;

        PartOfSpeech partOfSpeech = NOUN;
        Location location = new Location();
        location.setPartOfSpeech(partOfSpeech);
        PartOfSpeechNode partOfSpeechNode = new PartOfSpeechNode(location, 0d, 0d, 0d, 0d);
        EmptyNode emptyNode = new EmptyNode(null, textX, textY, x1, y1, x2, y2, x3, y3, partOfSpeechNode);

        reset();
        textY = 160;
        double posX = emptyNode.getX1();
        updatePartOfSpeechNode(emptyNode.getPartOfSpeeches().get(0), posX);
        return emptyNode;
    }


    /**
     * @param token
     * @return
     */
    private TerminalNode buildTerminalNode(Token token) {
        TerminalNode terminalNode = new TerminalNode(token, textX, textY, x1, y1, x2,
                y2, x3, y3, 0.0, 0.0);
        // update counters
        rectX = x2 + GAP_BETWEEN_TOKENS;
        textX = rectX + 30;
        x1 = rectX;
        x2 = RECTANGLE_WIDTH + rectX;
        x3 = rectX + 30;
        return terminalNode;
    }

    private void updatePartOfSpeechNode(PartOfSpeechNode posNode, double posX) {
        posNode.setX(posX);
        posNode.setY(textY);
        posNode.setCx(posX + 15);
        posNode.setCy(textY + 15);
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
