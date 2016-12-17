package com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor;

import com.alphasystem.morphologicalanalysis.graph.model.LineSupport;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin.LinePropertiesEditorSkin;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.LineSupportAdapter;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.PropertyAccessor;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.accessor.GraphNodePropertyAccessors.X1PropertyAccessor;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.accessor.GraphNodePropertyAccessors.X2PropertyAccessor;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.accessor.GraphNodePropertyAccessors.Y1PropertyAccessor;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.accessor.GraphNodePropertyAccessors.Y2PropertyAccessor;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Skin;

/**
 * @author sali
 */
public class LinePropertiesEditor<N extends LineSupport, A extends LineSupportAdapter<N>> extends PropertiesEditor<N, A> {

    private ObjectProperty<PropertyAccessor<N, A>> x1;
    private ObjectProperty<PropertyAccessor<N, A>> y1;
    private ObjectProperty<PropertyAccessor<N, A>> x2;
    private ObjectProperty<PropertyAccessor<N, A>> y2;

    @Override
    protected void initialize(A node) {
        if (x1 == null) {
            x1 = new SimpleObjectProperty<>(null, "x1");
        }
        if (y1 == null) {
            y1 = new SimpleObjectProperty<>(null, "y1");
        }
        if (x2 == null) {
            x2 = new SimpleObjectProperty<>(null, "x2");
        }
        if (y2 == null) {
            y2 = new SimpleObjectProperty<>(null, "y2");
        }
        setX1(new X1PropertyAccessor<>(node));
        setY1(new Y1PropertyAccessor<>(node));
        setX2(new X2PropertyAccessor<>(node));
        setY2(new Y2PropertyAccessor<>(node));
    }

    @Override
    protected void initSkin() {
        setSkin(new LinePropertiesEditorSkin(this));
    }

    public final PropertyAccessor<N, A> getX1() {
        return x1.get();
    }

    public final ObjectProperty<PropertyAccessor<N, A>> x1Property() {
        return x1;
    }

    public final void setX1(PropertyAccessor<N, A> x1) {
        this.x1.set(x1);
    }

    public final PropertyAccessor<N, A> getY1() {
        return y1.get();
    }

    public final ObjectProperty<PropertyAccessor<N, A>> y1Property() {
        return y1;
    }

    public final void setY1(PropertyAccessor<N, A> y1) {
        this.y1.set(y1);
    }

    public final PropertyAccessor<N, A> getX2() {
        return x2.get();
    }

    public final ObjectProperty<PropertyAccessor<N, A>> x2Property() {
        return x2;
    }

    public final void setX2(PropertyAccessor<N, A> x2) {
        this.x2.set(x2);
    }

    public final PropertyAccessor<N, A> getY2() {
        return y2.get();
    }

    public final ObjectProperty<PropertyAccessor<N, A>> y2Property() {
        return y2;
    }

    public final void setY2(PropertyAccessor<N, A> y2) {
        this.y2.set(y2);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new LinePropertiesEditorSkin(this);
    }
}
