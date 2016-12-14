package com.alphasystem.morphologicalanalysis.ui.dependencygraph.control;

import com.alphasystem.morphologicalanalysis.graph.model.FontMetaInfo;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.skin.GlobalPropertiesSkin;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.*;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import static com.alphasystem.morphologicalanalysis.ui.common.Global.fromFontMetaInfo;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.getColorValue;
import static com.alphasystem.util.AppUtil.isInstanceOf;

/**
 * @author sali
 */
public class GlobalPropertiesView extends Control {

    private final DoubleProperty canvasWidth = new SimpleDoubleProperty(null, "canvasWidth");
    private final DoubleProperty canvasHeight = new SimpleDoubleProperty(null, "canvasHeight");
    private final BooleanProperty showGridLines = new SimpleBooleanProperty(null, "showGridLines");
    private final BooleanProperty showOutline = new SimpleBooleanProperty(null, "showOutline");
    private final BooleanProperty debugMode = new SimpleBooleanProperty(null, "debugMode");
    private final DoubleProperty alignTerminalYAxis = new SimpleDoubleProperty(null, "alignTerminalYAxis");
    private final DoubleProperty alignTranslationYAxis = new SimpleDoubleProperty(null, "alignTranslationYAxis");
    private final DoubleProperty alignPosYAxis = new SimpleDoubleProperty(null, "alignPosYAxis");
    private final DoubleProperty alignPOSControlYAxis = new SimpleDoubleProperty(null, "alignPOSControlYAxis");
    private final DoubleProperty groupTranslateXAxis = new SimpleDoubleProperty(null, "groupTranslateXAxis");
    private final DoubleProperty groupTranslateYAxis = new SimpleDoubleProperty(null, "groupTranslateYAxis");
    private final ObjectProperty<FontMetaInfo> terminalFont = new SimpleObjectProperty<>(null, "terminalFont");
    private final ObjectProperty<FontMetaInfo> translationFont = new SimpleObjectProperty<>(null, "terminalFont");
    private final ObjectProperty<FontMetaInfo> posFont = new SimpleObjectProperty<>(null, "terminalFont");
    private final ObjectProperty<Paint> backgroundColor = new SimpleObjectProperty<>(null, "backgroundColor");
    private final ObjectProperty<DependencyGraphAdapter> dependencyGraph = new SimpleObjectProperty<>(null, "dependencyGraph");

    public GlobalPropertiesView(DependencyGraphAdapter dependencyGraph) {
        dependencyGraphProperty().addListener((observable, oldValue, newValue) -> initValues(newValue));
        initListeners();
        setSkin(new GlobalPropertiesSkin(this));
        setDependencyGraph(dependencyGraph);
    }

    private void initValues(DependencyGraphAdapter dga) {
        if (dga == null) {
            return;
        }
        final GraphMetaInfoAdapter graphMetaInfo = dga.getGraphMetaInfo();
        if (graphMetaInfo != null) {
            setCanvasWidth(graphMetaInfo.getWidth());
            setCanvasHeight(graphMetaInfo.getHeight());
            setShowGridLines(graphMetaInfo.isShowGridLines());
            setShowOutline(graphMetaInfo.isShowOutLines());
            setDebugMode(graphMetaInfo.isDebugMode());

            TerminalNodeAdapter node = null;
            ObservableList<GraphNodeAdapter> nodes = getDependencyGraph().getGraphNodes();
            if (!nodes.isEmpty()) {
                node = (TerminalNodeAdapter) nodes.get(0);
            }
            if (node == null) {
                node = new TerminalNodeAdapter();
            }

            setAlignTerminalYAxis(node.getY());
            setAlignTranslationYAxis(node.getTranslationY());
            final ObservableList<PartOfSpeechNodeAdapter> partOfSpeeches = node.getPartOfSpeeches();
            if (partOfSpeeches != null && !partOfSpeeches.isEmpty()) {
                final PartOfSpeechNodeAdapter posAdapter = partOfSpeeches.get(0);
                if (posAdapter != null) {
                    setAlignPosYAxis(posAdapter.getY());
                    setAlignPOSControlYAxis(posAdapter.getCy());
                }

            }
            setGroupTranslateXAxis(node.getTranslateX());
            setGroupTranslateYAxis(node.getTranslateY());
            setTerminalFont(graphMetaInfo.getTerminalFont());
            setTranslationFont(graphMetaInfo.getTranslationFont());
            setPosFont(graphMetaInfo.getPosFont());
            setBackgroundColor(Color.web(graphMetaInfo.getBackgroundColor()));
        }
    }

