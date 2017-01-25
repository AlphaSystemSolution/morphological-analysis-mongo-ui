package com.alphasystem.morphologicalanalysis.ui.wordbyword;

import com.alphasystem.morphologicalanalysis.ui.common.ChapterListCell;
import com.alphasystem.morphologicalanalysis.ui.common.TokenNumberListCell;
import com.alphasystem.morphologicalanalysis.ui.common.VerseListCell;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.TokenPropertiesView;
import com.alphasystem.morphologicalanalysis.util.RepositoryTool;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Chapter;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Verse;
import com.alphasystem.morphologicalanalysis.wordbyword.util.ChapterComparator;
import com.alphasystem.morphologicalanalysis.wordbyword.util.TokenComparator;
import com.alphasystem.morphologicalanalysis.wordbyword.util.VerseComparator;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.alphasystem.fx.ui.util.UiUtilities.defaultCursor;
import static com.alphasystem.fx.ui.util.UiUtilities.waitCursor;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.GAP;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.RESOURCE_BUNDLE;
import static com.alphasystem.util.AppUtil.NEW_LINE;
import static de.jensd.fx.glyphs.GlyphsDude.setIcon;
import static de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.ANGLE_DOUBLE_LEFT;
import static de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.ANGLE_DOUBLE_RIGHT;
import static de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.SAVE;
import static java.lang.String.format;
import static java.util.Collections.binarySearch;
import static javafx.geometry.Pos.CENTER;
import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.Alert.AlertType.INFORMATION;
import static javafx.scene.input.KeyCode.F;
import static javafx.scene.input.KeyCode.F4;
import static javafx.scene.input.KeyCode.LEFT;
import static javafx.scene.input.KeyCode.RIGHT;
import static javafx.scene.input.KeyCode.S;
import static javafx.scene.input.KeyCombination.ALT_DOWN;
import static javafx.scene.input.KeyCombination.SHORTCUT_DOWN;

/**
 * @author sali
 */
public class TokenEditorPane extends BorderPane {

    public static final int DEFAULT_INDEX = 0;
    private final RepositoryTool repositoryTool = RepositoryTool.getInstance();
    private final ComboBox<Chapter> chapterComboBox = new ComboBox<>();
    private final ComboBox<Verse> verseComboBox = new ComboBox<>();
    private final ComboBox<Token> tokenComboBox = new ComboBox<>();
    private final TokenPropertiesView tokenPropertiesView = new TokenPropertiesView();
    private final ReadOnlyObjectWrapper<Token> token = new ReadOnlyObjectWrapper<>(null, "token");
    private final ReadOnlyObjectWrapper<Token> nextToken = new ReadOnlyObjectWrapper<>(null, "nextToken");
    private final ReadOnlyObjectWrapper<Token> previousToken = new ReadOnlyObjectWrapper<>(null, "previousToken");

    public TokenEditorPane() {
        tokenComboBox.setDisable(true);
        tokenComboBox.setButtonCell(new TokenNumberListCell());
        tokenComboBox.setCellFactory(param -> new TokenNumberListCell());
        tokenComboBox.valueProperty().addListener((o, ov, nv) -> updateToken(nv));

        verseComboBox.setDisable(true);
        verseComboBox.setButtonCell(new VerseListCell());
        verseComboBox.setCellFactory(param -> new VerseListCell());
        verseComboBox.valueProperty().addListener((o, ov, nv) -> updateTokenComboBox(nv));

        chapterComboBox.setDisable(true);
        chapterComboBox.setButtonCell(new ChapterListCell());
        chapterComboBox.setCellFactory(param -> new ChapterListCell());
        chapterComboBox.valueProperty().addListener((o, ov, nv) -> updateVerseComboBox(nv));

        initializeChapterVerseTokenPane();
        setTop(createTopPane());
        setCenter(tokenPropertiesView);

        Service<List<Chapter>> chapterService = repositoryTool.getAllChapters();
        chapterService.setOnReady(event -> waitCursor(this));
        chapterService.setOnFailed(event -> defaultCursor(this));
        chapterService.setOnSucceeded(this::loadChapters);
        chapterService.start();

    }

    private VBox createTopPane() {
        VBox vBox = new VBox();
        vBox.getChildren().addAll(createMenuBar(), createToolBar());
        return vBox;
    }

