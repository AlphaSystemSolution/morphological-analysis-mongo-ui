package com.alphasystem.morphologicalanalysis.treebank.ui.util;

import com.alphasystem.ApplicationException;
import com.alphasystem.morphologicalanalysis.treebank.model.*;
import com.alphasystem.morphologicalanalysis.treebank.ui.model.*;
import com.alphasystem.morphologicalanalysis.util.RepositoryTool;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.PartOfSpeech;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.RelationshipType;
import com.alphasystem.util.ZipFileEntry;
import javafx.collections.ObservableList;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static com.alphasystem.util.AppUtil.createTempFile;
import static com.alphasystem.util.JAXBUtil.marshall;
import static com.alphasystem.util.JAXBUtil.unmarshal;
import static com.alphasystem.util.ZipUtil.archiveFile;
import static com.alphasystem.util.ZipUtil.extractFile;
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author sali
 */
public class SerializationTool {

    public static final ObjectFactory OBJECT_FACTORY = new ObjectFactory();
    public static final String MDG_EXTENSION = "mdg";
    public static final String MDG_EXTENSION_ALL = format("*.%s", MDG_EXTENSION);
    public static final String ZIP_ENTRY_NAME = "graphNode.xml";
    private static final String XML_FILE_EXTENSION = ".xml";
    private static SerializationTool instance;

    private SerializationTool() {
    }

    public synchronized static SerializationTool getInstance() {
        if (instance == null) {
            instance = new SerializationTool();
        }
        return instance;
    }

    private static <T extends Object> ConstructorArgument createConstructorArgument(Class<T> type, String value,
                                                                                    String initMethod,
                                                                                    String factoryMethod) {
        ConstructorArgument constructorArgument = OBJECT_FACTORY.createConstructorArgument()
                .withType(type.getName()).withValue(value);
        if (!isBlank(initMethod)) {
            constructorArgument.withInitMethod(initMethod);
        }
        if (!isBlank(factoryMethod)) {
            constructorArgument.withFactoryMethod(factoryMethod);
        }
        return constructorArgument;
    }

    private static <T extends Object> ConstructorArgument createConstructorArgument(Class<T> type, T value,
                                                                                    String initMethod,
                                                                                    String factoryMethod) {
        ConstructorArgument constructorArgument = OBJECT_FACTORY.createConstructorArgument()
                .withType(type.getName()).withValue(valueOf(value));
        if (!isBlank(initMethod)) {
            constructorArgument.withInitMethod(initMethod);
        }
        if (!isBlank(factoryMethod)) {
            constructorArgument.withFactoryMethod(factoryMethod);
        }
        return constructorArgument;
    }

    private static <T extends Object> ConstructorArgument createConstructorArgument(Class<T> type, T value,
                                                                                    String factoryMethod) {
        return createConstructorArgument(type, value, null, factoryMethod);
    }

    private static <T extends Object> ConstructorArgument createConstructorArgument(Class<T> type, T value) {
        return createConstructorArgument(type, value, null);
    }

    private static ConstructorArgument createStringArgument(String value) {
        return createConstructorArgument(String.class, value);
    }

    private static ConstructorArgument createIntegerArgument(Integer value) {
        return createConstructorArgument(Integer.class, value, "parseInt");
    }

    private static ConstructorArgument createDoubleArgument(Double value) {
        return createConstructorArgument(Double.class, value, "parseDouble");
    }

    private static ConstructorArgument createBooleanArgument(Boolean value) {
        return createConstructorArgument(Boolean.class, value, "parseBoolean");
    }

    private static <T extends Enum<?>> ConstructorArgument createEnumArgument(Class<T> enumClass, T enumType) {
        return createConstructorArgument(enumClass, (enumType == null ? null : enumType.name()), null, "valueOf");
    }

