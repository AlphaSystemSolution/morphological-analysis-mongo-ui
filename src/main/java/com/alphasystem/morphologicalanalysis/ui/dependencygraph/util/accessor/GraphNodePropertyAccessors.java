package com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.accessor;

import com.alphasystem.morphologicalanalysis.graph.model.GraphNode;
import com.alphasystem.morphologicalanalysis.graph.model.LineSupport;
import com.alphasystem.morphologicalanalysis.graph.model.LinkSupport;
import com.alphasystem.morphologicalanalysis.graph.model.TerminalNode;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.GraphNodeAdapter;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.LineSupportAdapter;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.LinkSupportAdapter;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.TerminalNodeAdapter;
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

    public static class ControlX1PropertyAccessor<N extends LinkSupport, A extends LinkSupportAdapter<N>> extends PropertyAccessor<N, A> {
        public ControlX1PropertyAccessor(A node) {
            super(LinkSupportAdapter::getX1, LinkSupportAdapter::setX1, node);
        }
    }

    public static class ControlX2PropertyAccessor<N extends LinkSupport, A extends LinkSupportAdapter<N>> extends PropertyAccessor<N, A> {
        public ControlX2PropertyAccessor(A node) {
            super(LinkSupportAdapter::getX2, LinkSupportAdapter::setX2, node);
        }
    }

    public static class ControlY1PropertyAccessor<N extends LinkSupport, A extends LinkSupportAdapter<N>> extends PropertyAccessor<N, A> {
        public ControlY1PropertyAccessor(A node) {
            super(LinkSupportAdapter::getY1, LinkSupportAdapter::setY1, node);
        }
    }

    public static class ControlY2PropertyAccessor<N extends LinkSupport, A extends LinkSupportAdapter<N>> extends PropertyAccessor<N, A> {
        public ControlY2PropertyAccessor(A node) {
            super(LinkSupportAdapter::getY2, LinkSupportAdapter::setY2, node);
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
