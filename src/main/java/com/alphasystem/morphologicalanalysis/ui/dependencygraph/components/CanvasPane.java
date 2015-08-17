package com.alphasystem.morphologicalanalysis.ui.dependencygraph.components;

import com.alphasystem.morphologicalanalysis.graph.model.DependencyGraph;
import com.alphasystem.morphologicalanalysis.graph.model.Fragment;
import com.alphasystem.morphologicalanalysis.graph.model.Relationship;
import com.alphasystem.morphologicalanalysis.graph.model.Terminal;
import com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType;
import com.alphasystem.morphologicalanalysis.graph.repository.RelationshipRepository;
import com.alphasystem.morphologicalanalysis.graph.repository.TerminalRepository;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.*;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.DependencyGraphGraphicTool;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.GraphBuilder;
import com.alphasystem.morphologicalanalysis.util.RepositoryTool;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import com.alphasystem.morphologicalanalysis.wordbyword.model.VerbProperties;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.PartOfSpeech;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType.REFERENCE;
import static com.alphasystem.morphologicalanalysis.graph.model.support.TerminalType.EMPTY;
import static com.alphasystem.morphologicalanalysis.graph.model.support.TerminalType.HIDDEN;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.ARABIC_FONT_SMALL_BOLD;
import static com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.DependencyGraphGraphicTool.DARK_GRAY_CLOUD;
import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.PartOfSpeech.NOUN;
import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.PartOfSpeech.VERB;
import static com.alphasystem.util.AppUtil.isGivenType;
import static java.lang.String.format;
import static java.util.Collections.reverse;
import static javafx.scene.control.Alert.AlertType.INFORMATION;
import static javafx.scene.paint.Color.*;
import static javafx.scene.text.TextAlignment.CENTER;
import static javafx.scene.text.TextAlignment.RIGHT;

/**
 * @author sali
 */
public class CanvasPane extends Pane {

    public static final String TERMINAL_GROUP_ID_PREFIX = "terminal";
    private static final double RADIUS = 2.0;
    private final ObjectProperty<CanvasData> canvasDataObject;
    private final ContextMenu terminalContextMenu;
    private final ContextMenu partOfSpeechContextMenu;
    private final ContextMenu relationshipContextMenu;
    private RepositoryTool repositoryTool = RepositoryTool.getInstance();
    private TerminalRepository terminalRepository = repositoryTool.getRepositoryUtil().getTerminalRepository();
    private RelationshipRepository relationshipRepository = repositoryTool
            .getRepositoryUtil().getRelationshipRepository();
    private RelationshipSelectionDialog relationshipSelectionDialog;
    private PhraseSelectionDialog phraseSelectionDialog;
    private ReferenceNodeSelectionDialog referenceNodeSelectionDialog;
    private DependencyGraphGraphicTool tool = DependencyGraphGraphicTool.getInstance();
    private GraphBuilder graphBuilder = GraphBuilder.getInstance();
    private Pane canvasPane;
    private LinkSupport dependentLinkNode;
    private LinkSupport ownerLinkNode;
    private TerminalNode firstTerminalNode;
    private TerminalNode lastTerminalNode;
    private Node gridLines;

    public CanvasPane(CanvasData data) {
        this.canvasDataObject = new SimpleObjectProperty<>(data);

        CanvasMetaData metaData = canvasDataObjectProperty().get().getCanvasMetaData();
        int width = metaData.getWidth();
        int height = metaData.getHeight();
        terminalContextMenu = new ContextMenu();
        partOfSpeechContextMenu = new ContextMenu();
        relationshipContextMenu = new ContextMenu();
        relationshipSelectionDialog = new RelationshipSelectionDialog();
        phraseSelectionDialog = new PhraseSelectionDialog();
        referenceNodeSelectionDialog = new ReferenceNodeSelectionDialog();
        initListeners();

        canvasPane = new Pane();
        initCanvas();

        canvasPane.setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        canvasPane.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        canvasPane.setPrefSize(width, height);
        canvasPane.setBackground(new Background(new BackgroundFill(BEIGE, null, null)));

        getChildren().add(canvasPane);

        setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        setPrefSize(width + 200, height + 200);
    }

