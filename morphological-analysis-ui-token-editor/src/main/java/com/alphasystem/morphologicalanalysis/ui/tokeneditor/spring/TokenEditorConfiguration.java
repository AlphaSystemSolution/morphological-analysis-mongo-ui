package com.alphasystem.morphologicalanalysis.ui.tokeneditor.spring;

import com.alphasystem.app.morphologicalengine.ui.MorphologicalChartView;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author sali
 */
@Configuration
@ComponentScan({"com.alphasystem.morphologicalanalysis.ui.tokeneditor.control",
        "com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.controller"})
public class TokenEditorConfiguration {

    @Bean
    public MorphologicalChartView morphologicalChartView() {
        return new MorphologicalChartView();
    }
}
