package com.alphasystem.morphologicalanalysis.ui.dependencygraph.util;

import com.alphasystem.arabic.model.ArabicLetterType;
import com.alphasystem.arabic.model.ArabicWord;
import com.alphasystem.morphologicalanalysis.common.model.Linkable;
import com.alphasystem.morphologicalanalysis.graph.model.*;
import com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.*;
import com.alphasystem.morphologicalanalysis.util.RepositoryTool;
import com.alphasystem.morphologicalanalysis.wordbyword.model.*;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.*;
import com.alphasystem.morphologicalanalysis.wordbyword.repository.LocationRepository;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.List;
import java.util.Optional;

import static com.alphasystem.arabic.model.ArabicWord.getSubWord;
import static com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType.ROOT;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.*;
import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.IncompleteVerbCategory.KANA_AND_ITS_SISTERS;
import static com.alphasystem.util.AppUtil.isGivenType;
import static java.lang.String.format;
import static javafx.scene.control.Alert.AlertType.INFORMATION;
import static javafx.scene.text.Font.font;

/**
 * @author sali
 */
public class CanvasUtil {

    private static final char LEFT_POINTING_DOUBLE_ANGLE_QUOTATION_MARK = '\u00AB';
    private static final char RIGHT_POINTING_DOUBLE_ANGLE_QUOTATION_MARK = '\u00BB';

    private static CanvasUtil instance = new CanvasUtil();

    private RepositoryTool repositoryTool = RepositoryTool.getInstance();

    private LocationRepository locationRepository = repositoryTool.getRepositoryUtil().getLocationRepository();

    private CanvasUtil() {
    }

    public static CanvasUtil getInstance() {
        return instance;
    }

    public static ArabicWord getLocationWord(Token token, Location location) {
        return getSubWord(token.getTokenWord(), location.getStartIndex(), location.getEndIndex());
    }

    public static int getIndex(TerminalNodeAdapter src, ObservableList<GraphNodeAdapter> graphNodes) {
        int index = -1;
        for (int i = 0; i < graphNodes.size(); i++) {
            GraphNodeAdapter node = graphNodes.get(i);
            if (node.getId().equals(src.getId())) {
                index = i;
                break;
            }
        }
        return index;
    }

    public static Line getReferenceLine(Group parent) {
        ObservableList<Node> children = parent.getChildren();
        Line line = null;
        for (Node child : children) {
            if (isGivenType(Line.class, child)) {
                line = (Line) child;
                break;
            }
        }
        if (line == null) {
            showAlert(INFORMATION, "No line found.", null);
            return null;
        }
        return line;
    }

    private static Optional<ButtonType> showAlert(Alert.AlertType type, String contentText, String headerText) {
        Alert alert = new Alert(type);
        if (headerText != null) {
            alert.setHeaderText(headerText);
        }
        alert.setContentText(contentText);
        return alert.showAndWait();
    }

    private static String getText(GraphNodeAdapter item) {
        return format("(%s)", item.getText());
    }

    public String getLocationText(final TerminalNodeAdapter terminalNode,
                                  final PartOfSpeechNodeAdapter partOfSpeechNodeAdapter) throws RuntimeException {
        if (terminalNode == null) {
            throw new RuntimeException(format("Null terminal node for {%s}", partOfSpeechNodeAdapter));
        }
        Location location = partOfSpeechNodeAdapter.getSrc().getLocation();
        Token token = terminalNode.getSrc().getToken();
        ArabicWord locationWord = getLocationWord(token, location);
        return format("%s", locationWord.toUnicode());
    }

    public String getNodeText(GraphNode graphNode) {
        String text = null;
        if (graphNode == null) {
            return null;
        }
        switch (graphNode.getGraphNodeType()) {
            case TERMINAL:
            case IMPLIED:
            case REFERENCE:
            case HIDDEN:
                text = getTerminalNodeText((TerminalNode) graphNode);
                break;
            case PART_OF_SPEECH:
                text = getPartOfSpeechNodeText((PartOfSpeechNode) graphNode);
                break;
            case PHRASE:
                text = getPhraseNodeText((PhraseNode) graphNode);
                break;
            case RELATIONSHIP:
                text = getRelationshipNodeText((RelationshipNode) graphNode);
                break;
            case ROOT:
                RootNode rootNode = (RootNode) graphNode;
                text = rootNode.getChildNodeType().name();
                break;
            default:
                break;
        }
        return text;
    }

