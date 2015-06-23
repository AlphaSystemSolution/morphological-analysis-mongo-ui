package com.alphasystem.morphologicalanalysis.ui.dependencygraph.util;

import com.alphasystem.morphologicalanalysis.graph.model.DependencyGraph;
import com.alphasystem.morphologicalanalysis.graph.model.Fragment;
import com.alphasystem.morphologicalanalysis.graph.model.Relationship;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.*;
import com.alphasystem.morphologicalanalysis.util.RepositoryTool;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.PartOfSpeech;
import com.alphasystem.morphologicalanalysis.wordbyword.repository.TokenRepository;
import javafx.collections.ObservableList;
import javafx.scene.shape.Line;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.PartOfSpeech.NOUN;
import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.PartOfSpeech.VERB;
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

    private RepositoryTool repositoryTool = RepositoryTool.getInstance();
    private Map<String, Token> emptyOrHiddenTokensMap = new HashMap<>();


    private GraphBuilder() {
        TokenRepository tokenRepository = repositoryTool.getRepositoryUtil().getTokenRepository();
        emptyOrHiddenTokensMap.put(NOUN.name(), tokenRepository.findByDisplayName("0:1:1"));
        emptyOrHiddenTokensMap.put(VERB.name(), tokenRepository.findByDisplayName("0:1:2"));
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

    public RelationshipNode buildRelationshipNode(Relationship relationship, LinkSupport dependentNode,
                                                  LinkSupport ownerNode) {
        RelationshipNode relationshipNode = new RelationshipNode();
        relationshipNode.setRelationship(relationship);
        relationshipNode.setDependentNode(dependentNode);
        relationshipNode.setOwnerNode(ownerNode);
        double startX = relationshipNode.getStartX();
        double startY = relationshipNode.getStartY();
        double endX = relationshipNode.getEndX();
        double endY = relationshipNode.getEndY();
        double controlY1 = startY + 100d;
        double controlY2 = endY + 100d;
        relationshipNode.setControlX1(startX);
        relationshipNode.setControlY1(controlY1);
        relationshipNode.setControlX2(endX);
        relationshipNode.setControlY2(controlY2);
        relationshipNode.setX((startX + endX) / 2);
        relationshipNode.setY((controlY1 + controlY2) / 2);
        return relationshipNode;
    }

    public PhraseNode buildPhraseNode(Fragment fragment, List<TerminalNode> terminalNodes) {
        double yOffset = 150.0;
        TerminalNode firstNode = terminalNodes.get(terminalNodes.size() - 1);
        TerminalNode lastNode = terminalNodes.get(0);
        Double x1 = firstNode.getX1();
        Double y1 = firstNode.getY1() + yOffset;
        Double x2 = lastNode.getX2();
        Double y2 = lastNode.getY2() + yOffset;
        Double x = (x1 + x2) / 2;
        Double y = (y1 + y2) / 2;
        Double cx = x + 15;
        Double cy = y + 15;


        return new PhraseNode(fragment, x, y, x1, y1, x2, y2, cx, cy);
    }

    public EmptyNode buildEmptyNode(Line referenceLine, PartOfSpeech partOfSpeech) {
        reset();
        rectX = referenceLine.getEndX() + GAP_BETWEEN_TOKENS;
        textX = rectX + 30;
        x1 = rectX;
        x2 = RECTANGLE_WIDTH + rectX;
        x3 = rectX + 30;

        Token token = emptyOrHiddenTokensMap.get(partOfSpeech.name());
        Location location = token.getLocations().get(0);

        PartOfSpeechNode partOfSpeechNode = new PartOfSpeechNode(location, 0d, 0d, 0d, 0d);
        EmptyNode emptyNode = new EmptyNode(token, textX, textY, x1, y1, x2, y2, x3, y3, 0d, 0d, partOfSpeechNode);

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
