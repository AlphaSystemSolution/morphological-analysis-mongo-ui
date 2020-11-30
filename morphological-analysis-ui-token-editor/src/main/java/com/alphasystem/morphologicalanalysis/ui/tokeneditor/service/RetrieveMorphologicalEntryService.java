package com.alphasystem.morphologicalanalysis.ui.tokeneditor.service;

import com.alphasystem.arabic.model.NamedTemplate;
import com.alphasystem.morphologicalanalysis.morphology.model.MorphologicalEntry;
import com.alphasystem.morphologicalanalysis.morphology.model.RootLetters;
import com.alphasystem.morphologicalanalysis.ui.util.ApplicationContextProvider;
import com.alphasystem.morphologicalanalysis.ui.util.RestClient;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * @author sali
 */
public class RetrieveMorphologicalEntryService extends Service<MorphologicalEntry> {

    private final RestClient restClient;
    private final RootLetters rootLetters;
    private final NamedTemplate namedTemplate;

    public RetrieveMorphologicalEntryService(RootLetters rootLetters, NamedTemplate namedTemplate) {
        this.rootLetters = rootLetters;
        this.namedTemplate = namedTemplate;
        this.restClient = ApplicationContextProvider.getBean(RestClient.class);
    }

    @Override
    protected Task<MorphologicalEntry> createTask() {
        return new Task<MorphologicalEntry>() {
            @Override
            protected MorphologicalEntry call() throws Exception {
                MorphologicalEntry source = new MorphologicalEntry();
                source.setRootLetters(rootLetters);
                source.setForm(namedTemplate);
                source.initDisplayName();

                return restClient.findMorphologicalEntry(source.getDisplayName());
            }
        };
    }
}