    @SuppressWarnings({"unchecked"})
    public LinkSupportAdapter createLinkSupportAdapter(LinkSupport linkSupport) {
        LinkSupportAdapter linkSupportAdapter = null;
        if (isGivenType(PartOfSpeechNode.class, linkSupport)) {
            linkSupportAdapter = new PartOfSpeechNodeAdapter();
        } else if (isGivenType(PhraseNode.class, linkSupport)) {
            linkSupportAdapter = new PhraseNodeAdapter();
        }
        if (linkSupportAdapter == null) {
            throw new NullPointerException(format("Could not create \"LinkSupportAdapter\" for class \"%s\" for \"%s\"."
                    , linkSupport.getClass().getName(), linkSupport.getDisplayName()));
        }
        linkSupportAdapter.setSrc(linkSupport);
        return linkSupportAdapter;
    }

    public Font createFont(FontMetaInfo fontMetaInfo) {
        return font(fontMetaInfo.getFamily(), FontWeight.valueOf(fontMetaInfo.getWeight()),
                FontPosture.valueOf(fontMetaInfo.getPosture()), fontMetaInfo.getSize());
    }

    private String getTerminalNodeText(TerminalNode terminalNode) {
        Token token = terminalNode.getToken();
        return token == null ? "" : token.getTokenWord().toUnicode();
    }

    private String getPartOfSpeechNodeText(PartOfSpeechNode partOfSpeechNode) {
        Location location = partOfSpeechNode.getLocation();
        PartOfSpeech partOfSpeech = location.getPartOfSpeech();
        AbstractProperties properties = location.getProperties();
        StringBuilder builder = new StringBuilder(partOfSpeech.getLabel().toUnicode());
        switch (partOfSpeech) {
            case NOUN:
            case DEMONSTRATIVE_PRONOUN:
            case RELATIVE_PRONOUN:
            case LOCATION_ADVERB:
            case TIME_ADVERB:
            case PROPER_NOUN:
                NounProperties np = (NounProperties) properties;
                builder.append(SPACE_STR).append(np.getStatus().getLabel().toUnicode());
                break;
            case VERB:
                VerbProperties vp = (VerbProperties) properties;
                builder.append(SPACE_STR).append(vp.getVerbType().getLabel().toUnicode());
                final VerbMode mode = vp.getMode();
                if (mode != null) {
                    builder.append(SPACE_STR).append(mode.getLabel().toUnicode());
                }
                break;
            case PRONOUN:
                ProNounProperties pp = (ProNounProperties) properties;
                /*builder.append(SPACE).append(pp.getProNounType().getLabel().toUnicode()).append(NEW_LINE)
                        .append(SPACE).append(FI_MAHL.toUnicode()).append(SPACE)
                        .append(getFromNounStatus(pp.getStatus()).getLabel().toUnicode());*/
                builder.append(SPACE_STR).append("(").append(pp.getStatus().getLabel().toUnicode()).append(")");
                break;
        }
        return builder.toString();
    }

    private String getRelationshipNodeText(RelationshipNode rn) {
        RelationshipType type = rn.getRelationshipType();
        String text = type.getLabel().toUnicode();
        Linkable owner = rn.getOwner().getLinkable();
        Location ol = locationRepository.findByDisplayName(owner.getDisplayName());
        if (ol != null) {
            PartOfSpeech pos = ol.getPartOfSpeech();
            switch (pos) {
                case ACCUSSATIVE_PARTICLE:
                    text = format("%s %s %s %s", type.getLabel().toUnicode(),
                            LEFT_POINTING_DOUBLE_ANGLE_QUOTATION_MARK, ol.getLocationWord().toUnicode(),
                            RIGHT_POINTING_DOUBLE_ANGLE_QUOTATION_MARK);
                    break;
                case VERB:
                    VerbProperties vp = (VerbProperties) ol.getProperties();
                    IncompleteVerb incompleteVerb = vp.getIncompleteVerb();
                    if (incompleteVerb != null) {
                        IncompleteVerbCategory category = incompleteVerb.getCategory();
                        IncompleteVerbType incompleteVerbType = incompleteVerb.getType();
                        if (category.equals(KANA_AND_ITS_SISTERS)) {
                            text = format("%s %s %s %s", type.getLabel().toUnicode(),
                                    LEFT_POINTING_DOUBLE_ANGLE_QUOTATION_MARK, incompleteVerbType.getLabel().toUnicode(),
                                    RIGHT_POINTING_DOUBLE_ANGLE_QUOTATION_MARK);
                        }
                    }
                    break;
            }
        }

        AlternateStatus alternateStatus = rn.getAlternateStatus();
        if (alternateStatus != null) {
            text = format("%s %s %s", text, FI_MAHL.toUnicode(), alternateStatus.getLabel().toUnicode());
        }

        return text;
    }