    private void initListeners() {
        canvasWidthProperty().addListener((observable, oldValue, newValue) ->
                getDependencyGraph().getGraphMetaInfo().setWidth((Double) newValue));
        canvasHeightProperty().addListener((observable, oldValue, newValue) ->
                getDependencyGraph().getGraphMetaInfo().setHeight((Double) newValue));
        showGridLinesProperty().addListener((observable, oldValue, newValue) ->
                getDependencyGraph().getGraphMetaInfo().setShowGridLines(newValue));
        showOutlineProperty().addListener((observable, oldValue, newValue) ->
                getDependencyGraph().getGraphMetaInfo().setShowOutLines(newValue));
        debugModeProperty().addListener((observable, oldValue, newValue) ->
                getDependencyGraph().getGraphMetaInfo().setDebugMode(newValue));
        backgroundColorProperty().addListener((observable, oldValue, newValue) ->
                getDependencyGraph().getGraphMetaInfo().setBackgroundColor(getColorValue((Color) newValue)));

        alignTerminalYAxisProperty().addListener((observable, oldValue1, newValue) ->
                getDependencyGraph().getGraphNodes().stream().filter(gn -> isInstanceOf(TerminalNodeAdapter.class, gn))
                        .forEach(graphNodeAdapter -> graphNodeAdapter.setY(newValue.doubleValue())));

        alignTranslationYAxisProperty().addListener((observable, oldValue1, newValue) ->
                getDependencyGraph().getGraphNodes().stream().filter(gn -> isInstanceOf(TerminalNodeAdapter.class, gn))
                        .forEach(graphNodeAdapter -> ((TerminalNodeAdapter) graphNodeAdapter).setTranslationY(newValue.doubleValue())));

        alignPosYAxisProperty().addListener((observable, oldValue, newValue) -> getDependencyGraph().getGraphNodes().stream()
                .filter(gn -> isInstanceOf(TerminalNodeAdapter.class, gn)).forEach(tn -> ((TerminalNodeAdapter) tn)
                        .getPartOfSpeeches().forEach(pn -> pn.setY(newValue.doubleValue()))));

        alignPOSControlYAxisProperty().addListener((observable, oldValue, newValue) -> getDependencyGraph().getGraphNodes()
                .stream().filter(gn -> isInstanceOf(TerminalNodeAdapter.class, gn))
                .forEach(tn -> ((TerminalNodeAdapter) tn).getPartOfSpeeches().forEach(pn -> pn.setCy(newValue.doubleValue()))));

        groupTranslateXAxisProperty().addListener((observable, oldValue, newValue) -> getDependencyGraph().getGraphNodes()
                .stream().filter(gn -> isInstanceOf(TerminalNodeAdapter.class, gn)).forEach(tn -> tn.setTranslateX(newValue.doubleValue())));

        groupTranslateYAxisProperty().addListener((observable, oldValue, newValue) -> getDependencyGraph().getGraphNodes()
                .stream().filter(gn -> isInstanceOf(TerminalNodeAdapter.class, gn)).forEach(tn -> tn.setTranslateY(newValue.doubleValue())));

        terminalFontProperty().addListener((observable, oldValue, newValue) -> {
            getDependencyGraph().getGraphMetaInfo().setTerminalFont(newValue);
            getDependencyGraph().getGraphNodes()
                    .stream().filter(gn -> isInstanceOf(TerminalNodeAdapter.class, gn)).forEach(tn -> tn.setFont(fromFontMetaInfo(newValue)));
        });
        translationFontProperty().addListener((observable, oldValue, newValue) -> {
            getDependencyGraph().getGraphMetaInfo().setTranslationFont(newValue);
            getDependencyGraph().getGraphNodes()
                    .stream().filter(gn -> isInstanceOf(TerminalNodeAdapter.class, gn)).forEach(tn ->
                    ((TerminalNodeAdapter) tn).setTranslationFont(fromFontMetaInfo(newValue)));
        });
        posFontProperty().addListener((observable, oldValue, newValue) -> {
            getDependencyGraph().getGraphMetaInfo().setPosFont(newValue);
            getDependencyGraph().getGraphNodes()
                    .stream().filter(gn -> isInstanceOf(TerminalNodeAdapter.class, gn))
                    .forEach(tn -> ((TerminalNodeAdapter) tn).getPartOfSpeeches().forEach(pn -> pn.setFont(fromFontMetaInfo(newValue))));
            getDependencyGraph().getGraphNodes().stream().filter(gn ->
                    (isInstanceOf(PhraseNodeAdapter.class, gn)) || isInstanceOf(RelationshipNodeAdapter.class, gn))
                    .forEach(n -> n.setFont(fromFontMetaInfo(newValue)));
        });
    }

    public final double getCanvasWidth() {
        return canvasWidth.get();
    }

    public final DoubleProperty canvasWidthProperty() {
        return canvasWidth;
    }

    public final void setCanvasWidth(double canvasWidth) {
        this.canvasWidth.set(canvasWidth);
    }