    private ToolBar createToolBar() {
        ToolBar toolBar = new ToolBar();

        Button toolbarButton;

        toolbarButton = new Button();
        toolbarButton.setTooltip(new Tooltip("Save current token"));
        setIcon(toolbarButton, SAVE, "2em");
        toolbarButton.disableProperty().bind(token().isNull());
        toolbarButton.setOnAction(event -> saveToken());
        toolBar.getItems().add(toolbarButton);

        toolbarButton = new Button();
        toolbarButton.setTooltip(new Tooltip("Go to next token"));
        setIcon(toolbarButton, ANGLE_DOUBLE_RIGHT, "2em");
        toolbarButton.disableProperty().bind(nextToken().isNull());
        toolbarButton.setOnAction(event -> loadNextToken());
        toolBar.getItems().add(toolbarButton);

        toolbarButton = new Button();
        toolbarButton.setTooltip(new Tooltip("Go to previous token"));
        setIcon(toolbarButton, ANGLE_DOUBLE_LEFT, "2em");
        toolbarButton.disableProperty().bind(previousToken().isNull());
        toolbarButton.setOnAction(event -> loadPreviousToken());
        toolBar.getItems().add(toolbarButton);

        return toolBar;
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        MenuItem menuItem;

        Menu menu = new Menu("File");
        menu.setAccelerator(new KeyCodeCombination(F));

        menuItem = new MenuItem("Save");
        setIcon(menuItem, SAVE);
        menuItem.disableProperty().bind(token().isNull());
        menuItem.setAccelerator(new KeyCodeCombination(S, SHORTCUT_DOWN));
        menuItem.setOnAction(event -> saveToken());
        menu.getItems().add(menuItem);

        menuItem = new MenuItem("Next");
        setIcon(menuItem, ANGLE_DOUBLE_RIGHT);
        menuItem.disableProperty().bind(nextToken().isNull());
        menuItem.setAccelerator(new KeyCodeCombination(RIGHT, SHORTCUT_DOWN));
        menuItem.setOnAction(event -> loadNextToken());
        menu.getItems().add(menuItem);

        menuItem = new MenuItem("Previous");
        setIcon(menuItem, ANGLE_DOUBLE_LEFT);
        menuItem.disableProperty().bind(previousToken().isNull());
        menuItem.setAccelerator(new KeyCodeCombination(LEFT, SHORTCUT_DOWN));
        menuItem.setOnAction(event -> loadPreviousToken());
        menu.getItems().add(menuItem);

        menu.getItems().add(new SeparatorMenuItem());

        menuItem = new MenuItem("Exit");
        menuItem.setAccelerator(new KeyCodeCombination(F4, ALT_DOWN));
        menuItem.setOnAction(event -> ((Stage) getScene().getWindow()).close());
        menu.getItems().add(menuItem);

        menuBar.getMenus().add(menu);
        return menuBar;
    }

    public void saveToken() {
        tokenPropertiesView.updateToken();
        Token token = tokenPropertiesView.getToken();
        if (token != null) {
            List<Location> locations = token.getLocations();
            StringBuilder builder = new StringBuilder();
            locations.forEach(location -> {
                if (location.isTransient()) {
                    builder.append(format("Location {%s} does not have start and end indices set", location)).append(NEW_LINE);
                }
            });
            if (builder.length() > 0) {
                Alert alert = new Alert(ERROR);
                alert.setContentText(builder.toString());
                Optional<ButtonType> result = alert.showAndWait();
                result.ifPresent(buttonType -> System.err.println("Nothing to save"));
                return;
            }
        }
        token = repositoryTool.saveToken(token);
        Alert alert = new Alert(INFORMATION);
        alert.setContentText(format("Token \"%s\" has been saved.", token));
        alert.showAndWait();
        int selectedLocationIndex = tokenPropertiesView.getSelectedLocationIndex();
        updateToken(null);
        updateToken(token);
        tokenPropertiesView.setSelectedLocation(selectedLocationIndex);
        tokenPropertiesView.setReload(false);
        tokenPropertiesView.setReload(true);
    }

    public void loadNextToken() {
        loadToken(nextToken.get());
    }

    public void loadPreviousToken() {
        loadToken(previousToken.get());
    }

    private void loadToken(Token srcToken) {
        if (srcToken != null) {
            Chapter chapter = chapterComboBox.getValue();
            Integer chapterNumber = srcToken.getChapterNumber();
            Integer verseNumber = srcToken.getVerseNumber();
            Integer tokenNumber = srcToken.getTokenNumber();
            if (!chapterNumber.equals(chapter.getChapterNumber())) {
                int indexOfChapter = binarySearch(chapterComboBox.getItems(), new Chapter(chapterNumber, ""),
                        new ChapterComparator());
                chapterComboBox.getSelectionModel().select(indexOfChapter);
            }
            Verse verse = verseComboBox.getValue();
            if (!verseNumber.equals(verse.getVerseNumber())) {
                int indexOfVerse = binarySearch(verseComboBox.getItems(), new Verse(chapterNumber, verseNumber),
                        new VerseComparator());
                verseComboBox.getSelectionModel().select(indexOfVerse);
            }
            Token token = tokenComboBox.getValue();
            if (!tokenNumber.equals(token.getTokenNumber())) {
                int indexOfToken = binarySearch(tokenComboBox.getItems(),
                        new Token(chapterNumber, verseNumber, tokenNumber, ""), new TokenComparator());
                tokenComboBox.getSelectionModel().select(indexOfToken);
            }
        }
    }

    @SuppressWarnings({"unchecked"})
    private void loadChapters(WorkerStateEvent event) {
        List<Chapter> chapters = (List<Chapter>) event.getSource().getValue();
        chapterComboBox.getItems().addAll(chapters);
        chapterComboBox.setDisable(false);
        verseComboBox.setDisable(false);
        tokenComboBox.setDisable(false);
        chapterComboBox.setValue(chapters.get(DEFAULT_INDEX));
        defaultCursor(this);
    }

