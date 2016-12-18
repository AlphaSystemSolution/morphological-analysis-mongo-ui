package com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.accessor;

import com.alphasystem.morphologicalanalysis.graph.model.*;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.*;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.PropertyAccessor;

/**
 * @author sali
 */
public final class GraphNodePropertyAccessors {

    public static class XPropertyAccessor<N extends GraphNode, A extends GraphNodeAdapter<N>> extends PropertyAccessor<N, A> {
        public XPropertyAccessor(A node) {
            super(GraphNodeAdapter::getX, GraphNodeAdapter::setX, node);
        }
    }

    public static class YPropertyAccessor<N extends GraphNode, A extends GraphNodeAdapter<N>> extends PropertyAccessor<N, A> {
        public YPropertyAccessor(A node) {
            super(GraphNodeAdapter::getY, GraphNodeAdapter::setY, node);
        }
    }

    public static class TranslateXPropertyAccessor<N extends GraphNode, A extends GraphNodeAdapter<N>> extends PropertyAccessor<N, A> {
        public TranslateXPropertyAccessor(A node) {
            super(GraphNodeAdapter::getTranslateX, GraphNodeAdapter::setTranslateX, node);
        }
    }

    public static class TranslateYPropertyAccessor<N extends GraphNode, A extends GraphNodeAdapter<N>> extends PropertyAccessor<N, A> {
        public TranslateYPropertyAccessor(A node) {
            super(GraphNodeAdapter::getTranslateY, GraphNodeAdapter::setTranslateY, node);
        }
    }

    public static class TranslationXPropertyAccessor<N extends TerminalNode, A extends GraphNodeAdapter<N>> extends PropertyAccessor<N, A> {
        public TranslationXPropertyAccessor(A node) {
            super(node1 -> ((TerminalNodeAdapter) node1).getTranslationX(), (node12, value) -> ((TerminalNodeAdapter) node12).setTranslationX(value), node);
        }
    }

    public static class TranslationYPropertyAccessor<N extends TerminalNode, A extends GraphNodeAdapter<N>> extends PropertyAccessor<N, A> {
        public TranslationYPropertyAccessor(A node) {
            super(tn -> ((TerminalNodeAdapter) tn).getTranslationY(), (tn, value) -> ((TerminalNodeAdapter) tn).setTranslationY(value), node);
        }
    }

    public static class X1PropertyAccessor<N extends LineSupport, A extends LineSupportAdapter<N>> extends PropertyAccessor<N, A> {
        public X1PropertyAccessor(A node) {
            super(LineSupportAdapter::getX1, LineSupportAdapter::setX1, node);
        }
    }

    public static class Y1PropertyAccessor<N extends LineSupport, A extends LineSupportAdapter<N>> extends PropertyAccessor<N, A> {
        public Y1PropertyAccessor(A node) {
            super(LineSupportAdapter::getY1, LineSupportAdapter::setY1, node);
        }
    }

    public static class X2PropertyAccessor<N extends LineSupport, A extends LineSupportAdapter<N>> extends PropertyAccessor<N, A> {
        public X2PropertyAccessor(A node) {
            super(LineSupportAdapter::getX2, LineSupportAdapter::setX2, node);
        }
    }

    public static class Y2PropertyAccessor<N extends LineSupport, A extends LineSupportAdapter<N>> extends PropertyAccessor<N, A> {
        public Y2PropertyAccessor(A node) {
            super(LineSupportAdapter::getY2, LineSupportAdapter::setY2, node);
        }
    }

    public static class ControlX1PropertyAccessor extends PropertyAccessor<RelationshipNode, RelationshipNodeAdapter> {
        public ControlX1PropertyAccessor(RelationshipNodeAdapter node) {
            super(RelationshipNodeAdapter::getControlX1, RelationshipNodeAdapter::setControlX1, node);
        }
    }

    public static class ControlX2PropertyAccessor extends PropertyAccessor<RelationshipNode, RelationshipNodeAdapter> {
        public ControlX2PropertyAccessor(RelationshipNodeAdapter node) {
            super(RelationshipNodeAdapter::getControlX2, RelationshipNodeAdapter::setControlX2, node);
        }
    }

    public static class ControlY1PropertyAccessor extends PropertyAccessor<RelationshipNode, RelationshipNodeAdapter> {
        public ControlY1PropertyAccessor(RelationshipNodeAdapter node) {
            super(RelationshipNodeAdapter::getControlY1, RelationshipNodeAdapter::setControlY1, node);
        }
    }

    public static class ControlY2PropertyAccessor extends PropertyAccessor<RelationshipNode, RelationshipNodeAdapter> {
        public ControlY2PropertyAccessor(RelationshipNodeAdapter node) {
            super(RelationshipNodeAdapter::getControlY2, RelationshipNodeAdapter::setControlY2, node);
        }
    }

    public static class T1PropertyAccessor extends PropertyAccessor<RelationshipNode, RelationshipNodeAdapter> {
        public T1PropertyAccessor(RelationshipNodeAdapter node) {
            super(RelationshipNodeAdapter::getT1, RelationshipNodeAdapter::setT1, node);
        }
    }

    public static class T2PropertyAccessor extends PropertyAccessor<RelationshipNode, RelationshipNodeAdapter> {
        public T2PropertyAccessor(RelationshipNodeAdapter node) {
            super(RelationshipNodeAdapter::getT2, RelationshipNodeAdapter::setT2, node);
        }
    }

    public static class CxPropertyAccessor<N extends LinkSupport, A extends LinkSupportAdapter<N>> extends PropertyAccessor<N, A> {
        public CxPropertyAccessor(A node) {
            super(LinkSupportAdapter::getCx, LinkSupportAdapter::setCx, node);
        }
    }

    public static class CyPropertyAccessor<N extends LinkSupport, A extends LinkSupportAdapter<N>> extends PropertyAccessor<N, A> {
        public CyPropertyAccessor(A node) {
            super(LinkSupportAdapter::getCy, LinkSupportAdapter::setCy, node);
        }
    }
}
