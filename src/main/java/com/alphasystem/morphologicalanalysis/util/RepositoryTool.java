package com.alphasystem.morphologicalanalysis.util;

import com.alphasystem.morphologicalanalysis.graph.model.DependencyGraph;
import com.alphasystem.morphologicalanalysis.graph.model.GraphMetaInfo;
import com.alphasystem.morphologicalanalysis.graph.model.TerminalNode;
import com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType;
import com.alphasystem.morphologicalanalysis.graph.repository.DependencyGraphRepository;
import com.alphasystem.morphologicalanalysis.graph.repository.PartOfSpeechNodeRepository;
import com.alphasystem.morphologicalanalysis.graph.repository.TerminalNodeRepository;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.GraphBuilder;
import com.alphasystem.morphologicalanalysis.wordbyword.model.AbstractProperties;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Chapter;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import com.alphasystem.morphologicalanalysis.wordbyword.repository.LocationRepository;
import com.alphasystem.morphologicalanalysis.wordbyword.repository.TokenRepository;
import com.alphasystem.persistence.mongo.repository.BaseRepository;
import com.alphasystem.persistence.mongo.spring.support.ApplicationContextProvider;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.List;

/**
 * @author sali
 */
public class RepositoryTool {

    private static RepositoryTool instance;

    private GraphBuilder graphBuilder;
    private MorphologicalAnalysisRepositoryUtil repositoryUtil;
    private TokenRepository tokenRepository;
    private LocationRepository locationRepository;
    private DependencyGraphRepository dependencyGraphRepository;
    private TerminalNodeRepository terminalNodeRepository;
    private PartOfSpeechNodeRepository partOfSpeechNodeRepository;

    /**
     * do not let anyone instantiate this class
     */
    private RepositoryTool() {
        repositoryUtil = ApplicationContextProvider
                .getInstance().getBean(MorphologicalAnalysisRepositoryUtil.class);
        tokenRepository = repositoryUtil.getTokenRepository();
        locationRepository = repositoryUtil.getLocationRepository();
        dependencyGraphRepository = repositoryUtil.getDependencyGraphRepository();
        terminalNodeRepository = repositoryUtil.getTerminalNodeRepository();
        partOfSpeechNodeRepository = repositoryUtil.getPartOfSpeechNodeRepository();
        graphBuilder = GraphBuilder.getInstance();

    }

    public static synchronized RepositoryTool getInstance() {
        if (instance == null) {
            instance = new RepositoryTool();
        }
        return instance;
    }

    public BaseRepository getRepository(GraphNodeType nodeType) {
        BaseRepository repository = null;
        switch (nodeType) {
            case TERMINAL:
                repository = repositoryUtil.getTerminalNodeRepository();
                break;
            case PART_OF_SPEECH:
                repository = repositoryUtil.getPartOfSpeechNodeRepository();
                break;
            case PHRASE:
                repository = repositoryUtil.getPhraseNodeRepository();
                break;
            case RELATIONSHIP:
                repository = repositoryUtil.getRelationshipNodeRepository();
                break;
            case REFERENCE:
                repository = repositoryUtil.getReferenceNodeRepository();
                break;
            case HIDDEN:
                repository = repositoryUtil.getHiddenNodeRepository();
                break;
            case IMPLIED:
                repository = repositoryUtil.getImpliedNodeRepository();
                break;
            default:
                break;
        }
        return repository;
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

    public Service<List<Token>> getTokens(final int chapterNumber, final int verseNumber) {
        return new Service<List<Token>>() {
            @Override
            protected Task<List<Token>> createTask() {
                return new Task<List<Token>>() {
                    @Override
                    protected List<Token> call() throws Exception {
                        return tokenRepository.findByChapterNumberAndVerseNumber(
                                chapterNumber, verseNumber);
                    } // end of call
                }; // end of "new task"
            } // end of createTask
        }; // end of "new Service"
    }

    public Token getToken(String id) {
        return tokenRepository.findOne(id);
    }

    public Token getTokenByDisplayName(String displayName) {
        return tokenRepository.findByDisplayName(displayName);
    }

    public Location getLocation(String id) {
        return locationRepository.findOne(id);
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
        AbstractProperties properties = lastLocation.getProperties();
        int tokenLength = token.getTokenWord().getLength();
        if (!lastLocation.isTransient() && lastLocation.getEndIndex() < tokenLength) {
            // there are still some unselected letters, so create new location
            Location location = new Location(token.getChapterNumber(), token.getVerseNumber(),
                    token.getTokenNumber(), locationCount + 1);
            locationRepository.save(location);
            token.getLocations().add(location);
        }

        tokenRepository.save(token);
        return token;
    }

    public DependencyGraph createDependencyGraph(List<Token> tokens, GraphMetaInfo graphMetaInfo) {
        Token firstToken = tokens.get(0);
        Token lastToken = tokens.get(tokens.size() - 1);

        Integer chapterNumber = firstToken.getChapterNumber();
        Integer verseNumber = firstToken.getVerseNumber();
        Integer firstTokenIndex = firstToken.getTokenNumber();
        Integer lastTokenIndex = lastToken.getTokenNumber();

        DependencyGraph dependencyGraph = dependencyGraphRepository.
                findByChapterNumberAndVerseNumberAndFirstTokenIndexAndLastTokenIndex(
                        chapterNumber, verseNumber, firstTokenIndex, lastTokenIndex);

        if (dependencyGraph == null) {
            dependencyGraph = new DependencyGraph(chapterNumber, verseNumber, firstTokenIndex, lastTokenIndex);
            dependencyGraph.initDisplayName();
            dependencyGraph.setId(dependencyGraph.getDisplayName());
            dependencyGraph.setMetaInfo(graphMetaInfo);
            graphBuilder.set(graphMetaInfo);
            List<TerminalNode> terminalNodes = graphBuilder.buildTerminalNodes(tokens);
            terminalNodeRepository.save(terminalNodes);
            for (TerminalNode terminalNode : terminalNodes) {
                dependencyGraph.addNode(terminalNode);
            }
        }

        return dependencyGraph;
    }

    public void saveDependencyGraph(DependencyGraph dependencyGraph, List<String> removalIds) {
        dependencyGraphRepository.save(dependencyGraph);
        if (!removalIds.isEmpty()) {
            removalIds.forEach(id -> partOfSpeechNodeRepository.delete(id));
        }
    }

    public MorphologicalAnalysisRepositoryUtil getRepositoryUtil() {
        return repositoryUtil;
    }
}