    private void updateVerseComboBox(Chapter chapter) {
        List<Verse> verses = chapter.getVerses();
        if (verses == null || verses.isEmpty()) {
            verses = new ArrayList<>();
            for (int i = 1; i <= chapter.getVerseCount(); i++) {
                Verse verse = new Verse(chapter.getChapterNumber(), i);
                verse.setTokenCount(-1);
                verses.add(verse);
            }
            chapter.setVerses(verses);
        }
        verseComboBox.getItems().clear();
        verseComboBox.getItems().addAll(verses);
        verseComboBox.setValue(verses.get(DEFAULT_INDEX));
    }

    private void updateTokenComboBox(Verse verse) {
        tokenComboBox.getItems().clear();
        if (verse != null) {
            List<Token> tokens = verse.getTokens();
            Integer tokenCount = verse.getTokenCount();
            if (tokenCount <= DEFAULT_INDEX) {
                Integer chapterNumber = verse.getChapterNumber();
                Integer verseNumber = verse.getVerseNumber();
                tokenCount = repositoryTool.getRepositoryUtil().getTokenCount(chapterNumber, verseNumber);
                verse.setTokenCount(tokenCount);
                tokens = new ArrayList<>();
                for (int i = 1; i <= tokenCount; i++) {
                    Token token = new Token(chapterNumber, verseNumber, i, "");
                    tokens.add(token);
                }
                verse.setTokens(tokens);
            }
            tokenComboBox.getItems().addAll(tokens);
            tokenComboBox.setValue(tokens.get(DEFAULT_INDEX));
        }
    }

    private void updateToken(Token token) {
        Token t = null;
        if (token != null) {
            t = repositoryTool.getTokenByDisplayName(token.getDisplayName());
        }
        getNextToken(t);
        getPreviousToken(t);
        tokenPropertiesView.setToken(t);
        this.token.setValue(t);
    }

    private void initializeChapterVerseTokenPane() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(GAP);
        gridPane.setAlignment(CENTER);

        Label label = new Label(RESOURCE_BUNDLE.getString("chapterName.label"));
        label.setLabelFor(chapterComboBox);
        gridPane.add(label, DEFAULT_INDEX, DEFAULT_INDEX);
        gridPane.add(chapterComboBox, 1, DEFAULT_INDEX);

        label = new Label(RESOURCE_BUNDLE.getString("verseNumber.label"));
        label.setLabelFor(verseComboBox);
        gridPane.add(label, 2, DEFAULT_INDEX);
        gridPane.add(verseComboBox, 3, DEFAULT_INDEX);

        label = new Label(RESOURCE_BUNDLE.getString("tokenNumber.label"));
        label.setLabelFor(tokenComboBox);
        gridPane.add(label, 4, DEFAULT_INDEX);
        gridPane.add(tokenComboBox, 5, DEFAULT_INDEX);

        getChildren().add(new TitledPane(RESOURCE_BUNDLE.getString("chapterVerseToken.title.label"), gridPane));
    }

    private void getPreviousToken(Token newToken) {
        // get new token in separate thread
        RetrieveTokenService getPreviousTokenService = new RetrieveTokenService(newToken, false);
        getPreviousTokenService.setOnFailed(event -> {
            // do some thing
            event.getSource().getException().printStackTrace();
        });
        getPreviousTokenService.setOnSucceeded(event -> {
            Token token = (Token) event.getSource().getValue();
            previousToken.setValue(token);
        });
        getPreviousTokenService.start();
    }

    private void getNextToken(Token newToken) {
        // get new token in separate thread
        RetrieveTokenService getNextTokenService = new RetrieveTokenService(newToken, true);
        getNextTokenService.setOnFailed(event -> {
            // do some thing
            event.getSource().getException().printStackTrace();
        });
        getNextTokenService.setOnSucceeded(event -> {
            Token token = (Token) event.getSource().getValue();
            nextToken.setValue(token);
        });
        getNextTokenService.start();
    }

    public ReadOnlyObjectProperty<Token> nextToken() {
        return nextToken.getReadOnlyProperty();
    }

    public ReadOnlyObjectProperty<Token> previousToken() {
        return previousToken.getReadOnlyProperty();
    }

    public ReadOnlyObjectProperty<Token> token() {
        return token.getReadOnlyProperty();
    }

    private class RetrieveTokenService extends Service<Token> {

        private final Token referenceToken;
        private final boolean next;

        private RetrieveTokenService(Token referenceToken, boolean next) {
            this.referenceToken = referenceToken;
            this.next = next;
        }

        @Override
        protected Task<Token> createTask() {
            return new Task<Token>() {
                @Override
                protected Token call() throws Exception {
                    return next ? repositoryTool.getNextToken(referenceToken) :
                            repositoryTool.getPreviousToken(referenceToken);
                }
            };
        }
    }
}
