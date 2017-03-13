package com.alphasystem.morphologicalengine.ui.control;

import com.alphasystem.app.morphologicalengine.util.TemplateReader;
import com.alphasystem.morphologicalengine.ui.control.model.TabInfo;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;

import java.io.File;

import static com.alphasystem.morphologicalengine.ui.Global.FILE_CHOOSER;
import static javafx.beans.binding.Bindings.when;
import static javafx.geometry.Pos.CENTER;
import static javafx.scene.control.ButtonType.CANCEL;
import static javafx.scene.control.ButtonType.OK;
import static javafx.stage.Modality.APPLICATION_MODAL;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author sali
 */
public class FileSelectionDialog extends Dialog<TabInfo> {

    private static final int DEFAULT_VALUE = 10;
    private final ObjectProperty<TabInfo> tabInfo = new SimpleObjectProperty<>();
    private final StringProperty sarfxFile = new SimpleStringProperty();

    public FileSelectionDialog(final TabInfo src) {
        super();

        setTitle("Select Files");
        initModality(APPLICATION_MODAL);

        tabInfoProperty().addListener((o, ov, nv) -> initDialogPane(nv));
        setTabInfo(src);

        getDialogPane().getButtonTypes().addAll(OK, CANCEL);

        setResultConverter(this::setResult);

        Button okButton = (Button) getDialogPane().lookupButton(OK);
        okButton.disableProperty().bind(when(sarfxFile.isNotNull()).then(false).otherwise(true));
    }

    private TabInfo setResult(ButtonType param) {
        ButtonData buttonData = param.getButtonData();
        if (buttonData.isDefaultButton()) {
            return tabInfoProperty().get();
        }
        return null;
    }

    private void initDialogPane(TabInfo tabInfo) {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(DEFAULT_VALUE);
        gridPane.setVgap(DEFAULT_VALUE);
        gridPane.setAlignment(CENTER);
        gridPane.setPadding(new Insets(DEFAULT_VALUE, DEFAULT_VALUE, DEFAULT_VALUE, DEFAULT_VALUE));

        Label label = new Label("Selected Sarf File:");
        TextField sarfxField = new TextField(getSafeFilePath(tabInfo.getSarfxFile()));
        sarfxField.setPrefColumnCount(30);
        sarfxField.setDisable(true);
        label.setLabelFor(sarfxField);
        Button button = new Button(" ... ");
        button.setOnAction(event -> {
            File file = FILE_CHOOSER.showSaveDialog(getOwner());
            if (file != null) {
                File sarfxFile = TemplateReader.getSarfxFile(file);
                sarfxField.setText(sarfxFile.getAbsolutePath());
            }
        });
        gridPane.add(label, 0, 0);
        gridPane.add(sarfxField, 1, 0);
        gridPane.add(button, 2, 0);

        label = new Label("Selected Word File:");
        TextField textField = new TextField(getSafeFilePath(tabInfo.getDocxFile()));
        textField.setPrefColumnCount(30);
        textField.setDisable(true);
        sarfxField.textProperty().addListener((o, ov, nv) -> {
            if (!isBlank(nv)) {
                File sarfxFile = TemplateReader.getSarfxFile(new File(nv));
                this.sarfxFile.setValue(sarfxFile.getAbsolutePath());
                tabInfo.setSarfxFile(sarfxFile);
                File docxFile = TemplateReader.getDocxFile(sarfxFile);
                tabInfo.setDocxFile(docxFile);
                textField.setText(docxFile.getAbsolutePath());
            }
        });

        label.setLabelFor(textField);
        gridPane.add(label, 0, 1);
        gridPane.add(textField, 1, 1);

        getDialogPane().setContent(gridPane);
    }

    private String getSafeFilePath(File file) {
        return file == null ? "" : file.getAbsolutePath();
    }

    private ObjectProperty<TabInfo> tabInfoProperty() {
        return tabInfo;
    }

    public final void setTabInfo(TabInfo tabInfo) {
        this.tabInfo.set(tabInfo);
    }

}
