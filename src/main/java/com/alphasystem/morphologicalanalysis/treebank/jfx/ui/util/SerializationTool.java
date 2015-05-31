package com.alphasystem.morphologicalanalysis.treebank.jfx.ui.util;

import com.alphasystem.ApplicationException;
import com.alphasystem.morphologicalanalysis.model.support.GrammaticalRelationship;
import com.alphasystem.morphologicalanalysis.model.support.PartOfSpeech;
import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.*;
import com.alphasystem.morphologicalanalysis.treebank.model.*;
import com.alphasystem.util.ZipFileEntry;
import javafx.collections.ObservableList;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
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
                    case RELATIONSHIP:
                        objectType = serializeRelationshipNode((RelationshipNode) node);
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
        List<ConstructorArgument> constructorArgs = objectType.getConstructorArgs();

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

        return objectType;
    }

    private ObjectType serializePartOfSpeechNode(PartOfSpeechNode node) {
        ObjectType objectType = OBJECT_FACTORY.createObjectType().withType(node.getClass().getName());
        List<ConstructorArgument> constructorArgs = objectType.getConstructorArgs();

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
        List<ConstructorArgument> constructorArgs = objectType.getConstructorArgs();

        constructorArgs.add(createEnumArgument(GrammaticalRelationship.class, node.getGrammaticalRelationship()));
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

    private GraphNode deserialize(ObjectType objectType) {
        GraphNode graphNode = null;
        try {
            Class<?> nodeClass = Class.forName(objectType.getType());
            List<ConstructorArgument> constructorArgs = objectType.getConstructorArgs();
            Object[] constructorObjects = new Object[constructorArgs.size()];
            Class<?>[] constructorClasses = new Class<?>[constructorArgs.size()];
            for (int i = 0; i < constructorArgs.size(); i++) {
                ConstructorArgument arg = constructorArgs.get(i);
                try {
                    Object object = getConstructorArgument(arg);
                    constructorObjects[i] = object;
                    constructorClasses[i] = object.getClass();
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
        Object object;
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
        } else {
            Constructor<?> constructor = typeClass.getConstructor(String.class);
            object = constructor.newInstance(arg.getValue());
        }
        return object;
    }
}
