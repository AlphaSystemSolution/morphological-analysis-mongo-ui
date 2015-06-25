package com.alphasystem.morphologicalanalysis.ui.dependencygraph.components;

import com.alphasystem.morphologicalanalysis.ui.common.TokenListCell;
import com.alphasystem.morphologicalanalysis.ui.common.VerseListCell;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Verse;
import com.alphasystem.morphologicalanalysis.wordbyword.repository.TokenRepository;
import com.alphasystem.morphologicalanalysis.wordbyword.repository.VerseRepository;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import static com.alphasystem.morphologicalanalysis.ui.common.Global.RESOURCE_BUNDLE;
import static com.alphasystem.morphologicalanalysis.util.RepositoryTool.getInstance;
import static javafx.scene.control.ButtonType.CANCEL;
import static javafx.scene.control.ButtonType.OK;


/**
 * @author sali
 */
public class ReferenceNodeSelectionDialog extends Dialog<Token> {

    private final IntegerProperty chapter;
    private final IntegerProperty verse;
    private final ComboBox<Token> tokenComboBox;
    private final ComboBox<Verse> verseComboBox;
    private final TokenRepository tokenRepository = getInstance().getRepositoryUtil().getTokenRepository();
    private final VerseRepository verseRepository = getInstance().getRepositoryUtil().getVerseRepository();

    public ReferenceNodeSelectionDialog() {
        setTitle("Select Reference Node");

        chapter = new SimpleIntegerProperty();
        verse = new SimpleIntegerProperty();

        verseComboBox = new ComboBox<>();
        verseComboBox.setButtonCell(new VerseListCell());
        verseComboBox.setCellFactory(param -> new VerseListCell());
        verseComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        loadTokens(newValue);
                    }
                });

        tokenComboBox = new ComboBox<>();
        tokenComboBox.setButtonCell(new TokenListCell());
        tokenComboBox.setCellFactory(param -> new TokenListCell());

        chapterProperty().addListener((observable, oldValue, newValue) -> {
            setTitle(String.format("Select Reference Node for chapter %s", (Integer) newValue));
            setVerse(1);
        });
        verseProperty().addListener((observable, oldValue, newValue) -> {
            initDialog();
        });
        setChapter(1);

        getDialogPane().getButtonTypes().addAll(OK, CANCEL);
        setResultConverter(param -> {
            Token token = null;
            if (!param.getButtonData().isCancelButton()) {
                token = tokenComboBox.getValue();
            }
            return token;
        });
    }

    private void removeAll(ObservableList<?> children) {
        children.remove(0, children.size());
    }

    private void initDialog() {
        removeAll(verseComboBox.getItems());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        int firstVerse = 1;
        int lastVerse = getVerse();
        if (lastVerse >= 10) {
            firstVerse = lastVerse - 10 + 1;
        }
        // both to and from are excluded
        firstVerse--;
        lastVerse++;
        verseComboBox.getItems().addAll(verseRepository.findByChapterNumberAndVerseNumberBetween(
                getChapter(), firstVerse, lastVerse));

        int lastIndex = verseComboBox.getItems().size() - 1;
        verseComboBox.getSelectionModel().select(lastIndex);
        loadTokens(verseComboBox.getItems().get(lastIndex));

        Label label = new Label(RESOURCE_BUNDLE.getString("verseNumber.label"));
        label.setLabelFor(verseComboBox);
        grid.add(label, 0, 0);
        grid.add(verseComboBox, 0, 1);

        label = new Label(RESOURCE_BUNDLE.getString("token.label"));
        label.setLabelFor(tokenComboBox);
        grid.add(label, 1, 0);
        grid.add(tokenComboBox, 1, 1);

        getDialogPane().setContent(grid);
    }

    private void loadTokens(Verse verse) {
        removeAll(tokenComboBox.getItems());
        tokenComboBox.getItems().addAll(verse.getTokens());
        tokenComboBox.getSelectionModel().select(0);
    }

    public final int getChapter() {
        return chapter.get();
    }

    public final void setChapter(int chapter) {
        this.chapter.set(chapter);
    }

    public final IntegerProperty chapterProperty() {
        return chapter;
    }

    public final int getVerse() {
        return verse.get();
    }

    public final void setVerse(int verse) {
        this.verse.set(verse);
    }

    public final IntegerProperty verseProperty() {
        return verse;
    }
}
