package com.alphasystem.morphologicalanalysis.ui.dependencygraph.util;

import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.CanvasMetaData;
import com.alphasystem.util.JAXBTool;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.w3.svg.ObjectFactory;
import org.w3.svg.RectType;
import org.w3.svg.SvgType;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static javafx.scene.paint.Color.BEIGE;

/**
 * @author sali
 */
public final class SVGExport {

    private static final ObjectFactory OBJECT_FACTORY = new ObjectFactory();

    /**
     * Do not let anyone instantiate this class
     */
    private SVGExport() {
    }

    public static void export(CanvasMetaData metaData, Pane canvasPane, File file) {
        SvgType svgType = OBJECT_FACTORY.createSvgType().withWidth(valueOf(metaData.getWidth()))
                .withHeight(valueOf(metaData.getHeight()));
        RectType rectType = OBJECT_FACTORY.createRectType().withWidth("100%").withHeight("100%")
                .withFill(getColorValue(BEIGE));
        svgType.getContents().add(rectType);

        JAXBTool jaxbTool = new JAXBTool();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            jaxbTool.marshall(writer, svgType.getClass().getPackage().getName(),
                    OBJECT_FACTORY.createSvg(svgType));
        } catch (IOException | XMLStreamException | JAXBException e) {
            e.printStackTrace();
        }

    }

    private static String getColorValue(Color color) {
        return format("#%02X%02X%02X", (int) (color.getRed() * 255), (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
}
