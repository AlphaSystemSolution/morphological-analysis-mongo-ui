package com.alphasystem.morphologicalanalysis.ui.wordbyword.control.skin;

import com.alphasystem.arabic.ui.Browser;
import com.alphasystem.arabic.ui.keyboard.KeyboardView;
import com.alphasystem.morphologicalanalysis.morphology.model.DictionaryNotes;
import com.alphasystem.morphologicalanalysis.morphology.model.RootLetters;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.DictionaryNotesView;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.model.AsciiDocStyle;
import de.jensd.fx.glyphs.GlyphIcons;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.AttributesBuilder;
import org.asciidoctor.Options;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static com.alphasystem.arabic.model.ArabicWord.fromBuckWalterString;
import static com.alphasystem.arabic.ui.util.UiUtilities.*;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.*;
import static com.alphasystem.util.AppUtil.NEW_LINE;
import static de.jensd.fx.glyphs.GlyphsDude.setIcon;
import static de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.*;
import static java.lang.String.format;
import static java.nio.file.Files.write;
import static java.nio.file.Paths.get;
import static java.util.Collections.addAll;
import static javafx.application.Platform.runLater;
import static javafx.collections.FXCollections.observableArrayList;
import static javafx.geometry.Side.BOTTOM;
import static javafx.scene.control.TabPane.TabClosingPolicy.UNAVAILABLE;
import static javafx.scene.text.FontPosture.REGULAR;
import static javafx.scene.text.FontWeight.NORMAL;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author sali
 */
public class DictionaryNotesSkin extends SkinBase<DictionaryNotesView> {

    private static final Set<AsciiDocStyle> ASCII_DOC_STYLE_SET = new LinkedHashSet<>();
    private static final String ASCII_DOCTOR_RESOURCE_PATH = "asciidoctor";
    private static final String CSS_RESOURCE_PATH = "morphology-min.css";

