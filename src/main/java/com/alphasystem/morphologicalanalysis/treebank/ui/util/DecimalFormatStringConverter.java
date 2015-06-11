package com.alphasystem.morphologicalanalysis.treebank.ui.util;

import javafx.util.StringConverter;

import java.text.DecimalFormat;
import java.text.ParseException;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author sali
 */
public class DecimalFormatStringConverter extends StringConverter<Double> {

    public static final DecimalFormatStringConverter SINGLE_DECIMAL_PLACE_CONVERTER =
            new DecimalFormatStringConverter("#.0");

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
    public Double fromString(String value) {
        Double result = 0.0;
        if (isBlank(value)) {
            return result;
        }
        try {
            result = (Double) decimalFormat.parse(value);
        } catch (ParseException e) {
            // there is non-digit number, filter out numbers only
            System.out.println("Original  value: " + value);
            StringBuilder builder = new StringBuilder();
            char[] chars = value.toCharArray();
            for (char c : chars) {
                if (c >= '0' || c <= '9') {
                    builder.append(c);
                }
            }
            System.out.println("Final value: " + builder.toString());
            result = fromString(builder.toString());
        }
        return result;
    }
}
