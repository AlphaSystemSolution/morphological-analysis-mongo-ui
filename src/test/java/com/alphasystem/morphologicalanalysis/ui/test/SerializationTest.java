package com.alphasystem.morphologicalanalysis.ui.test;

import com.alphasystem.morphologicalanalysis.util.SpringContextHelper;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

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

    private File tmpFolder;

    @BeforeSuite
    public void beforeSuite() {
        SpringContextHelper.getInstance();
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
