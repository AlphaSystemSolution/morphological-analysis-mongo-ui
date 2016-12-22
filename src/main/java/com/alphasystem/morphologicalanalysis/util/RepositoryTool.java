package com.alphasystem.morphologicalanalysis.util;

import com.alphasystem.arabic.model.ProNoun;
import com.alphasystem.morphologicalanalysis.common.model.VerseTokenPairGroup;
import com.alphasystem.morphologicalanalysis.graph.model.*;
import com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType;
import com.alphasystem.morphologicalanalysis.graph.repository.DependencyGraphRepository;
import com.alphasystem.morphologicalanalysis.graph.repository.PartOfSpeechNodeRepository;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.GraphBuilder;
import com.alphasystem.morphologicalanalysis.wordbyword.model.*;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.*;
import com.alphasystem.morphologicalanalysis.wordbyword.repository.LocationRepository;
import com.alphasystem.morphologicalanalysis.wordbyword.repository.TokenRepository;
import com.alphasystem.persistence.mongo.spring.support.ApplicationContextProvider;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.List;

import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.ConversationType.FIRST_PERSON;
import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.GenderType.MASCULINE;
import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.NounStatus.NOMINATIVE;
import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.PartOfSpeech.*;
import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.ProNounType.DETACHED;
import static java.lang.String.format;
import static java.util.Collections.singletonList;

/**
 * @author sali
 */
public class RepositoryTool {

    private static RepositoryTool instance;

    private GraphBuilder graphBuilder;
    private MorphologicalAnalysisRepositoryUtil repositoryUtil;
    private TokenRepository tokenRepository;
    private LocationRepository locationRepository;
    private PartOfSpeechNodeRepository partOfSpeechNodeRepository;
    private DependencyGraphRepository dependencyGraphRepository;

    /**
     * do not let anyone instantiate this class
     */
    private RepositoryTool() {
        repositoryUtil = ApplicationContextProvider
                .getInstance().getBean(MorphologicalAnalysisRepositoryUtil.class);
        tokenRepository = repositoryUtil.getTokenRepository();
        locationRepository = repositoryUtil.getLocationRepository();
        partOfSpeechNodeRepository = repositoryUtil.getPartOfSpeechNodeRepository();
        dependencyGraphRepository = repositoryUtil.getDependencyGraphRepository();
        graphBuilder = GraphBuilder.getInstance();
    }

    public static synchronized RepositoryTool getInstance() {
        if (instance == null) {
            instance = new RepositoryTool();
        }
        return instance;
    }

    public Service<List<Chapter>> getAllChapters() {
        return new Service<List<Chapter>>() {

            @Override
            protected Task<List<Chapter>> createTask() {
                return new Task<List<Chapter>>() {

                    @Override
                    protected List<Chapter> call() throws Exception {
                        return repositoryUtil.findAllChapters();
                    } // end of "call"
                }; // end of new "Task"
            } // end of "createTask"
        }; // end of new "Service"
    }

    public Token getTokenByDisplayName(String displayName) {
        return tokenRepository.findByDisplayName(displayName);
    }

    public Token getNextToken(Token token) {
        return repositoryUtil.getNextToken(token);
    }

    public Token getPreviousToken(Token token) {
        return repositoryUtil.getPreviousToken(token);
    }

    public Token saveToken(Token token) {
        // A token contains one or more part of speeches and each part of speech has a location corresponding to it.
        // A location has "startIndex" and "endIndex" of letters from the parent token. At the time of initial data
        // creation we only created one part of speech and one location for each token. Now from UI we will start
        // assigning part of speech and location. The logic is that user will select a part of speech and select
        // letters (by selecting toggle buttons), now if, for example, a token has five letters but user has selected
        // only two letters, that means three letters still doesn't have associated part of speech.
        // we will try to find any unselected locations, if we find then we will create a new location for this token,
        // in next iteration user can assign part of speech to those unselected letters. This process will continue
        // until we all letters part of a location

        List<Location> locations = token.getLocations();
        Integer locationCount = token.getLocationCount();
        Location lastLocation = locations.get(locationCount - 1);
        // save this location first
        lastLocation = locationRepository.save(lastLocation);
        int tokenLength = token.getTokenWord().getLength();
        if (!lastLocation.isTransient() && lastLocation.getEndIndex() < tokenLength) {
            // there are still some unselected letters, so create new location
            Location location = new Location(token.getChapterNumber(), token.getVerseNumber(),
                    token.getTokenNumber(), locationCount + 1);
            locationRepository.save(location);
            token.getLocations().add(location);
        }

        return tokenRepository.save(token);
    }

