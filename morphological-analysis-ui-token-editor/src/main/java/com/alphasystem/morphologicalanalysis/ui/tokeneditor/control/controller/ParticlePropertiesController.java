package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.controller;

import com.alphasystem.morphologicalanalysis.ui.model.ParticlePartOfSpeechCellModel;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.ParticlePropertiesView;
import com.alphasystem.morphologicalanalysis.ui.util.ApplicationHelper;
import com.alphasystem.morphologicalanalysis.wordbyword.model.AbstractProperties;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.ParticleProperties;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.ParticlePartOfSpeechType;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static javafx.geometry.NodeOrientation.RIGHT_TO_LEFT;
import static javafx.scene.control.ContentDisplay.GRAPHIC_ONLY;

/**
 * @author sali
 */
@Component
public class ParticlePropertiesController extends BorderPane {

    @Autowired private ParticlePropertiesView control;
    @FXML private TableView<ParticlePartOfSpeechCellModel> tableView;
    @FXML public GridPane selectedPartOfSpeechPane;
    private final List<ParticlePartOfSpeechCellModel> modelList;

    public ParticlePropertiesController() {
        final ParticlePartOfSpeechType[] values = ParticlePartOfSpeechType.values();
        modelList = new ArrayList<>(values.length);
        for (ParticlePartOfSpeechType partOfSpeechType : values) {
            final ParticlePartOfSpeechCellModel model = new ParticlePartOfSpeechCellModel(partOfSpeechType);
            model.checkedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    tableView.getSelectionModel().select(model.getParticlePartOfSpeechType().ordinal());
                } else {
                    tableView.getSelectionModel().clearSelection(model.getParticlePartOfSpeechType().ordinal());
                }
            });
            modelList.add(model);
        }
    }

    @PostConstruct
    void postConstruct() throws IOException, URISyntaxException {
        ApplicationHelper.loadFxml(control, this);
    }

    @FXML
    void initialize() {
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.getItems().addAll(modelList);
        tableView.setEditable(true);
        tableView.setMaxWidth(300);
        tableView.widthProperty().addListener((observable, oldValue, newValue) -> {
            final Pane header = (Pane) tableView.lookup("TableHeaderRow");
            if (header.isVisible()) {
                header.setMaxHeight(0);
                header.setMinHeight(0);
                header.setPrefHeight(0);
                header.setVisible(false);
            }
        });

        final TableColumn<ParticlePartOfSpeechCellModel, ParticlePartOfSpeechType> partOfSpeechTypeColumn = new TableColumn<>();
        partOfSpeechTypeColumn.setMinWidth(250);
        partOfSpeechTypeColumn.setText("Value");
        partOfSpeechTypeColumn.setCellValueFactory(param -> param.getValue().particlePartOfSpeechTypeProperty());
        partOfSpeechTypeColumn.setCellFactory(TextTableCell::new);

        final TableColumn<ParticlePartOfSpeechCellModel, Boolean> checkBoxColumn = new TableColumn<>();
        checkBoxColumn.setMaxWidth(30);
        checkBoxColumn.setCellValueFactory(param -> param.getValue().checkedProperty());
        Callback<Integer, ObservableValue<Boolean>> cb = index -> {
            final ParticlePartOfSpeechCellModel tableModel = checkBoxColumn.getTableView().getItems().get(index);
            final BooleanProperty checkedProperty = tableModel.checkedProperty();
            final ParticlePartOfSpeechType particlePartOfSpeechType = tableModel.getParticlePartOfSpeechType();
            final ParticleProperties newProperties = new ParticleProperties(particlePartOfSpeechType);
            if (tableModel.isChecked()) {
                control.getLocation().addProperties(newProperties);
            } else {
                final boolean lasItem = control.getLocation().getProperties().size() == 1;
                control.getLocation().removeProperties(newProperties);
                if (lasItem) {
                    modelList.get(0).setChecked(true);
                }
            }
            return checkedProperty;
        };
        checkBoxColumn.setCellFactory(param -> new CheckBoxTableCell<>(cb));

        tableView.getColumns().add(checkBoxColumn);
        tableView.getColumns().add(partOfSpeechTypeColumn);
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                newValue.setChecked(true);
            }
        });
        tableView.getSelectionModel().getSelectedItems().addListener((ListChangeListener<? super ParticlePartOfSpeechCellModel>) c -> {
            while (c.next()) {
                if (c.wasRemoved()) {
                    final List<? extends ParticlePartOfSpeechCellModel> removed = c.getRemoved();
                    if (removed != null) {
                        removed.forEach(o -> o.setChecked(false));
                    }
                }
                if (c.wasAdded()) {
                    final List<? extends ParticlePartOfSpeechCellModel> addedSubList = c.getAddedSubList();
                    if (addedSubList != null) {
                        addedSubList.forEach(o -> o.setChecked(true));
                    }
                }
            }
        });
        control.locationProperty().addListener((observable, oldValue, newValue) -> updateTable(newValue));
    }

    private void updateTable(Location location) {
        modelList.stream().filter(ParticlePartOfSpeechCellModel::isChecked).forEachOrdered(model -> model.setChecked(false));
        if (location == null) {
            return;
        }
        final List<AbstractProperties> properties = location.getProperties();
        properties.forEach(abstractProperties -> {
            final ParticlePartOfSpeechType partOfSpeech = ((ParticleProperties) abstractProperties).getPartOfSpeech();
            modelList.stream().filter(model -> model.getParticlePartOfSpeechType().equals(partOfSpeech)).forEachOrdered(model -> model.setChecked(true));
        });
    }

    private class TextTableCell extends TableCell<ParticlePartOfSpeechCellModel, ParticlePartOfSpeechType> {
        private final Text text;

        private TextTableCell(TableColumn<ParticlePartOfSpeechCellModel, ParticlePartOfSpeechType> column) {
            setContentDisplay(GRAPHIC_ONLY);
            text = new Text();
            text.setFont(Font.font(ApplicationHelper.PREFERENCES.getArabicFontName(), 16));
            text.setTextAlignment(TextAlignment.RIGHT);
            text.setNodeOrientation(RIGHT_TO_LEFT);
        }

        @Override
        protected void updateItem(ParticlePartOfSpeechType item, boolean empty) {
            super.updateItem(item, empty);

            BorderPane borderPane = new BorderPane();
            if (item != null && !empty) {
                text.setText(item.toLabel().toUnicode());
                borderPane.setLeft(text);
            }
            setGraphic(borderPane);
        }
    }
}
