package com.alphasystem.morphologicalanalysis.ui.util;

import com.alphasystem.morphologicalanalysis.common.model.VerseTokenPairGroup;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Chapter;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sali
 */
@Service
public class RestClient {

    private static final String CHAPTERS_PATH_SUFFIX = "/chapters";
    private static final String TOKENS_PATH_SUFFIX = "/tokens";

    @Autowired private RestTemplate restTemplate;
    @Value("${service.url}") private String urlPath;

    private Map<String, List<Token>> cache = new LinkedHashMap<>();
    private Chapter[] chapters;

    private static URI getServicePath(String pathPrefix, String uri, Object... pathVariables) {
        String pathSuffix = ArrayUtils.isEmpty(pathVariables) ? uri : String.format(uri, pathVariables);
        return URI.create(String.format("%s%s", pathPrefix, pathSuffix));
    }

    @PostConstruct
    public void postConstruct() {
        getChapters();
    }

    @SuppressWarnings("unchecked")
    public Chapter[] getChapters() {
        if (chapters == null) {
            final URI uri = getServicePath(urlPath, CHAPTERS_PATH_SUFFIX);
            final ResponseEntity<Chapter[]> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, null, new ChapterListType());
            chapters = responseEntity.getBody();
        }
        return chapters;
    }

    @SuppressWarnings("unchecked")
    public List<Token> getTokens(VerseTokenPairGroup group, boolean refresh) {
        final String key = group.toString();
        List<Token> tokens = refresh ? null : cache.get(key);
        if (tokens == null || tokens.isEmpty()) {
            final URI uri = getServicePath(urlPath, TOKENS_PATH_SUFFIX);
            final HttpEntity<VerseTokenPairGroup> entity = new HttpEntity<>(group);
            final ResponseEntity<List<Token>> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, entity, new TokenListType());
            tokens = responseEntity.getBody();
            cache.put(key, tokens);
        }
        return tokens;
    }

    private static class ChapterListType extends ParameterizedTypeReference<Chapter[]> {
    }

    private static class TokenListType extends ParameterizedTypeReference<List<Token>> {
    }
}
