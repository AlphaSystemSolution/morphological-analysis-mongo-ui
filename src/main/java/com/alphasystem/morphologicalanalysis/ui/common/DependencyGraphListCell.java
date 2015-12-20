package com.alphasystem.morphologicalanalysis.ui.common;

import com.alphasystem.arabic.model.ArabicWord;
import com.alphasystem.morphologicalanalysis.common.model.VerseTokensPair;
import com.alphasystem.morphologicalanalysis.graph.model.DependencyGraph;
import com.alphasystem.morphologicalanalysis.util.RepositoryTool;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Verse;
import com.alphasystem.morphologicalanalysis.wordbyword.repository.VerseRepository;
import javafx.scene.control.ListCell;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

import static com.alphasystem.arabic.model.ArabicLetters.WORD_SPACE;
import static com.alphasystem.arabic.model.ArabicWord.getVerseNumber;
import static com.alphasystem.arabic.ui.util.FontConstants.ARABIC_FONT_30;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.SPACE_STR;
import static javafx.scene.control.ContentDisplay.GRAPHIC_ONLY;

/**
 * @author sali
 */
public class DependencyGraphListCell extends ListCell<DependencyGraph> {

    private final Text label;
    private VerseRepository verseRepository = RepositoryTool.getInstance().getRepositoryUtil().getVerseRepository();

    public DependencyGraphListCell() {
        setContentDisplay(GRAPHIC_ONLY);
        label = new Text();
        label.setFont(ARABIC_FONT_30);
    }

    @Override
    protected void updateItem(DependencyGraph item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null && !empty) {
            List<VerseTokensPair> tokenInfoList = item.getTokens();
            VerseTokensPair tokenInfo = tokenInfoList.get(0);
            Integer chapterNumber = item.getChapterNumber();
            StringBuilder builder = new StringBuilder();
            builder.append(getTokensText(chapterNumber, tokenInfo));
            for (int i = 1; i < tokenInfoList.size(); i++) {
                tokenInfo = tokenInfoList.get(i);
                builder.append(SPACE_STR).append(getVerseNumber(chapterNumber, tokenInfo.getVerseNumber(), false))
                        .append(SPACE_STR).append(getTokensText(chapterNumber, tokenInfo));
            }

            label.setText(builder.toString());
        }

        setGraphic(label);
    }

    private String getTokensText(Integer chapterNumber, VerseTokensPair tokenInfo) {
        Verse verse = verseRepository.findByChapterNumberAndVerseNumber(chapterNumber, tokenInfo.getVerseNumber());
        List<Token> tokens = getGraphTokens(verse, tokenInfo.getFirstTokenIndex(), tokenInfo.getLastTokenIndex());
        return getArabicWord(tokens).toUnicode();
    }

    private ArabicWord getArabicWord(List<Token> tokens) {
        ArabicWord word = new ArabicWord();
        Token token = tokens.get(0);
        word.append(token.getTokenWord());
        for (int i = 1; i < tokens.size(); i++) {
            token = tokens.get(i);
            word.append(WORD_SPACE).append(token.getTokenWord());
        }
        return word;
    }

    private List<Token> getGraphTokens(Verse verse, int firstIndex, int lastIndex) {
        List<Token> allTokens = verse.getTokens();
        List<Token> tokens = new ArrayList<>();
        for (int i = firstIndex; i <= lastIndex; i++) {
            tokens.add(allTokens.get(i - 1));
        }
        return tokens;
    }
}