    public final double getCanvasHeight() {
        return canvasHeight.get();
    }

    public final DoubleProperty canvasHeightProperty() {
        return canvasHeight;
    }

    public final void setCanvasHeight(double canvasHeight) {
        this.canvasHeight.set(canvasHeight);
    }

    public final boolean isShowGridLines() {
        return showGridLines.get();
    }

    public final BooleanProperty showGridLinesProperty() {
        return showGridLines;
    }

    public final void setShowGridLines(boolean showGridLines) {
        this.showGridLines.set(showGridLines);
    }

    public final boolean isShowOutline() {
        return showOutline.get();
    }

    public final BooleanProperty showOutlineProperty() {
        return showOutline;
    }

    public final void setShowOutline(boolean showOutline) {
        this.showOutline.set(showOutline);
    }

    public final boolean isDebugMode() {
        return debugMode.get();
    }

    public final BooleanProperty debugModeProperty() {
        return debugMode;
    }

    public final void setDebugMode(boolean debugMode) {
        this.debugMode.set(debugMode);
    }

    public final double getAlignTerminalYAxis() {
        return alignTerminalYAxis.get();
    }

    public final DoubleProperty alignTerminalYAxisProperty() {
        return alignTerminalYAxis;
    }

    public final double getAlignTranslationYAxis() {
        return alignTranslationYAxis.get();
    }

    public final DoubleProperty alignTranslationYAxisProperty() {
        return alignTranslationYAxis;
    }

    public final void setAlignTranslationYAxis(double alignTranslationYAxis) {
        this.alignTranslationYAxis.set(alignTranslationYAxis);
    }

    public final void setAlignTerminalYAxis(double alignTerminalYAxis) {
        this.alignTerminalYAxis.set(alignTerminalYAxis);
    }

    public final double getAlignPosYAxis() {
        return alignPosYAxis.get();
    }

    public final DoubleProperty alignPosYAxisProperty() {
        return alignPosYAxis;
    }

    public final void setAlignPosYAxis(double alignPosYAxis) {
        this.alignPosYAxis.set(alignPosYAxis);
    }

    public final double getAlignPOSControlYAxis() {
        return alignPOSControlYAxis.get();
    }

    public final DoubleProperty alignPOSControlYAxisProperty() {
        return alignPOSControlYAxis;
    }

    public final void setAlignPOSControlYAxis(double alignPOSControlYAxis) {
        this.alignPOSControlYAxis.set(alignPOSControlYAxis);
    }

    public final double getGroupTranslateXAxis() {
        return groupTranslateXAxis.get();
    }

    public final DoubleProperty groupTranslateXAxisProperty() {
        return groupTranslateXAxis;
    }

    public final void setGroupTranslateXAxis(double groupTranslateXAxis) {
        this.groupTranslateXAxis.set(groupTranslateXAxis);
    }

    public final double getGroupTranslateYAxis() {
        return groupTranslateYAxis.get();
    }

    public final DoubleProperty groupTranslateYAxisProperty() {
        return groupTranslateYAxis;
    }

    public final void setGroupTranslateYAxis(double groupTranslateYAxis) {
        this.groupTranslateYAxis.set(groupTranslateYAxis);
    }

    public final FontMetaInfo getTerminalFont() {
        return terminalFont.get();
    }

    public final ObjectProperty<FontMetaInfo> terminalFontProperty() {
        return terminalFont;
    }

    public final void setTerminalFont(FontMetaInfo terminalFont) {
        this.terminalFont.set(terminalFont);
    }

    public final FontMetaInfo getTranslationFont() {
        return translationFont.get();
    }

    public final ObjectProperty<FontMetaInfo> translationFontProperty() {
        return translationFont;
    }

    public final void setTranslationFont(FontMetaInfo translationFont) {
        this.translationFont.set(translationFont);
    }

    public final FontMetaInfo getPosFont() {
        return posFont.get();
    }

    public final ObjectProperty<FontMetaInfo> posFontProperty() {
        return posFont;
    }

    public final void setPosFont(FontMetaInfo posFont) {
        this.posFont.set(posFont);
    }

    public final Paint getBackgroundColor() {
        return backgroundColor.get();
    }

    public final ObjectProperty<Paint> backgroundColorProperty() {
        return backgroundColor;
    }

    public final void setBackgroundColor(Paint backgroundColor) {
        this.backgroundColor.set(backgroundColor);
    }

    public final DependencyGraphAdapter getDependencyGraph() {
        return dependencyGraph.get();
    }

    public final ObjectProperty<DependencyGraphAdapter> dependencyGraphProperty() {
        return dependencyGraph;
    }

    public final void setDependencyGraph(DependencyGraphAdapter dependencyGraph) {
        this.dependencyGraph.set(dependencyGraph);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new GlobalPropertiesSkin(this);
    }
}
