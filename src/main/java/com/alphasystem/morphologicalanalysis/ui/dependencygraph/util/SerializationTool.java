package com.alphasystem.morphologicalanalysis.ui.dependencygraph.util;

import com.alphasystem.ApplicationException;
import com.alphasystem.morphologicalanalysis.graph.model.DependencyGraph;
import com.alphasystem.morphologicalanalysis.graph.model.Fragment;
import com.alphasystem.morphologicalanalysis.graph.model.Relationship;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.*;
import com.alphasystem.morphologicalanalysis.util.RepositoryTool;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import com.alphasystem.util.ZipFileEntry;
import javafx.collections.ObservableList;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alphasystem.util.AppUtil.createTempFile;
import static com.alphasystem.util.ZipUtil.archiveFile;
import static com.alphasystem.util.ZipUtil.extractFile;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author sali
 */
public class SerializationTool {

    public static final String MDG_EXTENSION = "mdg";
    public static final String MDG_EXTENSION_ALL = format("*.%s", MDG_EXTENSION);
    private static final String DATA_FILE_EXTENSION = ".ser";
    public static final String ZIP_ENTRY_NAME = format("data%s", DATA_FILE_EXTENSION);
    private static RepositoryTool repositoryTool = RepositoryTool.getInstance();
    private static SerializationTool instance;
    private Map<String, GraphNode> allNodes = new HashMap<>();

    private SerializationTool() {
    }

    public synchronized static SerializationTool getInstance() {
        if (instance == null) {
            instance = new SerializationTool();
        }
        return instance;
    }

    public <T extends Externalizable> void serialize(T object, File file) throws IOException {
        try (ObjectOutputStream objOut = new ObjectOutputStream(new FileOutputStream(file))) {
            objOut.writeObject(object);
        } catch (IOException e) {
            throw e;
        }
    }

    public <T extends Externalizable> T deserialize(Class<T> klass, File file) throws IOException {
        T obj = null;
        try (ObjectInputStream objIn = new ObjectInputStream(new FileInputStream(file))) {
            obj = (T) objIn.readObject();
        } catch (ClassNotFoundException e) {
            // this should never happened
            e.printStackTrace();
        } catch (IOException e) {
            throw e;
        }
        return obj;
    }

    /**
     * @param file
     * @param canvasData
     */
    public void save(File file, CanvasData canvasData) {
        File tempFile = null;
        File parentFile = file.getParentFile();
        String name = file.getName();
        String extension = FilenameUtils.getExtension(file.getAbsolutePath());
        if (isBlank(extension) || !extension.equals(MDG_EXTENSION)) {
            name = format("%s.%s", name, MDG_EXTENSION);
        }
        File _file = new File(parentFile, name);
        try {
            tempFile = createTempFile(DATA_FILE_EXTENSION);
            repositoryTool.getRepositoryUtil().getDependencyGraphRepository().save(canvasData.getDependencyGraph());
            serialize(canvasData, tempFile);
            archiveFile(_file, new ZipFileEntry(tempFile, ZIP_ENTRY_NAME));
        } catch (ApplicationException | IOException e) {
            e.printStackTrace();
        } finally {
            if (tempFile != null) {
                tempFile.deleteOnExit();
            }
        }
    }

    public CanvasData open(File file) {
        CanvasData canvasData = null;
        File tempFile = null;
        try {
            tempFile = createTempFile(DATA_FILE_EXTENSION);
            extractFile(file.getAbsolutePath(), ZIP_ENTRY_NAME, tempFile);
            canvasData = deserialize(CanvasData.class, tempFile);
            String id = canvasData.getId();
            DependencyGraph dependencyGraph = repositoryTool.getRepositoryUtil().
                    getDependencyGraphRepository().findOne(id);
            if (dependencyGraph == null) {
                throw new IllegalStateException(format("No dependency graph found with id {%s}", id));
            }
            List<Relationship> relationships = dependencyGraph.getRelationships();
            relationships.forEach(relationship -> System.out.println(relationship.getId()));
            canvasData.setDependencyGraph(dependencyGraph);
            List<Token> tokens = dependencyGraph.getTokens();
            if (tokens == null || tokens.isEmpty()) {
                throw new IllegalStateException(format("No token(s) found in dependency graph with id {%s}", id));
            }
            ObservableList<GraphNode> nodes = canvasData.getNodes();
            if (nodes == null || nodes.isEmpty()) {
                throw new IllegalStateException(format("No node(s) found in canvas data with id {%s}", id));
            }
            allNodes.clear();
            for (GraphNode node : nodes) {
                System.out.println(node.getNodeType() + ": " + node.getId());
                switch (node.getNodeType()) {
                    case TERMINAL:
                    case EMPTY:
                        loadToken((TerminalNode) node, dependencyGraph);
                        break;
                    case RELATIONSHIP:
                        loadRelationship((RelationshipNode) node, dependencyGraph);
                        break;
                    case PHRASE:
                        loadPhrase((PhraseNode) node, dependencyGraph);
                        break;
                }

            }
        } catch (ApplicationException | IOException e) {
            e.printStackTrace();
        } finally {
            if (tempFile != null) {
                tempFile.deleteOnExit();
            }
        }
        return canvasData;
    }

    private void loadToken(TerminalNode node, DependencyGraph dependencyGraph) {
        List<Token> tokens = dependencyGraph.getTokens();
        tokens.stream().filter(token -> token.getId().equals(node.getId())).forEach(token -> {
            ObservableList<PartOfSpeechNode> partOfSpeeches = node.getPartOfSpeeches();
            node.setToken(token);
            List<Location> locations = token.getLocations();
            for (PartOfSpeechNode partOfSpeech : partOfSpeeches) {
                for (Location location : locations) {
                    if (partOfSpeech.getId().equals(location.getId())) {
                        partOfSpeech.setLocation(location);
                        allNodes.put(partOfSpeech.getId(), partOfSpeech);
                        break;
                    } /* end of 'if' */
                } /* end of 'inner' (locations) loop */
            } /* end of 'partOfSpeeches' loop */
        });
        allNodes.put(node.getId(), node);
    }

    private void loadRelationship(RelationshipNode node, DependencyGraph dependencyGraph) {
        List<Relationship> relationships = dependencyGraph.getRelationships();
        relationships.stream().filter(relationship -> relationship.getId().equals(node.getId()))
                .forEach(relationship -> {
                    node.setRelationship(relationship);
                    node.setDependentNode((LinkSupport) allNodes.get(node.getDependentNode().getId()));
                    node.setOwnerNode((LinkSupport) allNodes.get(node.getOwnerNode().getId()));
                    allNodes.put(node.getId(), node);
                });
    }

    private void loadPhrase(PhraseNode node, DependencyGraph dependencyGraph) {
        List<Fragment> fragments = dependencyGraph.getFragments();
        fragments.stream().filter(fragment -> fragment.getId().equals(node.getId()))
                .forEach(fragment -> {
                    node.setFrament(fragment);
                    allNodes.put(node.getId(), node);
                });
    }

}
