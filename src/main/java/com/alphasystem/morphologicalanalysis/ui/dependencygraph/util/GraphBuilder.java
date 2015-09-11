package com.alphasystem.morphologicalanalysis.ui.dependencygraph.util;

import com.alphasystem.morphologicalanalysis.graph.model.*;
import com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.LinkSupportAdapter;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.PartOfSpeechNodeAdapter;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.TerminalNodeAdapter;
import com.alphasystem.morphologicalanalysis.util.MorphologicalAnalysisRepositoryUtil;
import com.alphasystem.morphologicalanalysis.util.RepositoryTool;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.PartOfSpeech;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType.*;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.*;
import static java.lang.String.format;
import static java.util.Collections.reverse;
import static java.util.Collections.singletonList;

/**
 * @author sali
 */
public class GraphBuilder {

    private static GraphBuilder instance = new GraphBuilder();
    private final String NOUN_EMPTY_TOKEN_DISPLAY_NAME = "0:1:1";
    private MorphologicalAnalysisRepositoryUtil repositoryUtil = RepositoryTool.getInstance().getRepositoryUtil();
    private double tokenWidth = RECTANGLE_WIDTH;
    private double tokenHeight = RECTANGLE_HEIGHT;
    private double gapBetweenTokens = GAP_BETWEEN_TOKENS;
    private double rectX;
    private double rectY;
    private double textX;
    private double textY;
    private double x1;
    private double y1;
    private double x2;
    private double y2;
    private double x3;
    private double y3;

    private GraphBuilder() {
        // initial values
        reset();
    }

    public static GraphBuilder getInstance() {
        return instance;
    }

    /**
     * Build terminal nodes from given <code>tokens</code>. This method will be called when the
     * {@link com.alphasystem.morphologicalanalysis.graph.model.DependencyGraph} was being created for the first time.
     *
     * @param tokens
     * @return
     * @throws IllegalArgumentException
     */
    public List<TerminalNode> buildTerminalNodes(List<Token> tokens) throws IllegalArgumentException {
        if (tokens == null || tokens.isEmpty()) {
            throw new IllegalArgumentException("tokens cannot be null or empty");
        }
        List<TerminalNode> terminalNodes = new ArrayList<>(tokens.size());

        reverse(tokens);
        // build terminal nodes first
        terminalNodes.addAll(tokens.stream().map(token ->
                buildTerminalNode(token, TERMINAL)).collect(Collectors.toList()));

        // populate part of speeches
        buildPartOfSpeechNodes(terminalNodes);

        return terminalNodes;
    }

    /**
     * @param token
     * @param nodeType
     * @return
     * @throws IllegalArgumentException
     */
    public TerminalNode buildTerminalNode(Token token, GraphNodeType nodeType)
            throws IllegalArgumentException {
        if (token == null) {
            throw new IllegalArgumentException("Token cannot be null");
        }
        TerminalNode terminalNode;
        switch (nodeType) {
            case TERMINAL:
                terminalNode = new TerminalNode(token);
                break;
            case EMPTY:
                terminalNode = new EmptyNode(token);
                break;
            case HIDDEN:
                terminalNode = new HiddenNode(token);
                break;
            case REFERENCE:
                terminalNode = new ReferenceNode(token);
                break;
            default:
                throw new IllegalArgumentException(format("Invalid node type {%s} for token {%s}", nodeType,
                        token.getDisplayName()));
        }
        Long count = repositoryUtil.getRepository(terminalNode.getGraphNodeType())
                .countByChapterNumberAndVerseNumberAndTokenNumber(token.getChapterNumber(),
                        token.getVerseNumber(), token.getTokenNumber());
        count = (count == null) ? 0L : count;
        terminalNode.setVersion(count.intValue());
        terminalNode.setX(textX);
        terminalNode.setY(textY);
        terminalNode.setX1(x1);
        terminalNode.setY1(y1);
        terminalNode.setX2(x2);
        terminalNode.setY2(y2);
        terminalNode.setTranslationX(x3);
        terminalNode.setTranslationY(y3);

        // update counters
        rectX = x2 + gapBetweenTokens;
        textX = rectX + 30;
        x1 = rectX;
        x2 = tokenWidth + rectX;
        x3 = rectX + 30;

        return terminalNode;
    }

    public void buildPartOfSpeechNodes(List<TerminalNode> terminalNodes) {
        reset();
        textY = 160;

        for (TerminalNode terminalNode : terminalNodes) {
            List<PartOfSpeechNode> partOfSpeechNodes = buildPartOfSpeechNodes(terminalNode);
            terminalNode.setPartOfSpeechNodes(partOfSpeechNodes);
        }
    }

    public List<PartOfSpeechNode> buildPartOfSpeechNodes(TerminalNode terminalNode) {
        List<PartOfSpeechNode> partOfSpeechNodes = new ArrayList<>();

        Token token = terminalNode.getToken();
        List<Location> locations = token.getLocations();
        reverse(locations);
        Double posX = terminalNode.getX1();
        for (Location location : locations) {
            partOfSpeechNodes.add(buildPartOfSpeechNode(location, posX));
            posX += 50;
        }

        return partOfSpeechNodes;
    }

    public EmptyNode buildEmptyNode(PartOfSpeech partOfSpeech, Line referenceLine) {
        EmptyNode emptyNode = null;

        rectX = gapBetweenTokens + referenceLine.getEndX();
        textX = rectX + 30;
        x1 = rectX;
        x2 = tokenWidth + rectX;
        x3 = rectX + 30;

        Token token = repositoryUtil.getTokenRepository().findByDisplayName(NOUN_EMPTY_TOKEN_DISPLAY_NAME);
        emptyNode = (EmptyNode) buildTerminalNode(token, EMPTY);
        buildPartOfSpeechNodes(singletonList(emptyNode));

        return emptyNode;
    }

