package com.alphasystem.morphologicalanalysis.ui.wordbyword.spring;

import com.alphasystem.morphologicalanalysis.ui.spring.support.CommonConfiguration;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.WordByWordPane;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author sali
 */
@Configuration
@Import({CommonConfiguration.class, WordByWordPane.class})
public class MainConfiguration {
}
