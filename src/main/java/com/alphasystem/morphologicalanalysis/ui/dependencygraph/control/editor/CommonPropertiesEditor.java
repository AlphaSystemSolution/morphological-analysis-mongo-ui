package com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor;

import com.alphasystem.morphologicalanalysis.graph.model.GraphNode;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin.CommonPropertiesEditorSkin;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.GraphNodeAdapter;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.PropertyAccessor;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.accessor.GraphNodePropertyAccessors.XPropertyAccessor;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.accessor.GraphNodePropertyAccessors.YPropertyAccessor;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Skin;
import javafx.scene.text.Font;

/**
 * @author sali
 */
public class CommonPropertiesEditor<N extends GraphNode, A extends GraphNodeAdapter<N>> extends PropertiesEditor<N, A> {

    private final StringProperty text = new SimpleStringProperty(null, "text");
    private final ObjectProperty<PropertyAccessor<N, A>> x = new SimpleObjectProperty<>(null, "x", new XPropertyAccessor<>(null));
    private final ObjectProperty<PropertyAccessor<N, A>> y = new SimpleObjectProperty<>(null, "y", new YPropertyAccessor<>(null));
    private final ObjectProperty<Font> arabicFont = new SimpleObjectProperty<>(null, "arabicFont");

    public CommonPropertiesEditor() {
        super();
        textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                getNode().setText(newValue);
            }
        });
        arabicFontProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            getNode().setFont(newValue);
        });
        setSkin(new CommonPropertiesEditorSkin<>(this));
    }

    @Override
    protected void setValues(A node) {
        setText((node == null) ? "" : node.getText());
        setX(new XPropertyAccessor<>(node));
        setY(new YPropertyAccessor<>(node));
        setArabicFont((node == null) ? null : node.getFont());
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

    public final PropertyAccessor getY() {
        return y.get();
    }

    public final ObjectProperty<PropertyAccessor<N, A>> yProperty() {
        return y;
    }

    public final void setY(PropertyAccessor<N, A> y) {
        this.y.set(y);
    }

    public final Font getArabicFont() {
        return arabicFont.get();
    }

    public final ObjectProperty<Font> arabicFontProperty() {
        return arabicFont;
    }

    public final void setArabicFont(Font arabicFont) {
        this.arabicFont.set(arabicFont);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new CommonPropertiesEditorSkin<>(this);
    }

}
