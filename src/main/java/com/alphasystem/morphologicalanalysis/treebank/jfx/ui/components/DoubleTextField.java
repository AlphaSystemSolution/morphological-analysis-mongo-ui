package com.alphasystem.morphologicalanalysis.treebank.jfx.ui.components;

import javafx.scene.control.TextField;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author sali
 */
public class DoubleTextField extends TextField {

    private static final Pattern PATTERN = compile("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?");

    public DoubleTextField() {
        super();
    }

    public DoubleTextField(String text) {
        super(text);
    }

    @Override
    public void replaceText(int start, int end, String text) {
        if (isBlank(text)) {
            super.replaceText(start, end, text);
            return;
        }
        Matcher matcher = PATTERN.matcher(text);
        if (matcher.find()) {
            String s = matcher.group(0);
            super.replaceText(start, end, s);
        }
    }

    @Override
    public void replaceSelection(String replacement) {
        if (isBlank(replacement)) {
            super.replaceSelection(replacement);
            return;
        }
        Matcher matcher = PATTERN.matcher(replacement);
        if (matcher.find()) {
            String s = matcher.group(0);
            super.replaceSelection(s);
        }
    }
}
