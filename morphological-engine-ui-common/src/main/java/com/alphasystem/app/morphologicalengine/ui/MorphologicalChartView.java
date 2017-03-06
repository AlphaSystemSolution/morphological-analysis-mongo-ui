package com.alphasystem.app.morphologicalengine.ui;

import com.alphasystem.app.morphologicalengine.conjugation.model.MorphologicalChart;
import com.alphasystem.app.morphologicalengine.ui.skin.MorphologicalChartSkin;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

/**
 * @author sali
 */
public class MorphologicalChartView extends Control {

    private final ObjectProperty<MorphologicalChart> morphologicalChart = new SimpleObjectProperty<>(null, "morphologicalChart");

    public MorphologicalChartView() {
        setSkin(createDefaultSkin());
    }

    public final MorphologicalChart getMorphologicalChart() {
        return morphologicalChart.get();
    }

    public final ObjectProperty<MorphologicalChart> morphologicalChartProperty() {
        return morphologicalChart;
    }

    public final void setMorphologicalChart(MorphologicalChart morphologicalChart) {
        this.morphologicalChart.set(morphologicalChart);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new MorphologicalChartSkin(this);
    }
}
