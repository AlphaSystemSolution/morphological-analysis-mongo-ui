package com.alphasystem.morphologicalengine.ui.control;

import com.alphasystem.BusinessException;
import com.alphasystem.app.morphologicalengine.ui.util.MorphologicalEnginePreferences;
import com.alphasystem.morphologicalanalysis.morphology.model.ChartConfiguration;
import com.alphasystem.util.GenericPreferences;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

import static javafx.scene.control.ButtonType.CANCEL;
import static javafx.scene.control.ButtonType.OK;
import static javafx.stage.Modality.APPLICATION_MODAL;

/**
 * @author sali
 */
public class ChartConfigurationDialog extends Dialog<ChartConfiguration> {

    private final ChartConfigurationView view = new ChartConfigurationView();

    public ChartConfigurationDialog() {
        setTitle("Select Chart Configuration");
        initModality(APPLICATION_MODAL);

        getDialogPane().setContent(view);
        getDialogPane().getScene().getWindow().sizeToScene();
        getDialogPane().getButtonTypes().addAll(OK, CANCEL);
        setResultConverter(this::save);
    }

    private ChartConfiguration save(ButtonType param) {
        final ChartConfiguration chartConfiguration = param.getButtonData().isDefaultButton() ? view.getChartConfiguration() : null;
        try {
            GenericPreferences.getInstance(MorphologicalEnginePreferences.class).save();
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return chartConfiguration;
    }

    public void setChartConfiguration(ChartConfiguration chartConfiguration) {
        view.setChartConfiguration(chartConfiguration);
    }
}
