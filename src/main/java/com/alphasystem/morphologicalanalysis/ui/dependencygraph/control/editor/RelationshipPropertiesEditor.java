package com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor;

import com.alphasystem.morphologicalanalysis.graph.model.RelationshipNode;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin.view.RelationshipPropertiesEditorSkinView;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.RelationshipNodeAdapter;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.accessor.GraphNodePropertyAccessors.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;

/**
 * @author sali
 */
public class RelationshipPropertiesEditor extends PropertiesEditor<RelationshipNode, RelationshipNodeAdapter> {

    private final ObjectProperty<ControlX1PropertyAccessor> x1 = new SimpleObjectProperty<>(null, "controlX1", new ControlX1PropertyAccessor(null));
    private final ObjectProperty<ControlY1PropertyAccessor> y1 = new SimpleObjectProperty<>(null, "controlY1", new ControlY1PropertyAccessor(null));
    private final ObjectProperty<ControlX2PropertyAccessor> x2 = new SimpleObjectProperty<>(null, "controlX2", new ControlX2PropertyAccessor(null));
    private final ObjectProperty<ControlY2PropertyAccessor> y2 = new SimpleObjectProperty<>(null, "controlY2", new ControlY2PropertyAccessor(null));
    private final ObjectProperty<T1PropertyAccessor> t1 = new SimpleObjectProperty<>(null, "t1", new T1PropertyAccessor(null));
    private final ObjectProperty<T2PropertyAccessor> t2 = new SimpleObjectProperty<>(null, "t2", new T2PropertyAccessor(null));

    public RelationshipPropertiesEditor() {
        super();
    }

    @Override
    protected void updateBounds(RelationshipNodeAdapter node) {
        setLowerXBound(node.getControlX1());
        setUpperXBound(node.getControlX2());
    }

    @Override
    protected void setValues(RelationshipNodeAdapter node) {
        super.setValues(node);
        setX1(new ControlX1PropertyAccessor(node));
        setX2(new ControlX2PropertyAccessor(node));
        setY1(new ControlY1PropertyAccessor(node));
        setY2(new ControlY2PropertyAccessor(node));
        setT1(new T1PropertyAccessor(node));
        setT2(new T2PropertyAccessor(node));
    }

    public final ControlX1PropertyAccessor getX1() {
        return x1.get();
    }

    public final ObjectProperty<ControlX1PropertyAccessor> x1Property() {
        return x1;
    }

    public final void setX1(ControlX1PropertyAccessor x1) {
        this.x1.set(x1);
    }

    public final ControlY1PropertyAccessor getY1() {
        return y1.get();
    }

    public final ObjectProperty<ControlY1PropertyAccessor> y1Property() {
        return y1;
    }

    public final void setY1(ControlY1PropertyAccessor y1) {
        this.y1.set(y1);
    }

    public final ControlX2PropertyAccessor getX2() {
        return x2.get();
    }

    public final ObjectProperty<ControlX2PropertyAccessor> x2Property() {
        return x2;
    }

    public final void setX2(ControlX2PropertyAccessor x2) {
        this.x2.set(x2);
    }

    public final ControlY2PropertyAccessor getY2() {
        return y2.get();
    }

    public final ObjectProperty<ControlY2PropertyAccessor> y2Property() {
        return y2;
    }

    public final void setY2(ControlY2PropertyAccessor y2) {
        this.y2.set(y2);
    }

    public final T1PropertyAccessor getT1() {
        return t1.get();
    }

    public final ObjectProperty<T1PropertyAccessor> t1Property() {
        return t1;
    }

    public final void setT1(T1PropertyAccessor t1) {
        this.t1.set(t1);
    }

    public final T2PropertyAccessor getT2() {
        return t2.get();
    }

    public final ObjectProperty<T2PropertyAccessor> t2Property() {
        return t2;
    }

    public final void setT2(T2PropertyAccessor t2) {
        this.t2.set(t2);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new DefaultSkin(this);
    }

    private class DefaultSkin extends SkinBase<RelationshipPropertiesEditor> {

        /**
         * Constructor for all SkinBase instances.
         *
         * @param control The control for which this Skin should attach to.
         */
        DefaultSkin(RelationshipPropertiesEditor control) {
            super(control);
            getChildren().setAll(new RelationshipPropertiesEditorSkinView(control));
        }
    }
}
