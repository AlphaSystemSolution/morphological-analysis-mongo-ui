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

    public T getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.getLabel().toUnicode();
    }
}
