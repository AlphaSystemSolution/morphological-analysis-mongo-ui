package com.alphasystem.morphologicalanalysis.ui.wordbyword.control;

import com.alphasystem.morphologicalanalysis.ui.control.ChapterVerseSelectionPane;
import com.alphasystem.morphologicalanalysis.util.MorphologicalAnalysisPreferences;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import com.alphasystem.util.GenericPreferences;
import javafx.scene.layout.BorderPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sali
 */
@Component
public class WordByWordPane extends BorderPane {

    private final MorphologicalAnalysisPreferences preferences = GenericPreferences.getInstance(MorphologicalAnalysisPreferences.class);
    private static Map<String, List<Token>> cache = new LinkedHashMap<>();
    @Autowired private ChapterVerseSelectionPane chapterVerseSelectionPane;

    public WordByWordPane() {
    }

    @PostConstruct
    public void postConstruct(){
        BorderPane topPane = new BorderPane();

        topPane.setCenter(chapterVerseSelectionPane);

        setTop(topPane);
    }
}
