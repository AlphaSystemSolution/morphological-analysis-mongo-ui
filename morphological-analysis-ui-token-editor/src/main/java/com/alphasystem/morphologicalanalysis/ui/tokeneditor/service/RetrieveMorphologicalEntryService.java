package com.alphasystem.morphologicalanalysis.ui.tokeneditor.service;

import com.alphasystem.morphologicalanalysis.morphology.model.MorphologicalEntry;
import com.alphasystem.morphologicalanalysis.ui.util.RestClient;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author sali
 */
@org.springframework.stereotype.Service
public class RetrieveMorphologicalEntryService extends Service<MorphologicalEntry> {

    @Autowired private RestClient restClient;
    private final MorphologicalEntry source;

    public RetrieveMorphologicalEntryService(MorphologicalEntry source) {
        this.source = source;
    }

    @Override
    protected Task<MorphologicalEntry> createTask() {
        return new Task<MorphologicalEntry>() {
            @Override
            protected MorphologicalEntry call() throws Exception {
                MorphologicalEntry savedEntry = restClient.findMorphologicalEntry(source.getDisplayName());
                // if current entry is same as the one just retrieved from DB, then we will not update the UI
                return (savedEntry == null || savedEntry.equals(source)) ? null : savedEntry;
            }
        };
    }
}
