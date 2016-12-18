package com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor;

import com.alphasystem.morphologicalanalysis.graph.model.TerminalNode;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin.TranslationPropertiesEditorSkin;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.GraphNodeAdapter;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.TerminalNodeAdapter;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.PropertyAccessor;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.accessor.GraphNodePropertyAccessors.TranslationXPropertyAccessor;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.accessor.GraphNodePropertyAccessors.TranslationYPropertyAccessor;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Skin;
import javafx.scene.text.Font;

/**
 * @author sali
 */
public class TranslationPropertiesEditor<N extends TerminalNode, A extends GraphNodeAdapter<N>>
        extends PropertiesEditor<N, A> {

    private final StringProperty text = new SimpleStringProperty(null, "translationText");
    private final ObjectProperty<PropertyAccessor<N, A>> x = new SimpleObjectProperty<>(null, "translationX", new TranslationXPropertyAccessor<>(null));
    private final ObjectProperty<PropertyAccessor<N, A>> y = new SimpleObjectProperty<>(null, "translationY", new TranslationYPropertyAccessor<>(null));
    private final ObjectProperty<Font> translationFont = new SimpleObjectProperty<>(null, "translationFont");


    public TranslationPropertiesEditor() {
        super();
        textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                ((TerminalNodeAdapter) getNode()).setTranslationText(newValue);
            }
        });
        translationFontProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            ((TerminalNodeAdapter) getNode()).setTranslationFont(newValue);
        });
        setSkin(new TranslationPropertiesEditorSkin<>(this));
    }

    @Override
    protected void setValues(A node) {
        TerminalNodeAdapter adapter = (TerminalNodeAdapter) node;
        setText((adapter == null) ? "" : adapter.getTranslationText());
        setX(new TranslationXPropertyAccessor<>(node));
        setY(new TranslationYPropertyAccessor<>(node));
        setTranslationFont((adapter == null) ? null : adapter.getTranslationFont());
    }

    public final String getText() {
        return text.get();
    }

    public final StringProperty textProperty() {
        return text;
    }

    public final void setText(String text) {
        this.text.set(text);
    }

    public final PropertyAccessor<N, A> getX() {
        return x.get();
    }

    public final ObjectProperty<PropertyAccessor<N, A>> xProperty() {
        return x;
    }

    public final void setX(PropertyAccessor<N, A> x) {
        this.x.set(x);
    }

    public final PropertyAccessor<N, A> getY() {
        return y.get();
    }

    public final ObjectProperty<PropertyAccessor<N, A>> yProperty() {
        return y;
    }

    public final void setY(PropertyAccessor<N, A> y) {
        this.y.set(y);
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

    @Override
    protected Skin<?> createDefaultSkin() {
        return new TranslationPropertiesEditorSkin<>(this);
    }
}
