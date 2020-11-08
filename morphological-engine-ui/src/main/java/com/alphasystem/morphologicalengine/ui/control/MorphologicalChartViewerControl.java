package com.alphasystem.morphologicalengine.ui.control;

import com.alphasystem.morphologicalengine.model.MorphologicalChart;
import com.alphasystem.morphologicalengine.ui.control.skin.MorphologicalChartViewerSkin;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
    private final IntegerProperty select = new SimpleIntegerProperty(this, "select", 0);

    @PostConstruct
    void postConstruct() {
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

    public final int getSelect() {
        return select.get();
    }

    public IntegerProperty selectProperty() {
        return select;
    }

    public void setSelectTab(int index) {
        selectProperty().set(index);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new MorphologicalChartViewerSkin(this);
    }

}
