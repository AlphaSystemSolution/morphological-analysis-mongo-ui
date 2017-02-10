package com.alphasystem.morphologicalanalysis.ui.util;

import com.alphasystem.morphologicalanalysis.wordbyword.model.Chapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author sali
 */
@Service
public class RestClient {

    private static final String CHAPTERS_PATH_SUFFIX = "/chapters";
    @Autowired private RestTemplate restTemplate;
    @Value("${service.url}") private String urlPath;
    private ObjectMapper objectMapper = new ObjectMapper();
    private List<Chapter> chapters;

    private static String getServicePath(String pathPrefix, String uri, Object... pathVariables) {
        String pathSuffix = ArrayUtils.isEmpty(pathVariables) ? uri : String.format(uri, pathVariables);
        return String.format("%s%s", pathPrefix, pathSuffix);
    }

    @PostConstruct
    public void postConstruct() {
        getChapters();
    }

    @SuppressWarnings("unchecked")
    public List<Chapter> getChapters() {
        if (chapters == null) {
            final List<LinkedHashMap> list = restTemplate.getForObject(getServicePath(urlPath, CHAPTERS_PATH_SUFFIX), List.class);
            chapters = new ArrayList<>();
            list.forEach(linkedHashMap -> chapters.add(objectMapper.convertValue(linkedHashMap, Chapter.class)));
        }
        return chapters;
    }
}