    private String getPhraseNodeText(PhraseNode phraseNode) {
        StringBuilder builder = new StringBuilder("");
        List<RelationshipType> relationships = phraseNode.getRelationships();
        if (relationships != null && !relationships.isEmpty()) {
            builder.append(relationships.get(0).getLabel().toUnicode());
            for (int i = 1; i < relationships.size(); i++) {
                builder.append(SPACE_STR).append(ArabicLetterType.WAW.toUnicode()).append(SPACE_STR)
                        .append(relationships.get(i).getLabel().toUnicode());
            }
        }
        String text = builder.toString();
        AlternateStatus alternateStatus = phraseNode.getAlternateStatus();
        if (alternateStatus != null) {
            text = format("%s %s %s", text, FI_MAHL.toUnicode(), alternateStatus.getLabel().toUnicode());
        }
        return text;
    }

    public String getPhraseText(List<PartOfSpeechNodeAdapter> nodes) {
        if (nodes == null || nodes.isEmpty()) {
            throw new RuntimeException("Empty part of speech nodes while getting phrase text");
        }
        StringBuilder text = new StringBuilder();
        int tokenNumber = nodes.get(0).getSrc().getLocation().getTokenNumber();
        for (PartOfSpeechNodeAdapter node : nodes) {
            TerminalNodeAdapter parent = (TerminalNodeAdapter) node.getParent();
            int currentTokenNumber = node.getSrc().getLocation().getTokenNumber();
            if (tokenNumber != currentTokenNumber) {
                text.append(" ");
            }
            tokenNumber = currentTokenNumber;
            text.append(getLocationText(parent, node));
        }
        return text.toString();
    }

    public String getPhraseMenuItemText(PhraseNodeAdapter phraseNodeAdapter) {
        ObservableList<RelationshipType> relationships = phraseNodeAdapter.getRelationships();
        StringBuilder builder = new StringBuilder("");
        if (relationships != null && !relationships.isEmpty()) {
            builder.append(relationships.get(0).getLabel().toUnicode());
            for (int i = 1; i < relationships.size(); i++) {
                builder.append(SPACE_STR).append(ArabicLetterType.WAW.toUnicode()).append(SPACE_STR)
                        .append(relationships.get(i).getLabel().toUnicode());
            }
        }
        return format("%s (%s) ", getPhraseText(phraseNodeAdapter.getFragments()), builder.toString());
    }

    public String getRelationshipMenuItemText(final LinkSupportAdapter linkSupportAdapter) {
        String text = null;
        if (isGivenType(PartOfSpeechNodeAdapter.class, linkSupportAdapter)) {
            PartOfSpeechNodeAdapter partOfSpeechNodeAdapter = (PartOfSpeechNodeAdapter) linkSupportAdapter;
            TerminalNodeAdapter terminalNode = (TerminalNodeAdapter) partOfSpeechNodeAdapter.getParent();
            Location location = partOfSpeechNodeAdapter.getSrc().getLocation();
            Token token = terminalNode.getSrc().getToken();
            ArabicWord locationWord = getLocationWord(token, location);
            text = format("%s (%s)", locationWord.toUnicode(), location.getPartOfSpeech().getLabel().toUnicode());
        } else if (isGivenType(PhraseNodeAdapter.class, linkSupportAdapter)) {
            PhraseNodeAdapter phraseNodeAdapter = (PhraseNodeAdapter) linkSupportAdapter;
            text = getPhraseMenuItemText(phraseNodeAdapter);
        }
        return text;
    }

    public String getRelationshipText(RelationshipNodeAdapter rna) {
        StringBuilder builder = new StringBuilder();

        LinkSupportAdapter dependent = rna.getDependent();
        LinkSupportAdapter owner = rna.getOwner();
        if (isGivenType(PartOfSpeechNodeAdapter.class, dependent)) {
            PartOfSpeechNodeAdapter pna = (PartOfSpeechNodeAdapter) dependent;
            builder.append(getLocationText((TerminalNodeAdapter) pna.getParent(), pna));
        } else if (isGivenType(PhraseNodeAdapter.class, dependent)) {
            PhraseNodeAdapter pna = (PhraseNodeAdapter) dependent;
            builder.append(getPhraseText(pna.getFragments()));
        }
        builder.append(" -> ");

        if (isGivenType(PartOfSpeechNodeAdapter.class, owner)) {
            PartOfSpeechNodeAdapter pna = (PartOfSpeechNodeAdapter) owner;
            builder.append(getLocationText((TerminalNodeAdapter) pna.getParent(), pna));
        } else if (isGivenType(PhraseNodeAdapter.class, owner)) {
            PhraseNodeAdapter pna = (PhraseNodeAdapter) owner;
            builder.append(getPhraseText(pna.getFragments()));
        }

        builder.append(" ");
        return builder.toString();
    }

    public DependencyGraph createDependencyGraph(VerseTokenPairGroup group, GraphMetaInfoAdapter adapterInfo) {
        GraphMetaInfo gmi = adapterInfo.getGraphMetaInfo();
        DependencyGraph dependencyGraph = repositoryTool.createDependencyGraph(group, gmi);
        dependencyGraph.getNodes().forEach(graphNode -> updateFonts(graphNode, adapterInfo));
        return dependencyGraph;
    }

