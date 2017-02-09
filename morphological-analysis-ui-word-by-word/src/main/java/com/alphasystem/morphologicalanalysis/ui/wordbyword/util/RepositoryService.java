package com.alphasystem.morphologicalanalysis.ui.wordbyword.util;

import com.alphasystem.morphologicalanalysis.wordbyword.model.Chapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
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
    public void postConstruct(){
        chapters = restTemplate.getForObject(String.format("%s/chapters/", urlPath), List.class);
    }

    public List<Chapter> getChapters() {
        return chapters;
    }
}
