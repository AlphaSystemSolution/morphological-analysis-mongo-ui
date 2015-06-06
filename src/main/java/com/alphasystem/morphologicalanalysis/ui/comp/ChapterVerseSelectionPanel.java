/**
 *
 */
package com.alphasystem.morphologicalanalysis.ui.comp;

import com.alphasystem.morphologicalanalysis.ui.util.ChapterAdapter;
import com.alphasystem.morphologicalanalysis.ui.util.VerseAdapter;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Chapter;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Verse;
import com.alphasystem.morphologicalanalysis.wordbyword.repository.VerseRepository;
import com.alphasystem.morphologicalanalysis.wordbyword.util.MorphologicalAnalysisRepositoryUtil;
import com.alphasystem.persistence.mongo.spring.support.ApplicationContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import static com.alphasystem.arabic.ui.util.Constants.ARABIC_FONT_BOLD_20;
import static com.alphasystem.persistence.mongo.spring.support.ApplicationContextProvider.getInstance;
import static com.alphasystem.ui.util.SpringUtilities.makeCompactGrid;
import static java.awt.Color.BLACK;
import static javax.swing.BorderFactory.*;

/**
 * @author sali
 */
public class ChapterVerseSelectionPanel extends JPanel {

    private static final long serialVersionUID = -3192347734541874497L;

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ChapterVerseSelectionPanel.class);

    private MorphologicalAnalysisRepositoryUtil repositoryUtil;

    private JComboBox chapterComboBox;

    private JComboBox verseComboBox;

    private List<Chapter> chapters;

    private MorphologicalAnalysisContentPanel parentPanel;

    public ChapterVerseSelectionPanel(MorphologicalAnalysisContentPanel parent) {
        super();

        this.parentPanel = parent;

        ApplicationContextProvider applicationContextProvider = getInstance();

        repositoryUtil = applicationContextProvider
                .getBean(MorphologicalAnalysisRepositoryUtil.class);

        try {
            chapters = repositoryUtil.findAllChapters();
        } catch (Exception e) {
            e.printStackTrace();
            chapters = new ArrayList<>();
        }

        initChapterComboBox();

        ChapterAdapter chapterAdapter = (ChapterAdapter) chapterComboBox
                .getSelectedItem();
        Chapter chapter = chapterAdapter.getChapter();
        add(initContentPanel(chapter));

        setBorder(createCompoundBorder(createLineBorder(BLACK),
                createEmptyBorder(6, 6, 6, 6)));
    }

    public Verse getSelectedVerse() {
        VerseAdapter verseAdapter = (VerseAdapter) verseComboBox
                .getSelectedItem();
        int chapterNumber = verseAdapter.getChapterNumber();
        int verseNumber = verseAdapter.getVerseNumber();
        VerseRepository verseRepository = repositoryUtil.getVerseRepository();
        Verse verse = verseRepository.findByChapterNumberAndVerseNumber(
                chapterNumber, verseNumber);
        LOGGER.info("Token count for verse {} are {}", verse.getDisplayName(),
                verse.getTokenCount());
        return verse;
    }

    @SuppressWarnings({"unchecked"})
    private void initChapterComboBox() {
        final DefaultComboBoxModel model = new DefaultComboBoxModel();

        for (Chapter chapter : chapters) {
            model.addElement(new ChapterAdapter(chapter));
        }

        chapterComboBox = new JComboBox(model);
        chapterComboBox.setFont(ARABIC_FONT_BOLD_20);
        chapterComboBox.addActionListener(new AbstractAction() {

            private static final long serialVersionUID = -5690380584311190954L;

            @Override
            public void actionPerformed(ActionEvent e) {
                ChapterAdapter selectedItem = (ChapterAdapter) model
                        .getSelectedItem();
                removeAll();
                add(initContentPanel(selectedItem.getChapter()));
                updateVerseViewPanel();
                updateUI();
            }
        });
    }

    private JPanel initContentPanel(Chapter chapter) {
        JPanel innerPanel = new JPanel(new SpringLayout());

        int rows = 1;
        int cols = 4;

        JLabel label = new JLabel("Chapter:");
        Font font = label.getFont().deriveFont(16.0f);
        label.setFont(font);
        label.setLabelFor(chapterComboBox);
        innerPanel.add(label);
        innerPanel.add(chapterComboBox);

        initVerseComboBox(chapter);
        label = new JLabel("Verse:");
        label.setFont(font);
        label.setLabelFor(verseComboBox);
        innerPanel.add(label);
        innerPanel.add(verseComboBox);

        makeCompactGrid(innerPanel, rows, cols, 6, 6, 6, 6);

        return innerPanel;
    }

    @SuppressWarnings({"unchecked"})
    private void initVerseComboBox(Chapter chapter) {
        final DefaultComboBoxModel model = new DefaultComboBoxModel();
        if (chapter != null) {
            int chapterNumber = chapter.getChapterNumber();
            int verseCount = chapter.getVerseCount();
            for (int i = 1; i <= verseCount; i++) {
                model.addElement(new VerseAdapter(chapterNumber, i));
            }
        }
        verseComboBox = new JComboBox(model);
        verseComboBox.setFont(ARABIC_FONT_BOLD_20);
        verseComboBox.addActionListener(new AbstractAction() {

            private static final long serialVersionUID = -2274079736016165801L;

            @Override
            public void actionPerformed(ActionEvent e) {
                updateVerseViewPanel();
                updateUI();
            }
        });
    }

    private void updateVerseViewPanel() {
        parentPanel.updateVerseViewPanel(getSelectedVerse());
    }

}