    static {
        try {
            write(get(DEFAULT_CSS_DIRECTORY.getAbsolutePath(), CSS_RESOURCE_PATH),
                    readAllLines(format("%s.%s", ASCII_DOCTOR_RESOURCE_PATH, CSS_RESOURCE_PATH)));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        addAll(ASCII_DOC_STYLE_SET,
                new AsciiDocStyle("no-style", "Apply Style", "", ""),
                new AsciiDocStyle("arabic-normal", "Arabic Normal", "[%s]#", "#"),
                new AsciiDocStyle("square", "Square", "[%s", "]"));
    }

    private final Asciidoctor asciidoctor = Asciidoctor.Factory.create();
    private final Popup keyboardPopup;
    private final KeyboardView keyboardView;
    private final TextArea editor = new TextArea();
    private final Browser preview = new Browser();
    private final TabPane tabPane = new TabPane();

    public DictionaryNotesSkin(DictionaryNotesView control) {
        super(control);

        keyboardView = new KeyboardView();
        keyboardView.setTarget(editor);
        keyboardView.setOnMouseClicked(event -> keyboardView.requestFocus());
        keyboardPopup = new Popup();
        keyboardPopup.setAutoHide(true);
        keyboardPopup.setHideOnEscape(true);
        keyboardPopup.getContent().add(keyboardView);

        initializeSkin();
        getSkinnable().dictionaryNotesProperty().addListener((o, ov, nv) -> populateEditor(nv));
        populateEditor(getSkinnable().getDictionaryNotes());
    }

    private static String formatText(String source, String prefix, String suffix) {
        return format("%s%s%s", prefix, source, suffix);
    }

    public void selectSource() {
        tabPane.getSelectionModel().select(0);
    }

    private void initializeSkin() {
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(createToolBar());

        editor.setFont(Font.font("Courier New", NORMAL, REGULAR, 16.0));
        borderPane.setCenter(new StackPane(wrapInScrollPane(editor)));

        tabPane.setTabClosingPolicy(UNAVAILABLE);
        tabPane.setSide(BOTTOM);
        Tab previewTab = new Tab("Preview", preview);
        previewTab.disableProperty().bind(getSkinnable().disabledProperty());
        previewTab.selectedProperty().addListener((o, ov, nv) -> saveAction());
        tabPane.getTabs().addAll(new Tab("Source", borderPane), previewTab);

        getChildren().add(tabPane);
    }

    private ToolBar createToolBar() {
        ToolBar toolBar = new ToolBar();

        Button saveButton = createButton(SAVE, "Save Notes", event -> saveAction());
        saveButton.disableProperty().bind(getSkinnable().disabledProperty());
        Button keyboardButton = createButton(KEYBOARD_ALT, "Show Keyboard", this::showKeyboard);
        toolBar.getItems().addAll(saveButton,
                createButton(BOLD, "Bold", "*", "*"), createButton(ITALIC, "Italic", "_", "_"),
                createButton(UNDERLINE, "Underline", "[underline]#", "#"),
                createButton(STRIKETHROUGH, "Strike through", "[line-through]#", "#"),
                createButton(SUBSCRIPT, "Subscript", "~", "~"),
                createButton(SUPERSCRIPT, "Superscript", "^", "^"),
                createButton(HEADER, "Heading", event -> insertHeading()),
                new Separator(), createStyleComboBox(), keyboardButton);

        return toolBar;
    }

    private ComboBox<AsciiDocStyle> createStyleComboBox() {
        ObservableList<AsciiDocStyle> items = observableArrayList(ASCII_DOC_STYLE_SET);
        final ComboBox<AsciiDocStyle> comboBox = new ComboBox<>(items);
        comboBox.valueProperty().addListener((o, ov, nv) -> {
            String name = nv.getName();
            if (!"no-style".equals(name)) {
                applyStyle(nv);
            }
            runLater(() -> comboBox.getSelectionModel().select(0));
        });
        comboBox.getSelectionModel().select(0);
        return comboBox;
    }

    private void applyStyle(AsciiDocStyle style) {
        String selectedText = editor.getSelectedText();
        String finalText = selectedText;
        boolean noStyle = isBlank(selectedText) || !selectedText.startsWith("[");
        if (noStyle) {
            // if there is no previous style then add style
            finalText = formatText(editor.getSelectedText(), format(style.getFormatStart(), style.getName()),
                    style.getFormatEnd());
        } else {
            // check whether the new style is already in the current list
            int openSquareBracket = selectedText.indexOf('[');
            int closeSquareBracket = selectedText.indexOf(']');
            String currentStyles = selectedText.substring(openSquareBracket + 1, closeSquareBracket);
            String rest = selectedText.substring(closeSquareBracket + 1);
            if (!currentStyles.contains(style.getName())) {
                currentStyles = format("%s,%s", currentStyles, style);
            }
            finalText = format("[%s]%s", currentStyles, rest);
        }
        editor.replaceSelection(finalText);
    }

    private void showKeyboard(ActionEvent event) {
        if (keyboardPopup.isShowing()) {
            keyboardPopup.hide();
        } else {
            Button button = (Button) event.getSource();
            final Bounds bounds = button.localToScreen(button.getBoundsInLocal());
            keyboardPopup.show(button, bounds.getMinX(), bounds.getMinY() + bounds.getHeight());
        }
    }

    private void saveAction() {
        Service<Void> saveService = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        waitCursor(getSkinnable());
                        convertDoc();
                        return null;
                    }
                };
            }
        };
        saveService.setOnFailed(event -> {
            defaultCursor(getSkinnable());
            preview.setDisable(true);
            Throwable exception = event.getSource().getException();
            exception.printStackTrace();
        });
        saveService.setOnSucceeded(event -> {
            defaultCursor(getSkinnable());
            preview.setDisable(false);
            loadPreview();
        });
        saveService.start();
    }

    private void loadPreview() {
        String location = preview.getWebEngine().getLocation();
        if (location == null) {
            File file = new File(DEFAULT_DICTIONARY_DIRECTORY, getSkinnable().getPreviewFileName());
            try {
                preview.loadUrl(file.toURI().toURL().toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else {
            preview.getWebEngine().reload();
        }
    }

    private void insertHeading() {
        String text = editor.getText();
        int i = text.lastIndexOf('\n');
        String currentLine = text.substring(i + 1);
        String insert = currentLine.startsWith("=") ? "=" : "= ";
        editor.insertText(i + 1, insert);
    }

    private Button createButton(GlyphIcons icon, String tooltip, EventHandler<ActionEvent> action) {
        Button button = new Button();
        button.setTooltip(new Tooltip(tooltip));
        setIcon(button, icon);
        button.setOnAction(action);
        return button;
    }

    private Button createButton(GlyphIcons icon, String tooltip, String prefix, String suffix) {
        return createButton(icon, tooltip, event -> doAction(prefix, suffix));
    }

    private void doAction(String prefix, String suffix) {
        editor.replaceSelection(formatText(editor.getSelectedText(), prefix, suffix));
    }

    private void populateEditor(DictionaryNotes dictionaryNotes) {
        if (dictionaryNotes == null || dictionaryNotes.isEmpty()) {
            return;
        }
        boolean doSave = true;
        String notes = dictionaryNotes.getNotes();
        if (isBlank(notes)) {
            try {
                List<String> lines = readAllLines(format("%s.%s", ASCII_DOCTOR_RESOURCE_PATH, "template.adoc"));
                StringBuilder builder = new StringBuilder();
                builder.append(lines.get(0));
                for (int i = 1; i < lines.size(); i++) {
                    builder.append(NEW_LINE).append(lines.get(i));
                }
                String text = builder.toString();
                RootLetters rootLetters = dictionaryNotes.getRootLetters();
                String displayName = rootLetters.getDisplayName();
                String replacement = fromBuckWalterString(displayName).toUnicode();
                notes = text.replace("${ArticleName}", replacement);
                dictionaryNotes.setNotes(notes);
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
                doSave = false;
            }
        }
        if (doSave) {
            editor.setText(notes);
            saveAction();
        }
    }

    private void convertDoc() throws Exception {
        Options options = new Options();
        options.setBaseDir(DEFAULT_DICTIONARY_DIRECTORY.getAbsolutePath());
        options.setToFile(getSkinnable().getPreviewFileName());
        AttributesBuilder attributesBuilder = AttributesBuilder.attributes().stylesDir(DEFAULT_CSS_DIRECTORY.getName())
                .styleSheetName(CSS_RESOURCE_PATH).linkCss(true);
        options.setAttributes(attributesBuilder.get());
        try {
            asciidoctor.convert(editor.getText(), options);
        } catch (Exception e) {
            throw e;
        }
    }

}
