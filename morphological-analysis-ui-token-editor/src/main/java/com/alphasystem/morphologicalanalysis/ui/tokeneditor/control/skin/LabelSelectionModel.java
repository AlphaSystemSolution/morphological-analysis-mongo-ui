package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.skin;

import com.alphasystem.arabic.ui.ArabicLabelToggleGroup;
import com.alphasystem.arabic.ui.ArabicLabelView;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.TokenPropertiesView;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;

/**
 * A simple POJO to keep data to create new {@link Location}.
 *
 * @author sali
 */
public class LabelSelectionModel {

    private TokenPropertiesView control;
    private Location location;
    private ArabicLabelView labelView;
    private ArabicLabelToggleGroup group;

    TokenPropertiesView getControl() {
        return control;
    }

    private void setControl(TokenPropertiesView control) {
        this.control = control;
    }

    Location getLocation() {
        return location;
    }

    private void setLocation(Location location) {
        this.location = location;
    }

    ArabicLabelView getLabelView() {
        return labelView;
    }

    private void setLabelView(ArabicLabelView labelView) {
        this.labelView = labelView;
    }

    ArabicLabelToggleGroup getGroup() {
        return group;
    }

    private void setGroup(ArabicLabelToggleGroup group) {
        this.group = group;
    }

    public LabelSelectionModel control(TokenPropertiesView control) {
        setControl(control);
        return this;
    }

    public LabelSelectionModel location(Location location) {
        setLocation(location);
        return this;
    }

    public LabelSelectionModel labelView(ArabicLabelView labelView) {
        setLabelView(labelView);
        return this;
    }

    public LabelSelectionModel group(ArabicLabelToggleGroup group) {
        setGroup(group);
        return this;
    }
}
