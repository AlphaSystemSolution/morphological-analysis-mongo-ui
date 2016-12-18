package com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin.view;

import com.alphasystem.morphologicalanalysis.graph.model.GraphNode;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.GraphNodeAdapter;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.PropertyAccessor;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.GridPane;

import static com.alphasystem.morphologicalanalysis.ui.common.Global.getMaxValue;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.getMinValue;

/**
 * @author sali
 */
class SkinViewBase<N extends GraphNode, A extends GraphNodeAdapter<N>> extends GridPane {

    private void setupField(ObjectProperty<? extends PropertyAccessor<N, A>> property, Spinner<Double> spinner, Slider slider, int lowerMinValue,
                            int upperMaxValue) {
        spinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-100, 100, 0, 0.5));
        slider.setMin(0);
        slider.setMin(0);
        slider.setValue(0);
        spinner.setOnMouseClicked(event -> property.get().set(spinner.getValue()));
        property.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                double x = newValue.get();
                final double minValue = getMinValue(x, lowerMinValue);
                final double maxValue = getMaxValue(x, upperMaxValue);
                spinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(minValue, maxValue, x, 0.5));
                slider.setMin(minValue);
                slider.setMax(maxValue);
                slider.setValue(x);
            }
        });
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            Double d = (Double) newValue;
            if (d % 1 != 0.0 && d % 0.5 != 0.0) {
                d = StrictMath.ceil(d);
            }
            slider.setValue(d);
            spinner.getValueFactory().setValue(d);
            property.get().set(d);
        });
        spinner.valueProperty().addListener((observable, oldValue, newValue) -> slider.setValue(newValue));
    }

    void setupField(ObjectProperty<? extends PropertyAccessor<N, A>> property, Spinner<Double> spinner, Slider slider) {
        setupField(property, spinner, slider, 10, 10);
    }
}
