package com.alphasystem.morphologicalanalysis.ui.util;

import com.alphasystem.morphologicalanalysis.wordbyword.model.Chapter;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class RepositoryService {

    @Autowired private RestTemplate restTemplate;
    @Value("${service.url}") private String urlPath;
    private List<Chapter> chapters;

    @PostConstruct
    @SuppressWarnings("unchecked")
    public void postConstruct() {
        final List<LinkedHashMap> list = restTemplate.getForObject(String.format("%s/chapters/", urlPath), List.class);
        chapters = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        list.forEach(linkedHashMap -> {
            chapters.add(objectMapper.convertValue(linkedHashMap, Chapter.class));
        });
    }

    public List<Chapter> getChapters() {
        return chapters;
    }
}
