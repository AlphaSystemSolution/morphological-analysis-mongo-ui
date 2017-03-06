package com.alphasystem.morphologicalanalysis.ui.access.application;

import com.alphasystem.access.builder.DocumentBuilder;
import com.alphasystem.access.model.QuestionData;
import com.alphasystem.access.model.TokenAdapter;
import com.alphasystem.morphologicalanalysis.common.model.VerseTokenPairGroup;
import com.alphasystem.morphologicalanalysis.common.model.VerseTokensPair;
import com.alphasystem.morphologicalanalysis.ui.util.ApplicationContextProvider;
import com.alphasystem.morphologicalanalysis.ui.util.RestClient;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author sali
 */
public final class ApplicationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationController.class);
    private static ApplicationController instance;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestClient restClient;
    private final DocumentBuilder documentBuilder;
    private List<QuestionData> currentData = new ArrayList<>();
    private File currentFile;
    private ObjectProperty<QuestionData> current = new SimpleObjectProperty<>(this, "current");

    /**
     * Do not let anyone instantiate this class.
     */
    private ApplicationController() {
        restClient = ApplicationContextProvider.getBean(RestClient.class);
        documentBuilder = ApplicationContextProvider.getBean(DocumentBuilder.class);
        setCurrentData(null);
        currentProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !getCurrentData().contains(newValue)) {
                getCurrentData().add(newValue);
            }
        });
    }

    public static ApplicationController getInstance() {
        if (instance == null) {
            instance = new ApplicationController();
        }
        return instance;
    }

    private static void addOrReplaceVerseTokenPairGroup(QuestionData current, VerseTokenPairGroup verseTokenPairGroup) {
        final List<VerseTokenPairGroup> verseTokenPairGroups = current.getVerseTokenPairGroups();
        if (!verseTokenPairGroups.contains(verseTokenPairGroup)) {
            current.addVerseTokenPairGroup(verseTokenPairGroup);
        } else {
            verseTokenPairGroups.set(verseTokenPairGroups.size() - 1, verseTokenPairGroup);
        }
    }

    public List<QuestionData> getCurrentData() {
        if (currentData == null) {
            setCurrentData(null);
        }
        return currentData;
    }

    public void setCurrentData(List<QuestionData> currentData) {
        this.currentData = new ArrayList<>();
        if (currentData != null) {
            this.currentData.addAll(currentData);
        }
        updateCurrent();
    }

    public final QuestionData getCurrent() {
        return current.get();
    }

    public final void setCurrent(QuestionData current) {
        this.current.set(current);
    }

    public File getCurrentFile() {
        return currentFile;
    }

    public void setCurrentFile(File currentFile) {
        this.currentFile = currentFile;
    }

    public final ObjectProperty<QuestionData> currentProperty() {
        return current;
    }

    public void openFile(File file) throws IOException {
        setCurrentFile(file);
        setCurrentData(objectMapper.readValue(currentFile, new TypeReference<List<QuestionData>>() {
        }));
    }

    public void saveFile(File file) throws IOException {
        objectMapper.writeValue(file, currentData);
    }

    public Service<Path> export(){
        return  new Service<Path>() {
            @Override
            protected Task<Path> createTask() {
                return new Task<Path>() {
                    @Override
                    protected Path call() throws Exception {
                        return documentBuilder.buildDocument(getCurrentData(), getCurrentFile());
                    }
                };
            }
        };
    }

    public void addTokens(List<TokenAdapter> selectedTokens, boolean append) throws InvalidAppendRequestException {
        if (selectedTokens.isEmpty()) {
            return;
        }
        final QuestionData current = getCurrent();
        TokenAdapter tokenAdapter = selectedTokens.get(0);
        Token token = tokenAdapter.getToken();
        Integer verseNumber = token.getVerseNumber();
        Integer firstTokenNumber = token.getTokenNumber();
        Integer lastTokenNumber = -1;

        VerseTokenPairGroup verseTokenPairGroup;
        if (append) {
            verseTokenPairGroup = canAppend(token);
        } else {
            verseTokenPairGroup = new VerseTokenPairGroup(token.getChapterNumber());
        }

        if (tokenAdapter.isHighlight()) {
            current.addHighlightedToken(token.getDisplayName());
        }

        for (int i = 1; i < selectedTokens.size(); i++) {
            tokenAdapter = selectedTokens.get(i);
            token = tokenAdapter.getToken();

            if (tokenAdapter.isHighlight()) {
                current.addHighlightedToken(token.getDisplayName());
            }
            final Integer currentVerseNumber = token.getVerseNumber();
            if (Objects.equals(currentVerseNumber, verseNumber)) {
                lastTokenNumber = token.getTokenNumber();
            } else {
                verseTokenPairGroup.addPairs(new VerseTokensPair(verseNumber, firstTokenNumber, lastTokenNumber));
                addOrReplaceVerseTokenPairGroup(current, verseTokenPairGroup);
                firstTokenNumber = token.getTokenNumber();
                lastTokenNumber = -1;
                verseNumber = currentVerseNumber;
            }
        }
        verseTokenPairGroup.addPairs(new VerseTokensPair(verseNumber, firstTokenNumber, lastTokenNumber));
        addOrReplaceVerseTokenPairGroup(current, verseTokenPairGroup);
        System.out.println(current.getVerseTokenPairGroups());
    }

    private VerseTokenPairGroup canAppend(Token token) throws InvalidAppendRequestException {
        final QuestionData current = getCurrent();
        final Integer chapterNumber = token.getChapterNumber();
        final List<VerseTokenPairGroup> verseTokenPairGroups = current.getVerseTokenPairGroups();
        if (verseTokenPairGroups.isEmpty()) {
            // this is first set of tokens to be added, create new group
            return new VerseTokenPairGroup(chapterNumber);
        }

        VerseTokenPairGroup verseTokenPairGroup = verseTokenPairGroups.get(verseTokenPairGroups.size() - 1);
        Integer previousChapterNumber = verseTokenPairGroup.getChapterNumber();
        if (!previousChapterNumber.equals(chapterNumber)) {
            throw new InvalidAppendRequestException(chapterNumber, previousChapterNumber);
        }

        final List<VerseTokensPair> pairs = verseTokenPairGroup.getPairs();
        if (!pairs.isEmpty()) {
            final VerseTokensPair verseTokensPair = pairs.get(pairs.size() - 1);
            Token previousToken = new Token(chapterNumber, verseTokensPair.getVerseNumber(),
                    verseTokensPair.getLastTokenIndex(), "");
            final Token nextToken = restClient.getNextToken(chapterNumber, verseTokensPair.getVerseNumber(),
                    verseTokensPair.getLastTokenIndex());
            if (!token.equals(nextToken)) {
                throw new InvalidAppendRequestException(token, previousToken);
            }
        }

        return verseTokenPairGroup;
    }

    private void updateCurrent() {
        if (!currentData.isEmpty()) {
            setCurrent(currentData.get(currentData.size() - 1));
        }
    }
}