    public void buildRelationshipNode(RelationshipNode relationshipNode,
                                      LinkSupportAdapter dependentNode, LinkSupportAdapter ownerNode) {
        LinkSupport dependent = (LinkSupport) dependentNode.getSrc();
        relationshipNode.setDependent(dependent);
        LinkSupport owner = (LinkSupport) ownerNode.getSrc();
        relationshipNode.setOwner(owner);
        double startX = dependent.getCx() + dependent.getTranslateX();
        double startY = dependent.getCy() + dependent.getTranslateY();
        double endX = owner.getCx() + owner.getTranslateX();
        double endY = owner.getCy() + owner.getTranslateY();
        double controlY1 = startY + 100d;
        double controlY2 = endY + 100d;
        relationshipNode.setControlX1(startX);
        relationshipNode.setControlY1(controlY1);
        relationshipNode.setControlX2(endX);
        relationshipNode.setControlY2(controlY2);
        relationshipNode.setX((startX + endX) / 2);
        relationshipNode.setY((controlY1 + controlY2) / 2);
        relationshipNode.setT1(0.50);
        relationshipNode.setT2(0.55);
    }

    public void buildPhraseNode(PhraseNode phraseNode, List<PartOfSpeechNodeAdapter> fragments) {
        double yOffset = 100.0;
        PartOfSpeechNodeAdapter firstNode = fragments.get(fragments.size() - 1);
        PartOfSpeechNodeAdapter lastNode = fragments.get(0);
        TerminalNodeAdapter firstNodeParent = (TerminalNodeAdapter) firstNode.getParent();
        TerminalNodeAdapter lastNodeParent = (TerminalNodeAdapter) lastNode.getParent();

        Double x1 = firstNodeParent.getX1() + firstNodeParent.getTranslateX();
        Double y1 = firstNodeParent.getY1() + yOffset + firstNodeParent.getTranslateY();
        Double x2 = lastNodeParent.getX2() + lastNodeParent.getTranslateX();
        Double y2 = lastNodeParent.getY2() + yOffset + lastNodeParent.getTranslateY();
        Double x = (x1 + x2) / 2;
        Double y = ((y1 + y2) / 2) + 15;
        Double cx = x + 15;
        Double cy = y + 15;

        phraseNode.setX(x);
        phraseNode.setY(y);
        phraseNode.setX1(x1);
        phraseNode.setY1(y1);
        phraseNode.setX2(x2);
        phraseNode.setY2(y2);
        phraseNode.setCx(cx);
        phraseNode.setCy(cy);
        fragments.forEach(psna -> phraseNode.getFragments().add(psna.getSrc()));
    }

    public ReferenceNode buildReferenceNode(Token token, Line referenceLine) {
        rectX = gapBetweenTokens + referenceLine.getEndX();
        textX = rectX + 30;
        x1 = rectX;
        x2 = tokenWidth + rectX;
        x3 = rectX + 30;

        ReferenceNode referenceNode = (ReferenceNode) buildTerminalNode(token, REFERENCE);

        reset();
        textY = 160;
        referenceNode.setPartOfSpeechNodes(buildPartOfSpeechNodes(referenceNode));

        return referenceNode;
    }

    private PartOfSpeechNode buildPartOfSpeechNode(Location location, Double posX) {
        PartOfSpeechNode partOfSpeechNode = new PartOfSpeechNode(location);
        Long count = repositoryUtil.getPartOfSpeechNodeRepository()
                .countByChapterNumberAndVerseNumberAndTokenNumberAndLocationNumber(
                        location.getChapterNumber(), location.getVerseNumber(), location.getTokenNumber(),
                        location.getLocationNumber());
        count = (count == null) ? 0 : count;
        partOfSpeechNode.setVersion(count.intValue());
        partOfSpeechNode.setX(posX);
        partOfSpeechNode.setY(textY);
        double x = posX + 20;
        // x = x + (x % 20);
        partOfSpeechNode.setCx(x);
        x = textY + 20;
        // x = x + (x % 20);
        partOfSpeechNode.setCy(x);
        return partOfSpeechNode;
    }

    public void set(GraphMetaInfo metaInfo) {
        setTokenWidth(metaInfo.getTokenWidth());
        setTokenHeight(metaInfo.getTokenHeight());
        setGapBetweenTokens(metaInfo.getGapBetweenTokens());
        reset();
    }

    public double getTokenWidth() {
        return tokenWidth;
    }

    public void setTokenWidth(double tokenWidth) {
        this.tokenWidth = tokenWidth;
    }

    public double getTokenHeight() {
        return tokenHeight;
    }

    public void setTokenHeight(double tokenHeight) {
        this.tokenHeight = tokenHeight;
    }

    public double getGapBetweenTokens() {
        return gapBetweenTokens;
    }

    public void setGapBetweenTokens(double gapBetweenTokens) {
        this.gapBetweenTokens = gapBetweenTokens;
    }

    private void reset() {
        rectX = INITIAL_X;
        rectY = INITIAL_Y;
        textX = rectX + 10;
        textY = 105;
        x1 = rectX;
        y1 = tokenHeight + rectY;
        x2 = tokenWidth + rectX;
        y2 = y1;
        x3 = rectX + 30;
        y3 = 50;
    }
}
