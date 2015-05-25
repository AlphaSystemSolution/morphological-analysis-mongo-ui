package com.alphasystem.svg.jfx;

import com.alphasystem.svg.SVGTool;
import com.alphasystem.util.IdGenerator;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;
import org.apache.commons.lang3.StringUtils;
import org.w3.svg.GType;
import org.w3.svg.SvgType;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import static com.alphasystem.svg.SVGTool.SVG_OBJECT_FACTORY;
import static com.alphasystem.util.IdGenerator.nextId;
import static com.alphasystem.util.JAXBUtil.marshall;
import static java.lang.String.valueOf;

/**
 * @author sali
 */
public final class SVGGraphicsContext {

    private final Pane layout;
    private final SVGTool svgTool;
    private SvgType svgDocument;
    private GType currentGroup;

    /**
     * @param layout
     * @param width
     * @param height
     */
    public SVGGraphicsContext(Pane layout, int width, int height) {
        this.layout = layout;
        svgTool = SVGTool.getInstance();
        createSVGDocument(width, height);
    }

    /**
     * initialize the
     *
     * @param width
     * @param height
     * @return
     */
    public SVGGraphicsContext createSVGDocument(int width, int height) {
        svgDocument = SVG_OBJECT_FACTORY.createSvgType().withPreserveAspectRatio("xMidYMid meet")
                .withWidth(valueOf(width)).withHeight(valueOf(height));
        return this;
    }

    /**
     * Starts new SVG "g" element with system generated id.
     *
     * @return reference to <code>this</code>
     */
    public SVGGraphicsContext startGroup() {
        return startGroup(null);
    }

    /**
     * Starts new SVG "g" element with given id.
     *
     * @param id given id, if null system generated id will be used.
     * @return reference to <code>this</code>
     * @see IdGenerator#nextId()
     */
    public SVGGraphicsContext startGroup(String id) {
        id = StringUtils.isBlank(id) ? nextId() : id;
        currentGroup = SVG_OBJECT_FACTORY.createGType().withId(id);
        return this;
    }

    /**
     * Add current "g" element in to to root.
     *
     * @return reference to <code>this</code>
     */
    public SVGGraphicsContext endGroup() {
        svgDocument.withContents(currentGroup);
        currentGroup = null;
        return this;
    }


    public SvgType getSvgDocument() {
        return svgDocument;
    }

    /**
     * Saves the svg document in to the given <code>file</code>.
     *
     * @param file
     */
    public void save(File file) throws FileNotFoundException {
        marshall(new BufferedOutputStream(new FileOutputStream(file)),
                SvgType.class.getPackage().getName(), SVG_OBJECT_FACTORY.createSvg(svgDocument));
    }

    public SVGGraphicsContext draw(Shape shape) {
        // TODO: save current path into SVG document
        layout.getChildren().add(shape);
        return this;
    }

}