    public DependencyGraph createDependencyGraph(VerseTokenPairGroup group, GraphMetaInfo graphMetaInfo)
            throws RuntimeException {
        Integer chapterNumber = group.getChapterNumber();
        String displayName = format("%s|%s", chapterNumber, group.toString());
        System.out.println(">>>>>>>>>>>>>>>> " + displayName);
        DependencyGraph dependencyGraph = dependencyGraphRepository.findByDisplayName(displayName);
        if (dependencyGraph == null) {
            List<Token> tokens = repositoryUtil.getTokens(group);
            if (tokens == null || tokens.isEmpty()) {
                throw new RuntimeException(format("Unable to create dependency graph for %s", displayName));
            }
            dependencyGraph = new DependencyGraph(chapterNumber);
            dependencyGraph.getTokens().addAll(group.getPairs());
            dependencyGraph.initDisplayName();
            dependencyGraph.setMetaInfo(graphMetaInfo);
            graphBuilder.set(graphMetaInfo);
            List<TerminalNode> terminalNodes = graphBuilder.buildTerminalNodes(tokens);
            for (TerminalNode terminalNode : terminalNodes) {
                terminalNode.initDisplayName();
                dependencyGraph.addNode(terminalNode);
            }
        }

        return dependencyGraph;
    }

    public boolean deleteDependencyGraph(String displayName) {
        final DependencyGraph dependencyGraph = repositoryUtil.getDependencyGraph(displayName);
        if (dependencyGraph == null) {
            return false;
        }
        final List<GraphNode> nodes = dependencyGraph.getNodes();
        if (nodes != null && !nodes.isEmpty()) {
            nodes.forEach(graphNode -> {
                final GraphNodeType graphNodeType = graphNode.getGraphNodeType();
                System.out.println(graphNode);
                switch (graphNodeType) {
                    case TERMINAL:
                    case REFERENCE:
                        TerminalNode terminalNode = (TerminalNode) graphNode;
                        deletePartOfSpeechNodes(terminalNode);
                        break;
                    case PHRASE:
                    case RELATIONSHIP:
                        repositoryUtil.getRepository(graphNode.getGraphNodeType()).delete(graphNode);
                        break;
                    case HIDDEN:
                    case IMPLIED:
                        TerminalNode node = (TerminalNode) graphNode;
                        deleteToken(node.getToken());
                        deletePartOfSpeechNodes(node);
                        break;
                    case ROOT:
                        break;
                }
            });
        }
        dependencyGraphRepository.delete(dependencyGraph);
        return true;
    }

    private void deletePartOfSpeechNodes(TerminalNode terminalNode) {
        final List<PartOfSpeechNode> partOfSpeechNodes = terminalNode.getPartOfSpeechNodes();
        partOfSpeechNodes.forEach(partOfSpeechNode -> {
            if (partOfSpeechNode != null) {
                System.out.println(format("    POS: %s", partOfSpeechNode));
                partOfSpeechNodeRepository.delete(partOfSpeechNode);
            }
        });
        repositoryUtil.getRepository(terminalNode.getGraphNodeType()).delete(terminalNode);
    }

    private void deleteToken(Token token){
        System.out.println(format("    Token: %s", token));
        token.getLocations().forEach(location -> {
            if(location != null){
                System.out.println(format("        Location: %s", location));
                locationRepository.delete(location);
            }
        });
        tokenRepository.delete(token);
    }

