package com.alphasystem.morphologicalengine.ui.application;

import com.alphasystem.app.morphologicalengine.spring.MorphologicalEngineConfiguration;
import com.alphasystem.app.morphologicalengine.ui.MorphologicalChartView;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author sali
 */
@Configuration
@Import({MorphologicalEngineConfiguration.class})
@ComponentScan({"com.alphasystem.morphologicalengine.ui.control", "com.alphasystem.morphologicalengine.ui.control.controller"})
public class MorphologicalEngineUIConfiguration {

    @Bean
    MorphologicalChartView morphologicalChartView() {
        return new MorphologicalChartView();
    }

}
