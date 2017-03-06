package com.alphasystem.access.builder;

import com.alphasystem.access.model.QuestionData;
import com.alphasystem.access.model.QuestionType;
import com.alphasystem.access.model.TokenAdapter;
import com.alphasystem.morphologicalanalysis.common.model.VerseTokenPairGroup;
import com.alphasystem.morphologicalanalysis.ui.util.RestClient;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import com.alphasystem.openxml.builder.wml.HeadingList;
import com.alphasystem.openxml.builder.wml.WmlAdapter;
import com.alphasystem.openxml.builder.wml.WmlPackageBuilder;
import org.apache.commons.io.FilenameUtils;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.String.format;

/**
 * @author sali
 */
@Component
public class DocumentBuilder {

    @Autowired private RestClient restClient;
    @Autowired private ParagraphBuilder paragraphBuilder;
    @Autowired private ListParagraphBuilder listParagraphBuilder;
    @Autowired private TableBuilder tableBuilder;

    public Path buildDocument(List<QuestionData> questionDataList, final File inputFile) throws Docx4JException {
        final String outputFilePrefix = FilenameUtils.getBaseName(inputFile.getAbsolutePath());
        final Path outputFile = Paths.get(inputFile.getParentFile().getAbsolutePath(), format("%s.docx", outputFilePrefix));
        final WmlPackageBuilder wmlPackageBuilder = WmlPackageBuilder.createPackage("META-INF/Custom.dotx")
                .styles("META-INF/custom-styles.xml").multiLevelHeading(new HeadingList("ArabicListParagraph"));

        final WordprocessingMLPackage wordprocessingMLPackage = wmlPackageBuilder.getPackage();
        buildDocument(wordprocessingMLPackage.getMainDocumentPart(), questionDataList);

        WmlAdapter.save(outputFile.toFile(), wordprocessingMLPackage);
        return outputFile;
    }

    private void buildDocument(final MainDocumentPart mainDocumentPart, List<QuestionData> questionDataList){
        questionDataList.forEach(questionData -> {
            final List<VerseTokenPairGroup> groups = questionData.getVerseTokenPairGroups();
            groups.forEach(group -> {
                final QuestionType questionType = questionData.getType();
                final List<Token> tokens = restClient.getTokens(group, false);
                List<TokenAdapter> tokenAdapterList = new ArrayList<>();
                tokens.forEach(token -> {
                    final boolean highlighted = questionData.getHighlightedTokens().contains(token.getDisplayName());
                    tokenAdapterList.add(new TokenAdapter(token, highlighted));
                });
                List<List<TokenAdapter>> lists = Collections.singletonList(tokenAdapterList);
                if (QuestionType.TRANSLATION.equals(questionType)) {
                    addObjects(mainDocumentPart, paragraphBuilder.build(lists));
                } else {
                    addObjects(mainDocumentPart, listParagraphBuilder.build(lists));
                    addObjects(mainDocumentPart, tableBuilder.build(lists));
                }
            });
        });
    }

    private void addObjects(MainDocumentPart mainDocumentPart, Object[] objects) {
        for (Object object : objects) {
            mainDocumentPart.addObject(object);
        }
    }

}
