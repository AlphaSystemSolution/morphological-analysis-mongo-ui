package com.alphasystem.fx.ui.util;

import javafx.util.StringConverter;

/**
 * @author sali
 */
public class FontSizeStringConverter extends StringConverter<Integer> {

    @Override
    public String toString(Integer object) {
        return (object == null) ? "" : String.valueOf(object);
    }

    @Override
    public Integer fromString(String string) {
        return (string == null) ? 0 : Integer.parseInt(string);
    }
}