    public void save(File file, String svgFileName, CanvasData canvasData) {
        File tempFile = null;
        File parentFile = file.getParentFile();
        String name = file.getName();
        String extension = FilenameUtils.getExtension(file.getAbsolutePath());
        if (extension == null || !extension.equals(MDG_EXTENSION)) {
            name = format("%s.%s", name, MDG_EXTENSION);
        }
        File _file = new File(parentFile, name);
        try {
            tempFile = createTempFile(XML_FILE_EXTENSION);
            serialize(tempFile, svgFileName, canvasData);
            ZipFileEntry entry = new ZipFileEntry(tempFile, ZIP_ENTRY_NAME);
            archiveFile(_file, entry);
        } catch (ApplicationException e) {
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
            tempFile = createTempFile(XML_FILE_EXTENSION);
            extractFile(file.getAbsolutePath(), ZIP_ENTRY_NAME, tempFile);
            canvasData = deserialize(tempFile);
        } catch (ApplicationException e) {
            e.printStackTrace();
        } finally {
            if (tempFile != null) {
                tempFile.deleteOnExit();
            }
        }
        return canvasData;
    }

    public CanvasData deserialize(File file) {
        GraphData graphData = unmarshal(GraphData.class, file);
        MetaData metaData = graphData.getMetaData();
        CanvasMetaData canvasMetaData = new CanvasMetaData(metaData.getWidth(), metaData.getHeight(),
                metaData.isShowGridLines(), metaData.isShowOutline(), metaData.isDebugMode());
        CanvasData canvasData = new CanvasData(canvasMetaData);
        List<ObjectType> objectList = graphData.getObject();
        for (ObjectType objectType : objectList) {
            GraphNode graphNode = deserialize(objectType);
            canvasData.add(graphNode);
        }
        return canvasData;
    }

    public void serialize(File file, String svgFileName, CanvasData canvasData) {
        CanvasMetaData canvasMetaData = canvasData.getCanvasMetaData();
        GraphData graphData = OBJECT_FACTORY.createGraphData()
                .withMetaData(serializeMetaData(svgFileName, canvasMetaData));
        ObservableList<GraphNode> graphNodes = canvasData.getNodes();
        GraphNode[] nodes = graphNodes.toArray(new GraphNode[graphNodes.size()]);
        if (!isEmpty(nodes)) {
            ObjectType[] objectTypes = new ObjectType[nodes.length];
            for (int i = 0; i < objectTypes.length; i++) {
                GraphNode node = nodes[i];
                ObjectType objectType = null;
                switch (node.getNodeType()) {
                    case TERMINAL:
                        objectType = serializeTerminalNode((TerminalNode) node);
                        break;
                    case PART_OF_SPEECH:
                        objectType = serializePartOfSpeechNode((PartOfSpeechNode) node);
                        break;
                    case PHRASE:
                        objectType = serializePhraseNode((PhraseNode) node);
                        break;
                    case RELATIONSHIP:
                        objectType = serializeRelationshipNode((RelationshipNode) node);
                        break;
                    case REFERENCE:
                        objectType = serializeReferenceNode((ReferenceNode) node);
                        break;
                    case HIDDEN:
                        objectType = serializeHiddenNode((HiddenNode) node);
                        break;
                    case EMPTY:
                        objectType = serializeEmptyNode((EmptyNode) node);
                        break;
                    case ROOT:
                        break;
                }
                objectTypes[i] = objectType;
            }
            graphData.withObject(objectTypes);
        }
        marshall(file, GraphData.class.getPackage().getName(), OBJECT_FACTORY.createGraphData(graphData));
    }

    private MetaData serializeMetaData(String svgFileName, CanvasMetaData metaData) {
        return OBJECT_FACTORY.createMetaData().withSvgFileName(svgFileName).withWidth(metaData.getWidth())
                .withHeight(metaData.getHeight()).withShowOutline(metaData.isShowOutLines())
                .withShowGridLines(metaData.isShowGridLines()).withDebugMode(metaData.isDebugMode());
    }

