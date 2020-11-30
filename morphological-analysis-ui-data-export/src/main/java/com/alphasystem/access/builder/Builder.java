package com.alphasystem.access.builder;

import com.alphasystem.access.model.TokenAdapter;
import com.alphasystem.arabic.model.ArabicTool;
import com.alphasystem.arabic.model.ArabicWord;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import com.alphasystem.openxml.builder.wml.RBuilder;
import com.alphasystem.openxml.builder.wml.RPrBuilder;
import com.alphasystem.openxml.builder.wml.WmlAdapter;
import com.alphasystem.openxml.builder.wml.WmlBuilderFactory;
import org.docx4j.wml.CTShd;
import org.docx4j.wml.R;
import org.docx4j.wml.RFonts;
import org.docx4j.wml.RPr;
import org.docx4j.wml.STHint;
import org.docx4j.wml.STShd;
import org.docx4j.wml.STThemeColor;
import org.docx4j.wml.Text;

import java.util.ArrayList;
import java.util.List;

import static com.alphasystem.util.IdGenerator.nextId;

/**
 * @author sali
 */
public abstract class Builder {

    public abstract Object[] build(List<List<TokenAdapter>> lists);

    Object[] buildParagraphRuns(List<TokenAdapter> tokens) {
        List<R> runs = new ArrayList<>();

        TokenAdapter tokenAdapter = tokens.get(0);
        Token token = tokenAdapter.getToken();
        int chapterNumber = token.getChapterNumber();
        int verseNumber = token.getVerseNumber();
        runs.add(createRun(tokenAdapter, false));
        for (int i = 1; i < tokens.size(); i++) {
            tokenAdapter = tokens.get(i);
            token = tokenAdapter.getToken();
            final Integer currentVerseNumber = token.getVerseNumber();
            if (currentVerseNumber != verseNumber) {
                runs.add(createChapterVerseNumber(chapterNumber, verseNumber));
                verseNumber = currentVerseNumber;
            }
            runs.add(createRun(tokenAdapter, true));
        }
        runs.add(createChapterVerseNumber(chapterNumber, verseNumber));

        return runs.toArray(new R[runs.size()]);
    }

    private R createRun(TokenAdapter tokenAdapter, boolean prefixSpace) {
        RFonts rFonts = WmlBuilderFactory.getRFontsBuilder().withHint(STHint.EAST_ASIA).getObject();
        final RPrBuilder rPrBuilder = WmlBuilderFactory.getRPrBuilder().withRFonts(rFonts).withRtl(true);
        if (tokenAdapter.isHighlight()) {
            CTShd shd = WmlBuilderFactory.getCTShdBuilder().withVal(STShd.CLEAR).withColor("auto")
                    .withFill("D9D9D9").withThemeFill(STThemeColor.BACKGROUND_1).withThemeFillShade("D9").getObject();
            rPrBuilder.withShd(shd);
        }
        final Token token = tokenAdapter.getToken();
        Text text = WmlBuilderFactory.getTextBuilder().withValue(token.tokenWord().toUnicode()).getObject();
        final RBuilder rBuilder = WmlBuilderFactory.getRBuilder().withRsidR(nextId()).withRsidRPr(nextId())
                .withRPr(rPrBuilder.getObject());
        if (prefixSpace) {
            rBuilder.addContent(WmlAdapter.SINGLE_SPACE);
        }
        return rBuilder.addContent(text).getObject();
    }

    private R createChapterVerseNumber(int chapterNumber, int verseNumber) {
        final ArabicWord verseChapterNumber = ArabicTool.getVerseChapterNumber(chapterNumber, verseNumber, true, true, true);
        Text text = WmlBuilderFactory.getTextBuilder().withValue(verseChapterNumber.toUnicode()).getObject();
        RFonts rFonts = WmlBuilderFactory.getRFontsBuilder().withHint(STHint.EAST_ASIA).getObject();
        final RPr rPr = WmlBuilderFactory.getRPrBuilder().withRFonts(rFonts).withRtl(true).getObject();
        return WmlBuilderFactory.getRBuilder().withRsidR(nextId()).withRPr(rPr).addContent(WmlAdapter.SINGLE_SPACE, text,
                WmlAdapter.SINGLE_SPACE).getObject();
    }
}
