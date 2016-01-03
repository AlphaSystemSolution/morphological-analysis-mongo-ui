package com.alphasystem.morphologicalanalysis.ui.wordbyword.model;

import static com.alphasystem.util.AppUtil.isGivenType;
import static java.util.Objects.hash;

/**
 * @author sali
 */
public final class AsciiDocStyle implements Comparable<AsciiDocStyle> {

    private final String name;
    private final String description;
    private final String formatStart;
    private final String formatEnd;

    public AsciiDocStyle(String name, String description, String formatStart, String formatEnd) {
        this.name = name;
        this.description = description;
        this.formatStart = formatStart;
        this.formatEnd = formatEnd;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getFormatStart() {
        return formatStart;
    }

    public String getFormatEnd() {
        return formatEnd;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = super.equals(obj);
        if (isGivenType(AsciiDocStyle.class, obj)) {
            AsciiDocStyle o = (AsciiDocStyle) obj;
            result = getName().equals(o.getName());
        }
        return result;
    }

    @Override
    public int compareTo(AsciiDocStyle o) {
        return (o == null) ? 1 : name.compareTo(o.getName());
    }

    @Override
    public int hashCode() {
        return hash(name);
    }

    @Override
    public String toString() {
        return description;
    }
}
