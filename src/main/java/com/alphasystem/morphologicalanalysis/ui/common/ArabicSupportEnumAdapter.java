package com.alphasystem.morphologicalanalysis.ui.common;

import com.alphasystem.arabic.model.ArabicSupportEnum;

/**
 * @author sali
 */
public class ArabicSupportEnumAdapter<T extends ArabicSupportEnum> {

    private final T value;

    public ArabicSupportEnumAdapter(T value) {
        this.value = value;
    }

    public static <T extends ArabicSupportEnum> ArabicSupportEnumAdapter<T>[] populateValues(T[] values) {
        ArabicSupportEnumAdapter<T>[] items = new ArabicSupportEnumAdapter[values.length];
        for (int i = 0; i < values.length; i++) {
            items[i] = new ArabicSupportEnumAdapter<>(values[i]);
        }
        return items;
    }

    public T getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.getLabel().toUnicode();
    }
}