    public boolean isTransientGraph(String displayName) {
        return repositoryUtil.getDependencyGraphRepository().findByDisplayName(displayName) == null;
    }

    public DependencyGraph recreateDependencyGraph(DependencyGraph src, List<TerminalNode> terminalNodes) {
        if (src == null) {
            //TODO: handle error
            return null;
        }
        DependencyGraph dependencyGraph = new DependencyGraph();
        dependencyGraph.setChapterNumber(src.getChapterNumber());
        dependencyGraph.setMetaInfo(src.getMetaInfo());
        dependencyGraph.setTokens(src.getTokens());
        dependencyGraph.setDisplayName(src.getDisplayName());
        terminalNodes.forEach(terminalNode -> {
            terminalNode.initDisplayName();
            dependencyGraph.addNode(terminalNode);
        });

        return dependencyGraph;
    }

    public Token createImpliedNode(Token srcToken, PartOfSpeech partOfSpeech, Object type) {
        Token token = null;
        if (NOUN.equals(partOfSpeech)) {
            token = createToken(srcToken, "(*)", partOfSpeech, new NounProperties().withNounStatus((NounStatus) type));
        } else if (VERB.equals(partOfSpeech)) {
            token = createToken(srcToken, "(*)", VERB, new VerbProperties().withVerbType((VerbType) type));
        } else if (PRONOUN.equals(partOfSpeech)) {
            ProNoun proNoun = (ProNoun) type;
            token = createToken(srcToken, proNoun.getLabel().toUnicode(), PRONOUN, createProNounProperties(proNoun));
        }
        return token;
    }

    public Token createHiddenNode(Token srcToken, GenderType genderType, NumberType numberType,
                                  ConversationType conversationType) {
        String genderAndType = genderType.name();
        genderAndType = conversationType.equals(FIRST_PERSON) ? "" : format("_%s", genderAndType);
        String pronounId = format("%s%s_%s", conversationType.name(), genderAndType, numberType.name());
        ProNoun proNoun = ProNoun.valueOf(pronounId);
        return createToken(srcToken, proNoun.getLabel().toUnicode(), PRONOUN,
                createProNounProperties(genderType, numberType, conversationType));
    }

    private ProNounProperties createProNounProperties(GenderType genderType, NumberType numberType,
                                                      ConversationType conversationType) {
        return (ProNounProperties) new ProNounProperties().withProNounType(DETACHED)
                .withConversationType(conversationType).withNounStatus(NOMINATIVE).withGenderType(genderType)
                .withNumberType(numberType);
    }

    private ProNounProperties createProNounProperties(ProNoun proNoun) {
        String name = proNoun.name();
        int index = name.indexOf('_');
        int secondIndex = name.indexOf('_', index + 1);
        ConversationType conversationType = ConversationType.valueOf(name.substring(0, secondIndex));
        index = name.lastIndexOf('_');
        NumberType numberType = NumberType.valueOf(name.substring(index + 1));
        GenderType genderType = MASCULINE;
        if (!FIRST_PERSON.equals(conversationType)) {
            genderType = GenderType.valueOf(name.substring(secondIndex + 1, index));
        }
        return createProNounProperties(genderType, numberType, conversationType);
    }

    private Token createToken(Token srcToken, String tokenText, PartOfSpeech partOfSpeech, AbstractProperties properties) {
        Token token = new Token().withHidden(true).withToken(tokenText).withChapterNumber(srcToken.getChapterNumber())
                .withVerseNumber(srcToken.getVerseNumber()).withTokenNumber(srcToken.getTokenNumber());

        Location location = new Location().withHidden(true).withChapterNumber(token.getChapterNumber())
                .withVerseNumber(token.getVerseNumber()).withTokenNumber(token.getTokenNumber())
                .withLocationIndex(1).withPartOfSpeech(partOfSpeech).withStartIndex(0)
                .withEndIndex(token.getTokenWord().getLength()).withProperties(properties);

        token.setLocations(singletonList(location));
        return token;
    }

    public MorphologicalAnalysisRepositoryUtil getRepositoryUtil() {
        return repositoryUtil;
    }
}
