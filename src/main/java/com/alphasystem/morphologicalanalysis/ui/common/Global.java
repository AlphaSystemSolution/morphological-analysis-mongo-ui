package com.alphasystem.morphologicalanalysis.ui.common;

import com.alphasystem.arabic.model.ArabicWord;
import com.alphasystem.morphologicalanalysis.graph.model.FontMetaInfo;
import com.alphasystem.morphologicalanalysis.graph.model.GraphNode;
import com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.GraphNodeAdapter;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.TerminalNodeAdapter;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.AlternateStatus;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.NounStatus;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.PartOfSpeech;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import static com.alphasystem.arabic.model.ArabicLetterType.*;
import static com.alphasystem.arabic.model.ArabicLetters.WORD_SPACE;
import static com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType.*;
import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.PartOfSpeech.*;
import static com.alphasystem.util.AppUtil.*;
import static java.lang.String.format;
import static java.nio.file.FileSystems.newFileSystem;
import static java.nio.file.Paths.get;
import static java.util.Arrays.asList;
import static javafx.scene.layout.BorderStroke.THIN;
import static javafx.scene.layout.BorderStrokeStyle.SOLID;
import static javafx.scene.layout.CornerRadii.EMPTY;
import static javafx.scene.paint.Color.LIGHTGREY;
import static javafx.scene.text.Font.font;

/**
 * @author sali
 */
public class Global {

    public static final File DEFAULT_WORD_BY_WORD_WORKING_DIRECTORY = new File(USER_HOME_DIR, ".wordbyword");
    public static final File DEFAULT_DICTIONARY_DIRECTORY = new File(DEFAULT_WORD_BY_WORD_WORKING_DIRECTORY, "dictionary");
    public static final File DEFAULT_CSS_DIRECTORY = new File(DEFAULT_DICTIONARY_DIRECTORY, "css");
    public static final String SPACE_STR = WORD_SPACE.toUnicode();
    public static final ArabicWord FI_MAHL = ArabicWord.getWord(FA, YA, SPACE, MEEM, HHA, LAM);
    public static final List<GraphNodeType> TERMINALS = asList(TERMINAL, IMPLIED, HIDDEN, REFERENCE);
    public static final List<GraphNodeType> NON_TERMINALS = asList(IMPLIED, HIDDEN, REFERENCE);
    public static final List<PartOfSpeech> PART_OF_SPEECH_EXCLUDE_LIST = asList(
            DEFINITE_ARTICLE, QURANIC_PUNCTUATION, CONJUNCTION_PARTICLE_WAW, CONJUNCTION_PARTICLE_FA);
    public static final double MIN_WIDTH = 20;
    public static final double MIN_HEIGHT = 20;
    public static final int AMOUNT_STEP_BY = 20;
    public static final int MAX_WIDTH = 1200;
    public static final int MAX_HEIGHT = 1000;
    public static final double RECTANGLE_WIDTH = 100;
    public static final double RECTANGLE_HEIGHT = 100;
    public static final double MIN_RECTANGLE_WIDTH = 60;
    public static final double MIN_RECTANGLE_HEIGHT = 10;
    public static final double GAP_BETWEEN_TOKENS = 60;
    public static final double MIN_GAP_BETWEEN_TOKENS = 10;
    public static final double INITIAL_X = 0;
    public static final double INITIAL_Y = 40;
    public static final int GAP = 10;
    public static final Border BORDER = new Border(new BorderStroke(LIGHTGREY, SOLID, EMPTY, THIN));
    public static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("resources");
    public static final String MAWRID_READER_URL = System.getProperty("mawrid-reader.url");
    private static Global instance;

    static {
        DEFAULT_CSS_DIRECTORY.mkdirs();
    }

    private Global() {
    }

    public synchronized static Global getInstance() {
        if (instance == null) {
            instance = new Global();
        }
        return instance;
    }

    public static String getLabel(Class<?> klass, String label) {
        String key = format("%s.%s.label", klass.getSimpleName(), label);
        return RESOURCE_BUNDLE.getString(key);
    }

    public static boolean isFakeTerminal(TerminalNodeAdapter src) {
        GraphNodeType graphNodeType = src.getGraphNodeType();
        return graphNodeType.equals(IMPLIED) || graphNodeType.equals(HIDDEN) || graphNodeType.equals(REFERENCE);
    }

    public static boolean isTerminal(GraphNodeAdapter src) {
        return TERMINALS.contains(src.getGraphNodeType());
    }

    public static boolean isTerminal(GraphNode src) {
        return TERMINALS.contains(src.getGraphNodeType());
    }

    public static AlternateStatus getFromNounStatus(NounStatus nounStatus) {
        return AlternateStatus.valueOf(nounStatus.name());
    }

    public static Font fromFontMetaInfo(FontMetaInfo fmi) {
        return font(fmi.getFamily(), FontWeight.valueOf(fmi.getWeight()),
                FontPosture.valueOf(fmi.getPosture()), fmi.getSize());
    }

    public static FontMetaInfo fromFont(Font font) {
        return new FontMetaInfo(font.getFamily(), "NORMAL", "REGULAR", font.getSize());
    }

    public static Image getImage(String imageFolder, String imageName) {
        imageFolder = (imageFolder == null) ? "" : format("%s.", imageFolder);
        return new Image(getResourceAsStream(format("images.%s%s", imageFolder, imageName)));
    }

    public static ImageView getImageView(String imageFolder, String imageName) {
        return getImageView(getImage(imageFolder, imageName));
    }

    public static ImageView getImageView(Image image) {
        return new ImageView(image);
    }

    public static List<String> readAllLines(String resourceName) throws IOException, URISyntaxException {
        FileSystem fs = null;
        List<String> lines = null;
        try {
            URL url = getResource(resourceName);
            URI uri = url.toURI();
            Path path;
            String[] split = uri.toString().split("!");
            if (split != null && split.length > 1) {
                fs = newFileSystem(URI.create(split[0]), new HashMap());
                path = fs.getPath(split[1]);
            } else {
                path = get(uri);
            }
            lines = Files.readAllLines(path);
        } catch (IOException | URISyntaxException e) {
            throw e;
        } finally {
            if (fs != null) {
                try {
                    fs.close();
                } catch (IOException e) {
                }
            }
        }

        return lines;
    }
}
