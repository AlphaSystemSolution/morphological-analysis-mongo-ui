package com.alphasystem.morphologicalanalysis.treebank.ui.panels;

import com.alphasystem.morphologicalanalysis.treebank.model.ObjectFactory;
import com.alphasystem.morphologicalanalysis.treebank.model.TreeBankData;
import com.alphasystem.morphologicalanalysis.treebank.model.TreeBankMetaData;
import com.alphasystem.morphologicalanalysis.treebank.ui.MATBFrame;
import com.alphasystem.morphologicalanalysis.treebank.ui.action.ExitAction;
import com.alphasystem.morphologicalanalysis.treebank.ui.util.SVGHelper;
import org.apache.batik.dom.svg12.SVG12DOMImplementation;
import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.swing.JSVGCanvas;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

import javax.swing.*;
import java.awt.*;
import java.io.*;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.LINE_END;
import static java.awt.Cursor.*;
import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static java.awt.event.KeyEvent.VK_F;
import static javax.swing.BorderFactory.*;
import static org.apache.batik.dom.svg.SVGDOMImplementation.SVG_NAMESPACE_URI;

/**
 * @author sali
 */
public class MATBContentPanel extends JPanel {

    private static final long serialVersionUID = 7957179366856811070L;

    private static final DOMImplementation DOM_IMPLEMENTATION = SVG12DOMImplementation.getDOMImplementation();

    private static final ObjectFactory OBJECT_FACTORY = new ObjectFactory();

    private final MATBFrame ownerFrame;

    // we are going to maintain two copies of "SVGGraphics2D" objects, one for dispalying on the screen
    // other for saving to the file

    private SVGGraphics2D sg2d;

    private SVGGraphics2D fg2d;

    private SVGDocument sDocument;

    private SVGDocument fDocument;

    private JSVGCanvas canvas;

    private JPanel viewPanel;

    private PropertiesPanel propertiesPanel;

    private TreeBankData tbData;

    private TreeBankMetaData metaData;

    public MATBContentPanel(final MATBFrame ownerFrame) {
        super(new BorderLayout());

        this.ownerFrame = ownerFrame;
        this.ownerFrame.setJMenuBar(createMenuBar());

        tbData = OBJECT_FACTORY.createTreeBankData();
        tbData.setMetaData(OBJECT_FACTORY.createTreeBankMetaData());
        metaData = tbData.getMetaData();
        viewPanel = new JPanel(new BorderLayout());

        initSwingGrapics();
        initFileGraphics();

        viewPanel.setBorder(createCompoundBorder(createEmptyBorder(0, 6, 0, 6), createTitledBorder("SVG Canvas")));
        viewPanel.add(new JScrollPane(canvas), CENTER);
        add(viewPanel, CENTER);

        propertiesPanel = new PropertiesPanel(this, metaData.getWidth(), metaData.getHeight());
        add(propertiesPanel, LINE_END);

//        JideSplitPane splitPane = new JideSplitPane(HORIZONTAL_SPLIT);
//        splitPane.setProportionalLayout(true);
//        splitPane.add(viewPanel, JideBoxLayout.VARY);
//        splitPane.add(propertiesPanel, JideBoxLayout.FLEXIBLE);
//        add(splitPane, CENTER);
    }

    private Dimension getCanavasDimension() {
        return new Dimension(metaData.getWidth(), metaData.getHeight());
    }

    private void initSwingGrapics() {
        sDocument = (SVGDocument) DOM_IMPLEMENTATION.createDocument(SVG_NAMESPACE_URI, "svg", null);
        SVGGeneratorContext ctx = SVGGeneratorContext.createDefault(sDocument);
        ctx.setEmbeddedFontsOn(true);

        sg2d = new SVGGraphics2D(ctx, false);
        sg2d.setSVGCanvasSize(getCanavasDimension());
        sg2d.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);

        initCanvas();
    }

    private void initFileGraphics() {
        fDocument = (SVGDocument) DOM_IMPLEMENTATION.createDocument(SVG_NAMESPACE_URI, "svg", null);
        SVGGeneratorContext ctx = SVGGeneratorContext.createDefault(fDocument);
        ctx.setEmbeddedFontsOn(true);

        fg2d = new SVGGraphics2D(ctx, false);
        fg2d.setSVGCanvasSize(getCanavasDimension());
        fg2d.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
    }

    private void initCanvas() {
        Element root = sDocument.getDocumentElement();
        sg2d.getRoot(root);
        canvas = new JSVGCanvas();
        canvas.setSVGDocument(sDocument);
    }

    private void resetViewPanel() {
        viewPanel.removeAll();
        viewPanel.add(new JScrollPane(canvas), CENTER);
        viewPanel.updateUI();
    }

    private void resetCanvas() {
        initSwingGrapics();
        initFileGraphics();
        refillDocuments();
    }

    private void refillDocuments() {
        drawGridLines();
        changeDimensions();
    }

    private void drawGridLines() {
        int width = metaData.getWidth();
        int height = metaData.getHeight();
        boolean showGridLines = metaData.isShowGridLines();
        boolean showOutline = metaData.isShowOutline();
        if (showOutline) {
            SVGHelper.drawGridLines(width, height, sg2d, showGridLines);
            SVGHelper.drawGridLines(width, height, fg2d, showGridLines);
        }
    }

    private void changeDimensions() {
        sg2d.setSVGCanvasSize(getCanavasDimension());
        fg2d.setSVGCanvasSize(getCanavasDimension());
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menu = new JMenu("File");
        menu.setMnemonic(VK_F);

        menu.addSeparator();
        menu.add(new ExitAction(this));

        menuBar.add(menu);
        return menuBar;
    }

    public TreeBankData getTreeBankData() {
        return tbData;
    }

    // delegate methods for actions

    /**
     * Called to redraw canvas. The parameter <code>dummy</code> has no significance, it is there just to
     * differentiate with out of box {@link JPanel#updateUI()}.
     *
     * @param dummy
     */
    public void updateUI(boolean dummy) {
        resetCanvas();
        initCanvas();
        resetViewPanel();
    }

    public void updateCursor(boolean wait) {
        Cursor cursor = wait ? getPredefinedCursor(WAIT_CURSOR) : getDefaultCursor();
        setCursor(cursor);
    }

    public void saveFile(File file) {
        try (Writer out = new OutputStreamWriter(new FileOutputStream(file), "UTF-16")) {
            fg2d.stream(out, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exit() {
        ownerFrame.exit();
    }

}
