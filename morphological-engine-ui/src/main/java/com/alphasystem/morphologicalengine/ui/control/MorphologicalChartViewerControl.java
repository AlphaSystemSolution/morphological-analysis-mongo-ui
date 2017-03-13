package com.alphasystem.morphologicalengine.ui.control;

import com.alphasystem.app.morphologicalengine.conjugation.model.MorphologicalChart;
import com.alphasystem.morphologicalengine.ui.control.skin.MorphologicalChartViewerSkin;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author sali
 */
@Component
public class MorphologicalChartViewerControl extends Control {

    private final ObjectProperty<MorphologicalChart> morphologicalChart = new SimpleObjectProperty<>(this, "morphologicalChart");

    @PostConstruct
    void postConstruct(){
    }

    public MorphologicalChart getMorphologicalChart() {
        return morphologicalChart.get();
    }

    public ObjectProperty<MorphologicalChart> morphologicalChartProperty() {
        return morphologicalChart;
    }

    public void setMorphologicalChart(MorphologicalChart morphologicalChart) {
        this.morphologicalChart.set(morphologicalChart);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new MorphologicalChartViewerSkin(this);
    }
}
