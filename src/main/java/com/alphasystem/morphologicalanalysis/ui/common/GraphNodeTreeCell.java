package com.alphasystem.morphologicalanalysis.ui.common;

import com.alphasystem.morphologicalanalysis.graph.model.GraphNode;
import com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType;
import javafx.scene.control.TreeCell;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import static com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType.ROOT;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.ARABIC_FONT_SMALL;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.ENGLISH_FONT_SMALL;
import static javafx.scene.control.ContentDisplay.GRAPHIC_ONLY;

/**
 * @author sali
 */
public class GraphNodeTreeCell extends TreeCell<GraphNode> {

    private final Text label;

    public GraphNodeTreeCell() {
        label = new Text();
        setContentDisplay(GRAPHIC_ONLY);
    }

    @Override
    protected void updateItem(GraphNode item, boolean empty) {
        super.updateItem(item, empty);

        if(item != null && !empty){
            GraphNodeType nodeType = item.getGraphNodeType();
            label.setText(item.toString());
            Font font = nodeType.equals(ROOT) ? ENGLISH_FONT_SMALL : ARABIC_FONT_SMALL;
            label.setFont(font);
            setGraphic(label);
        }

    }
}
