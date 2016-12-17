package com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor;

import com.alphasystem.morphologicalanalysis.graph.model.TerminalNode;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin.GroupTranslateEditorSkin;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.GraphNodeAdapter;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.PropertyAccessor;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.accessor.GraphNodePropertyAccessors.TranslateXPropertyAccessor;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.accessor.GraphNodePropertyAccessors.TranslateYPropertyAccessor;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Skin;

/**
 * @author sali
 */
public class GroupTranslateEditor<N extends TerminalNode, A extends GraphNodeAdapter<N>> extends PropertiesEditor<N, A> {

    private ObjectProperty<PropertyAccessor<N, A>> x;
    private ObjectProperty<PropertyAccessor<N, A>> y;

    @Override
    protected void initialize(A node) {
        if (x == null) {
            x = new SimpleObjectProperty<>(null, "translationX");
        }
        if (y == null) {
            y = new SimpleObjectProperty<>(null, "translationY");
        }
        setX(new TranslateXPropertyAccessor<>(node));
        setY(new TranslateYPropertyAccessor<>(node));
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

    @Override
    protected void initSkin() {
        setSkin(new GroupTranslateEditorSkin<>(this));
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new GroupTranslateEditorSkin<>(this);
    }
}
