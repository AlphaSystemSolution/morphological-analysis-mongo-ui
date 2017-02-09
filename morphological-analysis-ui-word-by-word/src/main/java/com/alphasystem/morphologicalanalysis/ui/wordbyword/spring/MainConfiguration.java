package com.alphasystem.morphologicalanalysis.ui.wordbyword.spring;

import com.alphasystem.morphologicalanalysis.ui.spring.support.CommonConfiguration;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.util.RepositoryService;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author sali
 */
@Configuration
@Import({CommonConfiguration.class, RepositoryService.class})
public class MainConfiguration {
}
