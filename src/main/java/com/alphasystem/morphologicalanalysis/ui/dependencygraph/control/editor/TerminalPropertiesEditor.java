package com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor;

import com.alphasystem.morphologicalanalysis.graph.model.TerminalNode;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin.view.TerminalPropertiesEditorSkinView;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.TerminalNodeAdapter;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.accessor.GraphNodePropertyAccessors.TranslateXPropertyAccessor;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.accessor.GraphNodePropertyAccessors.TranslateYPropertyAccessor;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.accessor.GraphNodePropertyAccessors.TranslationXPropertyAccessor;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.accessor.GraphNodePropertyAccessors.TranslationYPropertyAccessor;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;
import javafx.scene.text.Font;

/**
 * @author sali
 */
public class TerminalPropertiesEditor extends LineSupportPropertiesEditor<TerminalNode, TerminalNodeAdapter> {

    private final StringProperty translationText = new SimpleStringProperty(null, "translationText");
    private final ObjectProperty<TranslationXPropertyAccessor<TerminalNode, TerminalNodeAdapter>> translationX = new SimpleObjectProperty<>(null, "translationX", new TranslationXPropertyAccessor<>(null));
    private final ObjectProperty<TranslationYPropertyAccessor<TerminalNode, TerminalNodeAdapter>> translationY = new SimpleObjectProperty<>(null, "translationY", new TranslationYPropertyAccessor<>(null));
    private final ObjectProperty<Font> translationFont = new SimpleObjectProperty<>(null, "translationFont");
    private final ObjectProperty<TranslateXPropertyAccessor<TerminalNode, TerminalNodeAdapter>> groupTranslateX = new SimpleObjectProperty<>(null, "translationX", new TranslateXPropertyAccessor<>(null));
    private final ObjectProperty<TranslateYPropertyAccessor<TerminalNode, TerminalNodeAdapter>> groupTranslateY = new SimpleObjectProperty<>(null, "translationY", new TranslateYPropertyAccessor<>(null));

    public TerminalPropertiesEditor() {
        super();
        translationTextProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                getNode().setTranslationText(newValue);
            }
        });
        translationFontProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            getNode().setTranslationFont(newValue);
        });
    }

    @Override
    protected void setValues(TerminalNodeAdapter node) {
        super.setValues(node);
        setTranslationText((node == null) ? "" : node.getTranslationText());
        setTranslationX(new TranslationXPropertyAccessor<>(node));
        setTranslationY(new TranslationYPropertyAccessor<>(node));
        setTranslationFont((node == null) ? null : node.getTranslationFont());
        setGroupTranslateX(new TranslateXPropertyAccessor<>(node));
        setGroupTranslateY(new TranslateYPropertyAccessor<>(node));
    }

    public final String getTranslationText() {
        return translationText.get();
    }

    public final StringProperty translationTextProperty() {
        return translationText;
    }

    public final void setTranslationText(String translationText) {
        this.translationText.set(translationText);
    }

    public final TranslationXPropertyAccessor<TerminalNode, TerminalNodeAdapter> getTranslationX() {
        return translationX.get();
    }

    public final ObjectProperty<TranslationXPropertyAccessor<TerminalNode, TerminalNodeAdapter>> translationXProperty() {
        return translationX;
    }

    public final void setTranslationX(TranslationXPropertyAccessor<TerminalNode, TerminalNodeAdapter> translationX) {
        this.translationX.set(translationX);
    }

    public final TranslationYPropertyAccessor<TerminalNode, TerminalNodeAdapter> getTranslationY() {
        return translationY.get();
    }

    public final ObjectProperty<TranslationYPropertyAccessor<TerminalNode, TerminalNodeAdapter>> translationYProperty() {
        return translationY;
    }

    public final void setTranslationY(TranslationYPropertyAccessor<TerminalNode, TerminalNodeAdapter> translationY) {
        this.translationY.set(translationY);
    }

    public final Font getTranslationFont() {
        return translationFont.get();
    }

    public final ObjectProperty<Font> translationFontProperty() {
        return translationFont;
    }

    public final void setTranslationFont(Font translationFont) {
        this.translationFont.set(translationFont);
    }

    public final TranslateXPropertyAccessor<TerminalNode, TerminalNodeAdapter> getGroupTranslateX() {
        return groupTranslateX.get();
    }

    public final ObjectProperty<TranslateXPropertyAccessor<TerminalNode, TerminalNodeAdapter>> groupTranslateXProperty() {
        return groupTranslateX;
    }

    public final void setGroupTranslateX(TranslateXPropertyAccessor<TerminalNode, TerminalNodeAdapter> groupTranslateX) {
        this.groupTranslateX.set(groupTranslateX);
    }

    public final TranslateYPropertyAccessor<TerminalNode, TerminalNodeAdapter> getGroupTranslateY() {
        return groupTranslateY.get();
    }

    public final ObjectProperty<TranslateYPropertyAccessor<TerminalNode, TerminalNodeAdapter>> groupTranslateYProperty() {
        return groupTranslateY;
    }

    public final void setGroupTranslateY(TranslateYPropertyAccessor<TerminalNode, TerminalNodeAdapter> groupTranslateY) {
        this.groupTranslateY.set(groupTranslateY);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new DefaultSkin(this);
    }

    private class DefaultSkin extends SkinBase<TerminalPropertiesEditor> {

        /**
         * Constructor for all SkinBase instances.
         *
         * @param control The control for which this Skin should attach to.
         */
        DefaultSkin(TerminalPropertiesEditor control) {
            super(control);
            getChildren().setAll(new TerminalPropertiesEditorSkinView(control));
        }
    }
}
