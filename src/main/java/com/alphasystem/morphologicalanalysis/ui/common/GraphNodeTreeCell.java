package com.alphasystem.morphologicalanalysis.ui.common;

import com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.*;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.CanvasUtil;
import com.alphasystem.morphologicalanalysis.util.MorphologicalAnalysisPreferences;
import com.alphasystem.util.GenericPreferences;
import javafx.scene.control.TreeCell;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import static com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType.ROOT;
import static java.lang.String.format;
import static javafx.scene.control.ContentDisplay.GRAPHIC_ONLY;

/**
 * @author sali
 */
public class GraphNodeTreeCell extends TreeCell<GraphNodeAdapter> {

    private final Text label;
    private final CanvasUtil canvasUtil = CanvasUtil.getInstance();
    private final MorphologicalAnalysisPreferences preferences = GenericPreferences.getInstance(MorphologicalAnalysisPreferences.class);

    public GraphNodeTreeCell() {
        label = new Text();
        setContentDisplay(GRAPHIC_ONLY);
    }

    @Override
    protected void updateItem(GraphNodeAdapter item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null && !empty) {
            GraphNodeType nodeType = item.getGraphNodeType();
            StringBuilder builder = new StringBuilder();
            String text = item.getText();
            switch (nodeType) {
                case PART_OF_SPEECH:
                    PartOfSpeechNodeAdapter posna = (PartOfSpeechNodeAdapter) item;
                    TerminalNodeAdapter parent = (TerminalNodeAdapter) posna.getParent();
                    builder.append(canvasUtil.getLocationText(parent, posna)).append(" ").append(getText(item));
                    break;
                case PHRASE:
                    PhraseNodeAdapter pna = (PhraseNodeAdapter) item;
                    builder.append(canvasUtil.getPhraseText(pna.getFragments())).append(" ").append(getText(item));
                    break;
                case RELATIONSHIP:
                    RelationshipNodeAdapter rna = (RelationshipNodeAdapter) item;
                    builder.append(canvasUtil.getRelationshipText(rna)).append(getText(item));
                    break;
                default:
                    builder.append(text);
                    break;
            }
            label.setText(builder.toString());
            Font font = nodeType.equals(ROOT) ? preferences.getEnglishFont14() : preferences.getArabicFont24();
            label.setFont(font);
            setGraphic(label);
        }

    }

    private String getText(GraphNodeAdapter item) {
        return format("(%s)", item.getText());
    }
}