    private ObjectType serializeTerminalNode(TerminalNode node) {
        ObjectType objectType = OBJECT_FACTORY.createObjectType().withType(node.getClass().getName());
        List<ConstructorArgument> constructorArgs = objectType.getArgument();

        constructorArgs.add(createConstructorArgument(RepositoryTool.class, node.getToken().getId(),
                "getInstance", "getToken"));
        constructorArgs.add(createStringArgument(node.getId()));
        constructorArgs.add(createDoubleArgument(node.getX()));
        constructorArgs.add(createDoubleArgument(node.getY()));
        constructorArgs.add(createDoubleArgument(node.getX1()));
        constructorArgs.add(createDoubleArgument(node.getY1()));
        constructorArgs.add(createDoubleArgument(node.getX2()));
        constructorArgs.add(createDoubleArgument(node.getY2()));
        constructorArgs.add(createDoubleArgument(node.getX3()));
        constructorArgs.add(createDoubleArgument(node.getY3()));
        constructorArgs.add(createDoubleArgument(node.getTranslateX()));
        constructorArgs.add(createDoubleArgument(node.getTranslateY()));
        ConstructorArgument constructorArgument = OBJECT_FACTORY.createConstructorArgument()
                .withType(PartOfSpeechNode[].class.getName());
        constructorArgs.add(constructorArgument);

        ObservableList<PartOfSpeechNode> partOfSpeeches = node.getPartOfSpeeches();
        for (PartOfSpeechNode partOfSpeech : partOfSpeeches) {
            constructorArgument.getValues().add(serializePartOfSpeechNode(partOfSpeech));
        }

        return objectType;
    }

    private ObjectType serializePartOfSpeechNode(PartOfSpeechNode node) {
        ObjectType objectType = OBJECT_FACTORY.createObjectType().withType(node.getClass().getName());
        List<ConstructorArgument> constructorArgs = objectType.getArgument();

        constructorArgs.add(createConstructorArgument(PartOfSpeech.class, node.getPartOfSpeech().name(),
                null, "valueOf"));
        constructorArgs.add(createConstructorArgument(RepositoryTool.class, node.getLocation().getId(),
                "getInstance", "getLocation"));
        constructorArgs.add(createStringArgument(node.getId()));
        constructorArgs.add(createDoubleArgument(node.getX()));
        constructorArgs.add(createDoubleArgument(node.getY()));
        constructorArgs.add(createDoubleArgument(node.getCx()));
        constructorArgs.add(createDoubleArgument(node.getCy()));
        constructorArgs.add(createBooleanArgument(node.isExcluded()));

        return objectType;
    }

    private ObjectType serializeRelationshipNode(RelationshipNode node) {
        ObjectType objectType = OBJECT_FACTORY.createObjectType().withType(node.getClass().getName());
        List<ConstructorArgument> constructorArgs = objectType.getArgument();

        constructorArgs.add(createEnumArgument(RelationshipType.class, node.getGrammaticalRelationship()));
        constructorArgs.add(createStringArgument(node.getId()));
        constructorArgs.add(createDoubleArgument(node.getX()));
        constructorArgs.add(createDoubleArgument(node.getY()));
        constructorArgs.add(createDoubleArgument(node.getStartX()));
        constructorArgs.add(createDoubleArgument(node.getStartY()));
        constructorArgs.add(createDoubleArgument(node.getControlX1()));
        constructorArgs.add(createDoubleArgument(node.getControlY1()));
        constructorArgs.add(createDoubleArgument(node.getControlX2()));
        constructorArgs.add(createDoubleArgument(node.getControlY2()));
        constructorArgs.add(createDoubleArgument(node.getEndX()));
        constructorArgs.add(createDoubleArgument(node.getEndY()));
        constructorArgs.add(createDoubleArgument(node.getT1()));
        constructorArgs.add(createDoubleArgument(node.getT2()));

        return objectType;
    }

    private ObjectType serializePhraseNode(PhraseNode node) {
        ObjectType objectType = OBJECT_FACTORY.createObjectType().withType(node.getClass().getName());
        List<ConstructorArgument> constructorArgs = objectType.getArgument();

        constructorArgs.add(createEnumArgument(RelationshipType.class, node.getGrammaticalRelationship()));
        constructorArgs.add(createStringArgument(node.getId()));
        constructorArgs.add(createDoubleArgument(node.getX()));
        constructorArgs.add(createDoubleArgument(node.getY()));
        constructorArgs.add(createDoubleArgument(node.getX1()));
        constructorArgs.add(createDoubleArgument(node.getY1()));
        constructorArgs.add(createDoubleArgument(node.getX2()));
        constructorArgs.add(createDoubleArgument(node.getY2()));
        constructorArgs.add(createDoubleArgument(node.getCx()));
        constructorArgs.add(createDoubleArgument(node.getCy()));

        return objectType;
    }

