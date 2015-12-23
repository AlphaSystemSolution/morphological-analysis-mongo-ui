package com.alphasystem.morphologicalanalysis.util;

import com.alphasystem.arabic.model.ProNoun;
import com.alphasystem.morphologicalanalysis.common.model.VerseTokenPairGroup;
import com.alphasystem.morphologicalanalysis.graph.model.DependencyGraph;
import com.alphasystem.morphologicalanalysis.graph.model.GraphMetaInfo;
import com.alphasystem.morphologicalanalysis.graph.model.TerminalNode;
import com.alphasystem.morphologicalanalysis.graph.repository.DependencyGraphRepository;
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
    private DependencyGraphRepository dependencyGraphRepository;

    /**
     * do not let anyone instantiate this class
     */
    private RepositoryTool() {
        repositoryUtil = ApplicationContextProvider
                .getInstance().getBean(MorphologicalAnalysisRepositoryUtil.class);
        tokenRepository = repositoryUtil.getTokenRepository();
        locationRepository = repositoryUtil.getLocationRepository();
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

    private Token getToken(Token oldToken, boolean next) {
        if (oldToken == null) {
            return null;
        }
        Integer chapterNumber = oldToken.getChapterNumber();
        Integer verseNumber = oldToken.getVerseNumber();
        Integer tokenNumber = oldToken.getTokenNumber();
        boolean theFirstToken = chapterNumber == 1 && verseNumber == 1 && tokenNumber == 1;
        boolean theLastToken = chapterNumber == 114 && verseNumber == 6 && tokenNumber == 3;
        if (next && theLastToken) {
            // no more token beyond this
            return null;
        }
        if (!next && theFirstToken) {
            // no more token beyond this
            return null;
        }
        Integer newChapterNumber = chapterNumber;
        Integer newVerseNumber = verseNumber;
        Integer newTokenNumber = next ? (tokenNumber + 1) : (tokenNumber - 1);
        Token newToken = new Token(newChapterNumber, newVerseNumber, newTokenNumber, "");
        newToken = getTokenByDisplayName(newToken.getDisplayName());
        if (newToken == null) {
            // if we don't find any token that means "oldToken" was the last token of this verse,
            // let's move to next verse.
            newVerseNumber = next ? (verseNumber + 1) : (verseNumber - 1);
            newTokenNumber = 1;
            newToken = new Token(newChapterNumber, newVerseNumber, newTokenNumber, "");
            newToken = getTokenByDisplayName(newToken.getDisplayName());
            if (newToken == null) {
                // we still haven't able to find  next token yet, "oldToken" was the last token of this chapter,
                // let's move to next chapter
                newChapterNumber = next ? (chapterNumber + 1) : (chapterNumber - 1);
                newVerseNumber = 1;
                newTokenNumber = 1;
                newToken = new Token(newChapterNumber, newVerseNumber, newTokenNumber, "");
                newToken = getTokenByDisplayName(newToken.getDisplayName());
                if (newToken == null) {
                    // this should never happen, raise the flag
                    throw new IllegalArgumentException(format("No next token found after {%s:%s:%s}",
                            chapterNumber, verseNumber, tokenNumber));
                }
            }
        }
        return newToken;
    }

    public Token getNextToken(Token oldToken) {
        return getToken(oldToken, true);
    }

    public Token getPreviousToken(Token oldToken) {
        return getToken(oldToken, false);
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

        tokenRepository.save(token);
        return token;
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
