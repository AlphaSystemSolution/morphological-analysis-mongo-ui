package com.alphasystem.morphologicalanalysis.ui.wordbyword.spring;

import com.alphasystem.morphologicalanalysis.ui.spring.support.CommonConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author sali
 */
@Configuration
@ComponentScan({"com.alphasystem.morphologicalanalysis.ui.wordbyword.control"})
@Import({CommonConfiguration.class})
public class MainConfiguration {
}