    private ObjectType serializeEmptyNode(EmptyNode node) {
        ObjectType objectType = OBJECT_FACTORY.createObjectType().withType(node.getClass().getName());
        List<ConstructorArgument> constructorArgs = objectType.getArgument();

        constructorArgs.add(createStringArgument(node.getId()));
        constructorArgs.add(createDoubleArgument(node.getX()));
        constructorArgs.add(createDoubleArgument(node.getY()));
        constructorArgs.add(createDoubleArgument(node.getX1()));
        constructorArgs.add(createDoubleArgument(node.getY1()));
        constructorArgs.add(createDoubleArgument(node.getX2()));
        constructorArgs.add(createDoubleArgument(node.getY2()));
        constructorArgs.add(createDoubleArgument(node.getX3()));
        constructorArgs.add(createDoubleArgument(node.getY3()));
        ConstructorArgument constructorArgument = OBJECT_FACTORY.createConstructorArgument()
                .withType(PartOfSpeechNode.class.getName());
        constructorArgument.getValues().add(serializePartOfSpeechNode(node.getPartOfSpeeches().get(0)));
        constructorArgs.add(constructorArgument);

        return objectType;
    }

    private ObjectType serializeHiddenNode(HiddenNode node) {
        ObjectType objectType = OBJECT_FACTORY.createObjectType().withType(node.getClass().getName());
        List<ConstructorArgument> constructorArgs = objectType.getArgument();

        return objectType;
    }

    private ObjectType serializeReferenceNode(ReferenceNode node) {
        ObjectType objectType = OBJECT_FACTORY.createObjectType().withType(node.getClass().getName());
        List<ConstructorArgument> constructorArgs = objectType.getArgument();

        return objectType;
    }

    private GraphNode deserialize(ObjectType objectType) {
        GraphNode graphNode = null;
        try {
            Class<?> nodeClass = Class.forName(objectType.getType());
            List<ConstructorArgument> constructorArgs = objectType.getArgument();
            Object[] constructorObjects = new Object[constructorArgs.size()];
            Class<?>[] constructorClasses = new Class<?>[constructorArgs.size()];
            for (int i = 0; i < constructorArgs.size(); i++) {
                ConstructorArgument arg = constructorArgs.get(i);
                try {
                    Object object = getConstructorArgument(arg);
                    if (object != null) {
                        constructorObjects[i] = object;
                        constructorClasses[i] = object.getClass();
                    }
                } catch (IllegalAccessException | InvocationTargetException
                        | NoSuchMethodException | InstantiationException e) {
                    e.printStackTrace();
                }
            }
            try {
                Constructor<?> constructor = nodeClass.getConstructor(constructorClasses);
                graphNode = (GraphNode) constructor.newInstance(constructorObjects);
            } catch (NoSuchMethodException | InvocationTargetException |
                    InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return graphNode;
    }

    private Object getConstructorArgument(ConstructorArgument arg) throws ClassNotFoundException,
            IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Object object = null;
        Class<?> typeClass = Class.forName(arg.getType());
        String factoryMethodName = arg.getFactoryMethod();
        if (!isBlank(factoryMethodName)) {
            String initMethodName = arg.getInitMethod();
            Method factoryMethod = null;
            Object factory = null;
            if (initMethodName == null) {
                factoryMethod = typeClass.getMethod(factoryMethodName, String.class);
            } else {
                Method initMethod = typeClass.getMethod(initMethodName);
                factory = initMethod.invoke(null);
                factoryMethod = typeClass.getMethod(factoryMethodName, String.class);
            }
            object = factoryMethod.invoke(factory, arg.getValue());
        } else if (typeClass.getName().equals(String.class.getName())) {
            Constructor<?> constructor = typeClass.getConstructor(String.class);
            object = constructor.newInstance(arg.getValue());
        } else if (typeClass.isArray()) {
            List<ObjectType> values = arg.getValues();
            object = Array.newInstance(typeClass.getComponentType(), values.size());
            for (int j = 0; j < values.size(); j++) {
                Array.set(object, j, deserialize(values.get(j)));
            }
        } else {
            throw new IllegalStateException(format("Unhandled type: %s", typeClass.getName()));
        }
        return object;
    }
}