    private List<GraphNode> createListOfNodes(boolean reverse) {
        List<GraphNode> nodes = new ArrayList<>(canvasDataObjectProperty().get().getNodes());
        if (reverse) {
            reverse(nodes);
        }
        return nodes;
    }

    private void initTerminalContextMenu(Text source) {
        TerminalNode tn = (TerminalNode) source.getUserData();
        Token token = tn.getToken();

        final ObservableList<MenuItem> items = terminalContextMenu.getItems();
        items.remove(0, items.size());

        Menu menu = new Menu("Make Phrase");
        List<GraphNode> nodes = createListOfNodes(true);
        nodes.forEach(graphNode -> {
            GraphNodeType nodeType = graphNode.getNodeType();
            switch (nodeType) {
                case TERMINAL:
                case EMPTY:
                case HIDDEN:
                    TerminalNode _tn = (TerminalNode) graphNode;
                    menu.getItems().add(createPhraseMenuItem(_tn));
                    break;
            }
        });
        items.add(menu);

        Menu addEmptyNodeMenu = new Menu("Add Empty Node");

        addEmptyNodeMenu.getItems().add(createAddEmptyNodeMenuItem(source, token, NOUN));
        addEmptyNodeMenu.getItems().add(createAddEmptyNodeMenuItem(source, token, VERB));
        items.add(addEmptyNodeMenu);

        MenuItem menuItem = new MenuItem("Add Reference Node ...");
        menuItem.setOnAction(event -> {
            DependencyGraph dependencyGraph = canvasDataObject.get().getDependencyGraph();
            referenceNodeSelectionDialog.setChapter(dependencyGraph.getChapterNumber());
            referenceNodeSelectionDialog.setVerse(dependencyGraph.getVerseNumber());
            Optional<Terminal> result = referenceNodeSelectionDialog.showAndWait();
            result.ifPresent(terminal -> addReferenceNode(source, terminal));
        });

        items.add(menuItem);

        // add "Hidden" pronoun if applicable (only for verb)
        token.getLocations().forEach(location -> {
            PartOfSpeech partOfSpeech = location.getPartOfSpeech();
            if (partOfSpeech.equals(VERB)) {
                MenuItem mi = new MenuItem("Add Hidden Node");
                mi.setOnAction(event -> {
                    VerbProperties vp = (VerbProperties) location.getProperties();
                    String id = format("%s_%s_%s", vp.getConversationType().name(), vp.getGender().name(),
                            vp.getNumber().name());
                    addHiddenNode(getReferenceLine(source), token, id);
                });
                items.add(mi);
            }
        });
    }

    private void shiftNodes(Token token){
        List<Token> tokens = getTokens(canvasDataObjectProperty().get().getDependencyGraph().getTerminals());
        int index = tokens.indexOf(token);
        int reverseIndex = canvasDataObjectProperty().get().getNodes().size() - 1 - index;
        graphBuilder.set(canvasDataObjectProperty().get().getCanvasMetaData());
        graphBuilder.shiftNodes(canvasDataObjectProperty().get().getNodes(), reverseIndex);
    }

    private void initPartOfSpeechContextMenu(String currentNodeId) {
        ObservableList<MenuItem> items = partOfSpeechContextMenu.getItems();
        items.remove(0, items.size());

        Menu menu = new Menu("Make Relationship");
        ObservableList<GraphNode> nodes = canvasDataObject.get().getNodes();
        nodes.forEach(graphNode -> {
            GraphNodeType nodeType = graphNode.getNodeType();
            switch (nodeType) {
                case TERMINAL:
                case EMPTY:
                case REFERENCE:
                case HIDDEN:
                    TerminalNode tn = (TerminalNode) graphNode;
                    ObservableList<PartOfSpeechNode> partOfSpeeches = tn.getPartOfSpeeches();
                    for (PartOfSpeechNode partOfSpeech : partOfSpeeches) {
                        if (currentNodeId.equals(partOfSpeech.getId())) {
                            continue;
                        }
                        menu.getItems().add(createRelationshipMenuItem(tn, partOfSpeech));
                    }
                    break;
                case PHRASE:
                    PhraseNode pn = (PhraseNode) graphNode;
                    if (!currentNodeId.equals(pn.getId())) {
                        menu.getItems().add(createRelationshipMenuItem(null, pn));
                    }
                    break;
            }
        });
        items.add(menu);
    }