    public void updateFonts(GraphNode graphNode, GraphMetaInfoAdapter adapterInfo) {
        GraphNodeType graphNodeType = graphNode.getGraphNodeType();
        switch (graphNodeType) {
            case TERMINAL:
            case REFERENCE:
            case HIDDEN:
            case IMPLIED:
                TerminalNode terminalNode = (TerminalNode) graphNode;
                terminalNode.setFont(adapterInfo.getTerminalFont());
                terminalNode.setTranslationFont(adapterInfo.getTranslationFont());
                terminalNode.getPartOfSpeechNodes().forEach(partOfSpeechNode ->
                        partOfSpeechNode.setFont(adapterInfo.getPosFont()));
                break;
            default:
                graphNode.setFont(adapterInfo.getPosFont());
                break;
        }
    }

    /**
     * Shift nodes to the right. The calculation of shifting right is:
     * <div>
     * <code>{@link GraphMetaInfoAdapter#getGapBetweenTokens()} + {@link GraphMetaInfoAdapter#getTokenWidth()} + {@link GraphNodeAdapter#getTranslateX()}</code>
     * </div>
     *
     * @param index                  index of current node
     * @param dependencyGraphAdapter dependency graph
     */
    public void shiftRight(int index, DependencyGraphAdapter dependencyGraphAdapter) {
        shiftNodes(index, false, dependencyGraphAdapter);
    }

    /**
     * Shift nodes to the right. The calculation of shifting right is:
     * <div>
     * <code>{@link GraphNodeAdapter#getTranslateX()} - {@link GraphMetaInfoAdapter#getTokenWidth()}</code>
     * </div>
     *
     * @param index                  index of current node
     * @param dependencyGraphAdapter dependency graph
     */
    public void shiftLeft(int index, DependencyGraphAdapter dependencyGraphAdapter) {
        shiftNodes(index, true, dependencyGraphAdapter);
    }

    /**
     * @param index                  index of current node
     * @param left                   flag to shift nodes left or right, true if shift nodes left false otherwise
     * @param dependencyGraphAdapter dependency graph
     */
    private void shiftNodes(int index, boolean left, DependencyGraphAdapter dependencyGraphAdapter) {
        ObservableList<GraphNodeAdapter> graphNodes = dependencyGraphAdapter.getGraphNodes();
        GraphMetaInfoAdapter graphMetaInfo = dependencyGraphAdapter.getGraphMetaInfo();
        for (int i = index; i < graphNodes.size(); i++) {
            GraphNodeAdapter node = graphNodes.get(i);
            if (isTerminal(node)) {
                double shiftLeft = node.getTranslateX() - graphMetaInfo.getTokenWidth();
                double shiftRight = graphMetaInfo.getGapBetweenTokens() + graphMetaInfo.getTokenWidth() + node.getTranslateX();
                double translateX = left ? shiftLeft : shiftRight;
                node.setTranslateX(translateX);
                // update pos translateX
                TerminalNodeAdapter terminalNodeAdapter = (TerminalNodeAdapter) node;
                terminalNodeAdapter.getPartOfSpeeches().forEach(posAdapter -> posAdapter.setTranslateX(translateX));
            }
        }
    }

    private Text getItemGraphic(GraphNodeAdapter item) {
        Text label = new Text();
        StringBuilder builder = new StringBuilder();
        if (item != null) {
            GraphNodeType nodeType = item.getGraphNodeType();
            label.setFont(nodeType.equals(ROOT) ? ENGLISH_FONT_SMALL : ARABIC_FONT_SMALL);
            String text = item.getText();
            switch (nodeType) {
                case PART_OF_SPEECH:
                    PartOfSpeechNodeAdapter posna = (PartOfSpeechNodeAdapter) item;
                    TerminalNodeAdapter parent = (TerminalNodeAdapter) posna.getParent();
                    builder.append(getLocationText(parent, posna)).append(" ").append(getText(item));
                    break;
                case PHRASE:
                    PhraseNodeAdapter pna = (PhraseNodeAdapter) item;
                    builder.append(getPhraseText(pna.getFragments())).append(" ").append(getText(item));
                    break;
                case RELATIONSHIP:
                    RelationshipNodeAdapter rna = (RelationshipNodeAdapter) item;
                    builder.append(getRelationshipText(rna)).append(getText(item));
                    break;
                default:
                    builder.append(text);
                    break;
            }
        }
        label.setText(builder.toString());
        return label;
    }

    public MenuItem createMenuItem(GraphNodeAdapter graphNode) {
        MenuItem menuItem = isTerminal(graphNode) ? new Menu() : new MenuItem();
        menuItem.setGraphic(getItemGraphic(graphNode));
        return menuItem;
    }
}
