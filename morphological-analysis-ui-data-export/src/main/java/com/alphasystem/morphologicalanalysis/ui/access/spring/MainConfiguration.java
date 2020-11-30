package com.alphasystem.morphologicalanalysis.ui.access.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author sali
 */
@Configuration
@ComponentScan({"com.alphasystem.access.builder",
        "com.alphasystem.morphologicalanalysis.ui.access.control",
        "com.alphasystem.morphologicalanalysis.ui.access.control.controller"})
public class MainConfiguration {
}
