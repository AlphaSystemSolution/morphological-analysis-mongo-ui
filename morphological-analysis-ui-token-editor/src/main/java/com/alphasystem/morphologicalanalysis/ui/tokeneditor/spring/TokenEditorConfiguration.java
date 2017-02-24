package com.alphasystem.morphologicalanalysis.ui.tokeneditor.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author sali
 */
@Configuration
@ComponentScan({"com.alphasystem.morphologicalanalysis.ui.tokeneditor.service",
        "com.alphasystem.morphologicalanalysis.ui.tokeneditor.control",
        "com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.controller"})
public class TokenEditorConfiguration {
}