    private void initRelationshipContextMenu(Text text) {
        ObservableList<MenuItem> items = relationshipContextMenu.getItems();
        items.remove(0, items.size());

        MenuItem menuItem = new MenuItem("Remove");
        menuItem.setUserData(text);
        menuItem.setOnAction(event -> {
            MenuItem source = (MenuItem) event.getSource();
            Text selectedText = (Text) source.getUserData();
            RelationshipNode relationshipNode = (RelationshipNode) selectedText.getUserData();
            removeRelationship(relationshipNode);
        });
        items.add(menuItem);
    }

    private void removeRelationship(RelationshipNode relationshipNode) {
        Relationship relationship = relationshipNode.getRelationship();
        CanvasData canvasData = canvasDataObject.get();

        canvasData.getNodes().remove(relationshipNode);
        boolean removed = canvasData.getDependencyGraph().getRelationships().remove(relationship);
        if (removed) {
            relationshipRepository.delete(relationship.getId());
        }

        canvasDataObject.setValue(null);
        canvasDataObject.setValue(canvasData);
    }

    private MenuItem createPhraseMenuItem(TerminalNode terminalNode) {
        Text text = new Text(terminalNode.getText());
        text.setFont(ARABIC_FONT_SMALL_BOLD);
        MenuItem menuItem = new MenuItem("", text);
        menuItem.setUserData(terminalNode);
        menuItem.setOnAction(event -> {
            MenuItem source = (MenuItem) event.getSource();
            phraseSelectionDialog.reset();
            lastTerminalNode = (TerminalNode) source.getUserData();
            Token firstToken = firstTerminalNode.getToken();
            Token lastToken = lastTerminalNode.getToken();
            Integer firstTokenTokenNumber = firstToken.getTokenNumber();
            Integer lastTokenTokenNumber = lastToken.getTokenNumber();
            if (firstTokenTokenNumber > lastTokenTokenNumber) {
                // in UI we have laid out nodes from left to right but Arabic is read right to left.
                // if we have selected from left, then switch the nodes to make it right to left
                TerminalNode tmp = firstTerminalNode;
                firstTerminalNode = lastTerminalNode;
                lastTerminalNode = tmp;
                tmp = null;
                firstTokenTokenNumber = firstTerminalNode.getToken().getTokenNumber();
                lastTokenTokenNumber = lastTerminalNode.getToken().getTokenNumber();
            } else if (firstTokenTokenNumber.equals(lastTokenTokenNumber)) {
                //lastTokenTokenNumber--;
            }
            lastTokenTokenNumber++;
            // populate the list of nodes
            List<TerminalNode> terminalNodes = new ArrayList<>();
            List<GraphNode> nodes = createListOfNodes(true);
            Iterator<GraphNode> iterator = nodes.iterator();
            loop:
            while (iterator.hasNext()) {
                GraphNode gn = iterator.next();
                GraphNodeType nodeType = gn.getNodeType();
                switch (nodeType) {
                    case TERMINAL:
                    case EMPTY:
                    case HIDDEN:
                        TerminalNode tn = (TerminalNode) gn;
                        Integer tokenNumber = tn.getToken().getTokenNumber();
                        if (tokenNumber < firstTokenTokenNumber) {
                            continue loop;
                        }
                        if (tokenNumber >= lastTokenTokenNumber) {
                            break loop;
                        }
                        terminalNodes.add(tn);
                        break;
                }

            }

            phraseSelectionDialog.setNodes(terminalNodes);
            Optional<Fragment> result = phraseSelectionDialog.showAndWait();
            result.ifPresent(fragment -> addPhrase(fragment, terminalNodes));
        });
        return menuItem;
    }

