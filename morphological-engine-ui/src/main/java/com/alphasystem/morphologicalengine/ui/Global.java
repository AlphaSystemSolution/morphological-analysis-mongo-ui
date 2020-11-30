package com.alphasystem.morphologicalengine.ui;

import com.alphasystem.app.morphologicalengine.ui.util.MorphologicalEnginePreferences;
import com.alphasystem.app.morphologicalengine.util.TemplateReader;
import com.alphasystem.arabic.model.ArabicSupport;
import com.alphasystem.util.GenericPreferences;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import static com.alphasystem.arabic.model.ArabicLetterType.WAW;
import static com.alphasystem.util.AppUtil.USER_HOME_DIR;

/**
 * @author sali
 */
public final class Global {

    public static final FileChooser FILE_CHOOSER = new FileChooser();

    static {
        FILE_CHOOSER.setInitialDirectory(USER_HOME_DIR);
        FILE_CHOOSER.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(TemplateReader.SARF_FILE_DESCRIPTION,
                TemplateReader.SARF_FILE_EXTENSION_ALL));
    }

    private static final MorphologicalEnginePreferences PREFERENCES = GenericPreferences.getInstance(MorphologicalEnginePreferences.class);

    public static Text createLabel(ArabicSupport letter) {
        Text text = new Text();
        text.setText(letter == null ? "" : letter.toLabel().toUnicode());
        text.setFont(PREFERENCES.getArabicFont());
        return text;
    }

    public static Text createAndLabel() {
        return createLabel(WAW);
    }

    public static Text createSpaceLabel() {
        return createSpaceLabel(1);
    }

    public static Text createSpaceLabel(int numOfSpace) {
        numOfSpace = numOfSpace <= 0 ? 1 : numOfSpace;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < numOfSpace; i++) {
            builder.append(" ");
        }
        return new Text(builder.toString());
    }

    public static double roundTo100(double srcValue) {
        return (double) ((((int) srcValue) + 99) / 100) * 100;
    }
}
