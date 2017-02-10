package com.alphasystem.morphologicalanalysis.ui.spring.support;

import com.alphasystem.morphologicalanalysis.ui.control.ChapterVerseSelectionPane;
import com.alphasystem.morphologicalanalysis.ui.util.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

/**
 * @author sali
 */
@Configuration
@Import({RestClient.class, ChapterVerseSelectionPane.class})
public class CommonConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