    private MenuItem createRelationshipMenuItem(TerminalNode terminalNode, LinkSupport ownerNode) {
        String tnText = terminalNode == null ? "" : format("%s - ", terminalNode.getText());
        String value = format("%s%s", tnText, ownerNode.getText());
        Text text = new Text(value);
        text.setFont(ARABIC_FONT_SMALL_BOLD);
        MenuItem menuItem = new MenuItem("", text);
        menuItem.setUserData(ownerNode);
        menuItem.setOnAction(event -> {
            MenuItem source = (MenuItem) event.getSource();
            relationshipSelectionDialog.reset();
            relationshipSelectionDialog.setDependentNode(dependentLinkNode);
            ownerLinkNode = (LinkSupport) source.getUserData();
            relationshipSelectionDialog.setOwnerNode(ownerLinkNode);
            Optional<Relationship> result = relationshipSelectionDialog.showAndWait();
            result.ifPresent(this::addRelationship);
        });
        return menuItem;
    }

    private MenuItem createAddEmptyNodeMenuItem(Text selectedText, Token token, PartOfSpeech partOfSpeech) {
        Text text = new Text(partOfSpeech.getLabel().toUnicode());
        text.setFont(ARABIC_FONT_SMALL_BOLD);
        MenuItem menuItem = new MenuItem("", text);
        menuItem.setUserData(partOfSpeech);
        menuItem.setOnAction(event -> {
            MenuItem source = (MenuItem) event.getSource();
            PartOfSpeech pos = (PartOfSpeech) source.getUserData();
            addEmptyNode(selectedText, token, pos);
        });
        return menuItem;
    }

