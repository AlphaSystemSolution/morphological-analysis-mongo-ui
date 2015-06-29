package com.alphasystem.morphologicalanalysis.ui.test;

import com.alphasystem.morphologicalanalysis.ui.dependencygraph.components.CanvasPane;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.CanvasData;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.CanvasMetaData;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.SVGExport;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.SerializationTool;
import com.alphasystem.morphologicalanalysis.util.SpringContextHelper;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import static java.lang.String.format;
import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.Files.delete;
import static java.nio.file.Files.walkFileTree;
import static org.testng.Assert.assertEquals;
import static org.testng.Reporter.log;

/**
 * @author sali
 */
public class SerializationTest {

    private SerializationTool serializationTool;
    private File tmpFolder;

    @BeforeSuite
    public void beforeSuite() {
        SpringContextHelper.getInstance();
        serializationTool = SerializationTool.getInstance();
        tmpFolder = new File("tmp");
        if (tmpFolder.exists()) {
            try {
                deleteRecursively();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        tmpFolder.mkdir();
        assertEquals(tmpFolder.exists(), true);
        log(format("Temp folder created: %s", tmpFolder.getAbsolutePath()), true);
    }

    @AfterSuite
    public void afterSuite() {
        if (tmpFolder != null && tmpFolder.exists()) {
            try {
                deleteRecursively();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void serializeCanvasMetaData() {
        CanvasMetaData canvasMetaData = new CanvasMetaData();
        canvasMetaData.setWidth(700);
        canvasMetaData.setShowGridLines(true);
        try {
            serializationTool.serialize(canvasMetaData,
                    getSerializeFileName(canvasMetaData.getClass()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(dependsOnMethods = {"serializeCanvasMetaData"})
    public void deserializeCanvasMetaData() {
        CanvasMetaData metaData = null;
        try {
            metaData = serializationTool.deserialize(
                    getSerializeFileName(CanvasMetaData.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        log(format("Width: %s, Height: %s, Show Grid Lines: %s",
                metaData.getWidth(), metaData.getHeight(), metaData.isShowGridLines()));
    }

    @Test(dependsOnMethods = {"deserializeCanvasMetaData"})
    public void exportAsSvg() {
        Path inPath = Paths.get("C:\\Users\\sali\\Arabic\\Notes\\dependency-graph\\001_002.mdg");
        Path outPath = Paths.get("C:\\Users\\sali\\Arabic\\Notes\\dependency-graph\\001_002.svg");
        CanvasData canvasData = serializationTool.open(inPath.toFile());
        CanvasPane canvasPane = new CanvasPane(canvasData);
        SVGExport.export(canvasData.getCanvasMetaData(), canvasPane.getCanvasPane(), outPath.toFile());
    }

    private File getSerializeFileName(Class<?> klass) {
        return new File(tmpFolder, format("%s.ser", klass.getSimpleName().toLowerCase()));
    }

    private void deleteRecursively() throws IOException {
        if (tmpFolder == null || !tmpFolder.exists()) {
            return;
        }
        Path directory = Paths.get(tmpFolder.toURI());
        walkFileTree(directory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                log(format("Deleting file: %s", file));
                delete(file);
                return CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                log(format("Deleting directory: %s", dir));
                delete(dir);
                return CONTINUE;
            }
        });
    }

}
