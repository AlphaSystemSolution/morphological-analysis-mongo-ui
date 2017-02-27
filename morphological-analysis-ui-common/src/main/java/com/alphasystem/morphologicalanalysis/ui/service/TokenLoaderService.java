package com.alphasystem.morphologicalanalysis.ui.service;

import com.alphasystem.morphologicalanalysis.common.model.VerseTokenPairGroup;
import com.alphasystem.morphologicalanalysis.ui.util.ApplicationContextProvider;
import com.alphasystem.morphologicalanalysis.ui.util.RestClient;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * @author sali
 */
public class TokenLoaderService extends Service<TokenResultAdapter> {

    private final RestClient restClient;
    private final VerseTokenPairGroup group;
    private final boolean refresh;

    public TokenLoaderService(VerseTokenPairGroup group, boolean refresh) {
        this.group = group;
        this.refresh = refresh;
        this.restClient = ApplicationContextProvider.getBean(RestClient.class);
    }

    @Override
    protected Task<TokenResultAdapter> createTask() {
        return new Task<TokenResultAdapter>() {
            @Override
            protected TokenResultAdapter call() throws Exception {
                return new TokenResultAdapter(restClient.getTokens(group, refresh));
            }
        };
    }
}