    private Line getReferenceLine(Text selectedText) {
        Group parent = (Group) selectedText.getParent();
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

    private void addReferenceNode(Text selectedText, Terminal terminal) {
        Line line = getReferenceLine(selectedText);
        if (line != null) {
            addReferenceNode(line, terminal);
        }
    }

    private void addEmptyNode(Text selectedText, Token token, PartOfSpeech partOfSpeech) {
        Line line = getReferenceLine(selectedText);
        if (line != null) {
            addEmptyNode(line, token, partOfSpeech);
        }
    }

    private void initListeners() {
        CanvasMetaData metaData = canvasDataObject.get().getCanvasMetaData();
        metaData.widthProperty().addListener((observable, oldValue, newValue) -> {
            resizeCanvas();
        });
        metaData.heightProperty().addListener((observable, oldValue, newValue) -> {
            resizeCanvas();
        });
        metaData.tokenWidthProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("tokenWidthProperty: " + newValue);
        });
        metaData.tokenHeightProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("tokenHeightProperty: " + newValue);
        });
        metaData.gapBetweenTokensProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("gapBetweenTokensProperty: " + newValue);
        });
        metaData.showGridLinesProperty().addListener((observable, oldValue, newValue) -> {
            toggleGridLines(newValue, metaData.isShowOutLines());
        });
        metaData.showOutLinesProperty().addListener((observable, oldValue, newValue) -> {
            toggleGridLines(metaData.isShowGridLines(), newValue);
        });
        metaData.debugModeProperty().addListener((observable, oldValue, newValue) -> {
            initCanvas();
        });
        canvasDataObjectProperty().addListener((observable, oldValue, newData) -> {
            if (newData != null) {
                ObservableList<GraphNode> nodes = newData.getNodes();
                if (nodes != null && !nodes.isEmpty()) {
                    drawNodes(nodes, false);
                }
            }
        });
    }

    public final ObjectProperty<CanvasData> canvasDataObjectProperty() {
        return canvasDataObject;
    }

    private void toggleGridLines(boolean showGridLines, boolean showOutline) {
        canvasPane.getChildren().remove(gridLines);
        gridLines = null;
        if (showOutline || showGridLines) {
            CanvasMetaData metaData = canvasDataObject.get().getCanvasMetaData();
            int width = metaData.getWidth();
            int height = metaData.getHeight();
            gridLines = tool.drawGridLines(showGridLines, width, height);
            canvasPane.getChildren().add(gridLines);
        }
    }

    private void resizeCanvas() {
        CanvasMetaData metaData = canvasDataObject.get().getCanvasMetaData();

        int width = metaData.getWidth();
        int height = metaData.getHeight();
        canvasPane.setPrefSize(width, height);

        setPrefSize(width + 200, height + 200);

        toggleGridLines(metaData.isShowGridLines(), metaData.isShowOutLines());
        requestLayout();
    }

    private void initCanvas() {
        CanvasMetaData metaData = canvasDataObject.get().getCanvasMetaData();
        int size = canvasPane.getChildren().size();
        canvasPane.getChildren().remove(0, size);
        boolean showOutline = metaData.isShowOutLines();
        boolean showGridLines = metaData.isShowGridLines();
        int width = metaData.getWidth();
        int height = metaData.getHeight();
        canvasPane.setPrefSize(width, height);

        ObservableList<GraphNode> nodes = canvasDataObject.get().getNodes();
        if (nodes != null && !nodes.isEmpty()) {
            drawNodes(nodes, true);
        }

        if (showOutline || showGridLines) {
            gridLines = tool.drawGridLines(showGridLines, width, height);
            canvasPane.getChildren().add(gridLines);
        }

        setPrefSize(width + 200, height + 200);
        requestLayout();
    }

    private void removeAll(boolean removeGridLines) {
        ObservableList<Node> children = canvasPane.getChildren();
        Iterator<Node> iterator = children.iterator();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            String id = node.getId();
            if ("gridLines".equals(id) && !removeGridLines) {
                continue;
            }
            iterator.remove();
        }

    }

    private void drawNodes(ObservableList<GraphNode> nodes, boolean removeGridLines) {
        removeAll(removeGridLines);
        for (GraphNode node : nodes) {
            GraphNodeType nodeType = node.getNodeType();
            switch (nodeType) {
                case TERMINAL:
                case EMPTY:
                case REFERENCE:
                case HIDDEN:
                    buildTerminalNode((TerminalNode) node);
                    break;
                case RELATIONSHIP:
                    buildRelationshipNode((RelationshipNode) node);
                    break;
                case PHRASE:
                    buildPhraseNode((PhraseNode) node);
                    break;
                default:
                    break;
            }
        }
    }

    private List<Token> getTokens(List<Terminal> terminals) {
        List<Token> tokens = new ArrayList<>();
        terminals.forEach(terminal -> tokens.add(terminal.getToken()));
        return tokens;
    }

    private void addEmptyNode(Line referenceLine, Token token, PartOfSpeech partOfSpeech) {
        shiftNodes(token);
        CanvasData canvasData = canvasDataObject.get();
        graphBuilder.set(canvasData.getCanvasMetaData());
        // Step 1: call GraphBuilder to create an empty node
        EmptyNode emptyNode = graphBuilder.buildEmptyNode(referenceLine, partOfSpeech);

        // add this node into existing list of nodes and
        // update canvasDataObject for changes to take effect
        List<Token> tokens = getTokens(canvasData.getDependencyGraph().getTerminals());
        int index = tokens.indexOf(firstTerminalNode.getToken());
        Terminal terminal = new Terminal(emptyNode.getToken(), EMPTY);
        Terminal result = terminalRepository.findByDisplayName(terminal.getDisplayName());
        if (result == null) {
            result = terminal;
        }
        canvasData.getDependencyGraph().getTerminals().add(index, result);
        index = (canvasData.getNodes().size() - 1) - (index - 1);
        canvasData.getNodes().add(index, emptyNode);
        canvasDataObject.setValue(null);
        canvasDataObject.setValue(canvasData);
    }

    private void addReferenceNode(Line referenceLine, Terminal terminal) {
        CanvasData canvasData = canvasDataObject.get();
        graphBuilder.set(canvasData.getCanvasMetaData());
        // Step 1: call GraphBuilder to create an reference node
        ReferenceNode referenceNode = graphBuilder.buildReferenceNode(referenceLine, terminal.getToken());

        // add this node into existing list of nodes and
        // update canvasDataObject for changes to take effect
        List<Token> tokens = getTokens(canvasData.getDependencyGraph().getTerminals());
        int index = tokens.indexOf(firstTerminalNode.getToken());
        canvasData.getDependencyGraph().getTerminals().add(index, terminal);
        canvasData.getNodes().add(index, referenceNode);
        canvasDataObject.setValue(null);
        canvasDataObject.setValue(canvasData);
    }

    private void addRelationship(Relationship relationship) {
        CanvasData canvasData = canvasDataObject.get();
        final CanvasMetaData canvasMetaData = canvasData.getCanvasMetaData();
        graphBuilder.set(canvasMetaData);
        // Step 1: call GraphBuilder to create a relationship node
        RelationshipNode relationshipNode = graphBuilder.buildRelationshipNode(relationship,
                dependentLinkNode, ownerLinkNode);

        // add this node into existing list of nodes and
        // update canvasDataObject for changes to take effect
        canvasData.getDependencyGraph().getRelationships().add(relationship);
        canvasData.getNodes().add(relationshipNode);
        canvasDataObject.setValue(null);
        canvasDataObject.setValue(canvasData);
    }

    private void addPhrase(Fragment fragment, List<TerminalNode> nodes) {
        firstTerminalNode = null;
        lastTerminalNode = null;
        CanvasData canvasData = canvasDataObject.get();
        graphBuilder.set(canvasData.getCanvasMetaData());
        // Step 1: call GraphBuilder to create a phrase node
        PhraseNode phraseNode = graphBuilder.buildPhraseNode(fragment, nodes);

        // add this node into existing list of nodes and
        // update canvasDataObject for changes to take effect
        canvasData.getDependencyGraph().getFragments().add(fragment);
        canvasData.getNodes().add(phraseNode);
        canvasDataObject.setValue(null);
        canvasDataObject.setValue(canvasData);
    }

    private void addHiddenNode(Line referenceLine, Token referenceTerminal, String pronounId) {
        shiftNodes(referenceTerminal);
        CanvasData canvasData = canvasDataObject.get();
        graphBuilder.set(canvasData.getCanvasMetaData());
        // Step 1: call GraphBuilder to create an hidden node
        HiddenNode hiddenNode = graphBuilder.buildHiddenNode(referenceLine, pronounId);

        // add this node into existing list of nodes and
        // update canvasDataObject for changes to take effect
        List<Token> tokens = getTokens(canvasData.getDependencyGraph().getTerminals());
        int index = tokens.indexOf(referenceTerminal);
        index++;

        Token token = hiddenNode.getToken();
        Terminal terminal = new Terminal(token, HIDDEN);
        Terminal result = terminalRepository.findByDisplayName(terminal.getDisplayName());
        if (result == null) {
            terminal = terminalRepository.save(terminal);
        } else {
            terminal = result;
        }
        canvasData.getDependencyGraph().getTerminals().add(index, terminal);

        index = (canvasData.getNodes().size() - 1) - (index - 1);
        canvasData.getNodes().add(index, hiddenNode);
        canvasDataObject.setValue(null);
        canvasDataObject.setValue(canvasData);
    }

    private Line drawLine(LineSupport node) {
        Line line = tool.drawLine(node.getId(), node.getX1(), node.getY1(), node.getX2(), node.getY2(),
                DARK_GRAY_CLOUD, 0.5);

        // bind line co-ordinates
        line.startXProperty().bind(node.x1Property());
        line.startYProperty().bind(node.y1Property());
        line.endXProperty().bind(node.x2Property());
        line.endYProperty().bind(node.y2Property());

        return line;
    }

    private Text drawText(GraphNode node, Color color, Font font) {
        Text arabicText = tool.drawText(node.getId(), node.getText(), RIGHT, color,
                node.getX(), node.getY(), font);
        arabicText.setUserData(node);

        // bind text x and y locations
        arabicText.textProperty().bind(node.textProperty());
        arabicText.xProperty().bind(node.xProperty());
        arabicText.yProperty().bind(node.yProperty());

        return arabicText;
    }

    private void buildTerminalNode(TerminalNode tn) {
        Line line = drawLine(tn);

        Token token = tn.getToken();
        GraphNodeType nodeType = tn.getNodeType();
        boolean hiddenOrEmptyNode = nodeType.equals(REFERENCE) || token.isHidden();
        Color hiddenOrEmptyNodeColor = LIGHTGRAY.darker();
        Color color = hiddenOrEmptyNode ? hiddenOrEmptyNodeColor : BLACK;

        String tokenId;
        String trans;

        tokenId = token.getDisplayName();
        trans = token.getTranslation();
        List<Location> locations = token.getLocations();
        if (token.getLocationCount() == 1 && !hiddenOrEmptyNode) {
            Location location = locations.get(0);
            color = web(location.getPartOfSpeech().getColorCode());
        }

        final Text arabicText = drawText(tn, color, getTokenFont());
        arabicText.fontProperty().bind(canvasDataObjectProperty().get().getCanvasMetaData().tokenFontProperty());

        double translateX = tn.getTranslateX();
        double translateY = tn.getTranslateY();
        arabicText.setOnMouseClicked(event -> {
            Text source = (Text) event.getSource();
            if (event.isPopupTrigger()) {
                lastTerminalNode = null;
                firstTerminalNode = tn;
                initTerminalContextMenu(source);
                terminalContextMenu.show(source, event.getScreenX(), event.getScreenY());
            } else {
                // single click, populate editor with this node
                canvasDataObject.get().setSelectedNode(tn);
            }
        });

        String id = format("trans_%s", tn.getId());
        Color transColor = hiddenOrEmptyNode ? hiddenOrEmptyNodeColor : BLACK;
        Text englishText = tool.drawText(id, trans, CENTER, transColor,
                tn.getX3(), tn.getY3(), getTranslationFont());
        // bind text x and y locations and translation font
        englishText.xProperty().bind(tn.x3Property());
        englishText.yProperty().bind(tn.y3Property());
        englishText.fontProperty().bind(canvasDataObjectProperty().get().getCanvasMetaData().translationFontProperty());

        Group group = new Group();

        group.setId(format("%s_%s", TERMINAL_GROUP_ID_PREFIX, tokenId));
        group.getChildren().addAll(englishText, arabicText, line);

        ObservableList<PartOfSpeechNode> partOfSpeeches = tn.getPartOfSpeeches();
        for (PartOfSpeechNode pn : partOfSpeeches) {
            pn.setTranslateX(translateX);
            pn.setTranslateY(translateY);
            color = hiddenOrEmptyNode ? hiddenOrEmptyNodeColor : web(pn.getPartOfSpeech().getColorCode());
            Text posArabicText = drawText(pn, color, getPartOfSpeechFont());
            posArabicText.fontProperty().bind(canvasDataObjectProperty().get().getCanvasMetaData().partOfSpeechFontProperty());
            posArabicText.setOnMouseClicked(event -> {
                Text source = (Text) event.getSource();
                String thisId = pn.getId();
                if (event.isPopupTrigger()) {
                    ownerLinkNode = null;
                    dependentLinkNode = pn;
                    initPartOfSpeechContextMenu(thisId);
                    partOfSpeechContextMenu.show(source, event.getScreenX(), event.getScreenY());
                } else {
                    // single click, populate editor with this node
                    canvasDataObject.get().setSelectedNode(pn);
                }
            });

            id = format("c_%s", pn.getId());
            Circle circle = tool.drawCircle(id, color, pn.getCx(), pn.getCy(), RADIUS);
            // bind coordinates
            circle.centerXProperty().bind(pn.cxProperty());
            circle.centerYProperty().bind(pn.cyProperty());

            group.getChildren().addAll(posArabicText, circle);
        }

        group.setTranslateX(translateX);
        group.setTranslateY(translateY);

        tn.translateXProperty().addListener((observable, oldValue, newValue) -> {
            Double x = (Double) newValue;
            group.setTranslateX(x);
            tn.getPartOfSpeeches().forEach(partOfSpeechNode -> partOfSpeechNode.setTranslateX(x));
        });
        tn.translateYProperty().addListener((observable, oldValue, newValue) -> {
            Double y = (Double) newValue;
            group.setTranslateY(y);
            tn.getPartOfSpeeches().forEach(partOfSpeechNode -> partOfSpeechNode.setTranslateY(y));
        });

        canvasPane.getChildren().add(group);
    }

    private void buildRelationshipNode(RelationshipNode rn) {
        Color color = rn.getStroke();
        CubicCurve cubicCurve = tool.drawCubicCurve(rn.getId(), rn.getStartX(),
                rn.getStartY(), rn.getControlX1(), rn.getControlY1(), rn.getControlX2(),
                rn.getControlY2(), rn.getEndX(), rn.getEndY(),
                color);

        // bind line co-ordinates
        cubicCurve.startXProperty().bind(rn.startXProperty());
        cubicCurve.startYProperty().bind(rn.startYProperty());
        cubicCurve.controlX1Property().bind(rn.controlX1Property());
        cubicCurve.controlY1Property().bind(rn.controlY1Property());
        cubicCurve.controlX2Property().bind(rn.controlX2Property());
        cubicCurve.controlY2Property().bind(rn.controlY2Property());
        cubicCurve.endXProperty().bind(rn.endXProperty());
        cubicCurve.endYProperty().bind(rn.endYProperty());
        cubicCurve.strokeProperty().bind(rn.strokeProperty());

        Text arabicText = drawText(rn, color, getPartOfSpeechFont());
        arabicText.fontProperty().bind(canvasDataObjectProperty().get().getCanvasMetaData().partOfSpeechFontProperty());
        arabicText.fillProperty().bind(rn.strokeProperty());
        arabicText.setOnMouseClicked(event -> {
            Text source = (Text) event.getSource();
            String thisId = rn.getId();
            if (event.isPopupTrigger()) {
                initRelationshipContextMenu(source);
                relationshipContextMenu.show(source, event.getScreenX(), event.getScreenY());
            } else {
                // single click, populate editor with this node
                canvasDataObject.get().setSelectedNode(rn);
            }
        });

        // small arrow pointing towards the relationship direction
        Polyline triangle = tool.drawPolyline(rn.getCurvePointX(), rn.getCurvePointY(), rn.getArrowPointX1(),
                rn.getArrowPointY1(), rn.getArrowPointX2(), rn.getArrowPointY2(), color);
        // bind line co-ordinates
        //TODO: need to figure out how to bind polyline
        // commented following code as it causes application to crashed
//        rn.t1Property().addListener((observable, oldValue, newValue) -> {
//            runLater(this::initCanvas);
//        });
//        rn.t2Property().addListener((observable, oldValue, newValue) -> {
//            runLater(this::initCanvas);
//        });

        Group group = new Group();
        group.setId(format("rln_%s", rn.getRelationship().getDisplayName()));

        group.getChildren().addAll(cubicCurve, arabicText, triangle);

        if (canvasDataObject.get().getCanvasMetaData().isDebugMode()) {
            Path path = tool.drawCubicCurveBounds(rn.getStartX(), rn.getStartY(), rn.getControlX1(),
                    rn.getControlY1(), rn.getControlX2(), rn.getControlY2(), rn.getEndX(), rn.getEndY());
            group.getChildren().add(path);
        }
        canvasPane.getChildren().add(group);
    }

    private void buildPhraseNode(PhraseNode pn) {
        Color color = web(pn.getFrament().getRelationshipType().getColorCode());

        Line line = drawLine(pn);
        Text arabicText = drawText(pn, color, getPartOfSpeechFont());
        arabicText.fontProperty().bind(canvasDataObjectProperty().get().getCanvasMetaData().partOfSpeechFontProperty());
        arabicText.setOnMouseClicked(event -> {
            Text source = (Text) event.getSource();
            String thisId = pn.getId();
            if (event.isPopupTrigger()) {
                ownerLinkNode = null;
                dependentLinkNode = pn;
                initPartOfSpeechContextMenu(thisId);
                partOfSpeechContextMenu.show(source, event.getScreenX(), event.getScreenY());
            } else {
                // single click, populate editor with this node
                canvasDataObject.get().setSelectedNode(pn);
            }
        });
        Circle circle = tool.drawCircle(null, color, pn.getCx(), pn.getCy(), RADIUS);
        // bind coordinates
        circle.centerXProperty().bind(pn.cxProperty());
        circle.centerYProperty().bind(pn.cyProperty());

        Group group = new Group();
        group.getChildren().addAll(line, arabicText, circle);
        canvasPane.getChildren().add(group);
    }

    private Optional<ButtonType> showAlert(Alert.AlertType type, String contentText, String headerText) {
        Alert alert = new Alert(type);
        if (headerText != null) {
            alert.setHeaderText(headerText);
        }
        alert.setContentText(contentText);
        return alert.showAndWait();
    }

    public Pane getCanvasPane() {
        return canvasPane;
    }

    private Font getTokenFont() {
        return canvasDataObjectProperty().get().getCanvasMetaData().getTokenFont();
    }

    private Font getPartOfSpeechFont() {
        return canvasDataObjectProperty().get().getCanvasMetaData().getPartOfSpeechFont();
    }

    private Font getTranslationFont() {
        return canvasDataObjectProperty().get().getCanvasMetaData().getTranslationFont();
    }
}
