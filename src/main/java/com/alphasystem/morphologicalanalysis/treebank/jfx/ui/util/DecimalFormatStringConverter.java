package com.alphasystem.morphologicalanalysis.treebank.jfx.ui.util;

import javafx.util.StringConverter;

import java.text.DecimalFormat;
import java.text.ParseException;

/**
 * @author sali
 */
public class DecimalFormatStringConverter extends StringConverter<Double> {

    public static final DecimalFormatStringConverter TWO_DECIMAL_PLACE_CONVERTER =
            new DecimalFormatStringConverter("#.00");

    public static final DecimalFormatStringConverter THREE_DECIMAL_PLACE_CONVERTER =
            new DecimalFormatStringConverter();

    private DecimalFormat decimalFormat;

    public DecimalFormatStringConverter() {
        this("#.000");
    }

    public DecimalFormatStringConverter(String pattern) {
        decimalFormat = new DecimalFormat(pattern);
    }

    @Override
    public String toString(Double object) {
        return object == null ? null : decimalFormat.format(object);
    }

    @Override
    public Double fromString(String string) {
        Double result = 0.0;
        try {
            result = (Double) decimalFormat.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
}
