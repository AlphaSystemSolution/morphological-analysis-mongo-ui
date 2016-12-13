package com.alphasystem.morphologicalanalysis.ui.dependencygraph;

import com.alphasystem.morphologicalanalysis.common.model.VerseTokenPairGroup;
import com.alphasystem.morphologicalanalysis.graph.model.DependencyGraph;
import com.alphasystem.morphologicalanalysis.ui.common.GraphMetaInfoSelectionDialog;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.DependencyGraphAdapter;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.GraphMetaInfoAdapter;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.GraphNodeAdapter;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.TerminalNodeAdapter;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.CanvasUtil;
import com.alphasystem.morphologicalanalysis.util.RepositoryTool;
import com.alphasystem.morphologicalanalysis.util.SpringContextHelper;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import static com.alphasystem.morphologicalanalysis.ui.common.Global.fromFont;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.isTerminal;
import static com.alphasystem.morphologicalanalysis.util.VerseTokensPairsReader.createGroup;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static javafx.scene.control.Alert.AlertType.INFORMATION;
import static javafx.scene.control.ButtonType.OK;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author sali
 */
public class TreeBankApp extends Application {

    private static final String OPEN_ARG = "open";
    private static final String CREATE_ARG = "create";

    static {
        SpringContextHelper.getInstance();
    }

    private final RepositoryTool repositoryTool = RepositoryTool.getInstance();
    private final CanvasUtil canvasUtil = CanvasUtil.getInstance();
    private GraphMetaInfoSelectionDialog graphMetaInfoSelectionDialog = new GraphMetaInfoSelectionDialog();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Quranic Morphological Dependency Graph Builder");

        Parameters parameters = getParameters();
        boolean noArg = parameters == null;
        if (!noArg) {
            Map<String, String> namedParameters = parameters.getNamed();
            noArg = namedParameters.isEmpty();
            Set<Entry<String, String>> entries = namedParameters.entrySet();
            for (Entry<String, String> parameter : entries) {
                String name = parameter.getKey();
                String value = parameter.getValue();
                if (isBlank(name) || isBlank(value)) {
                    continue;
                }
                if (OPEN_ARG.equalsIgnoreCase(name)) {
                    open(primaryStage, value);
                } else if (CREATE_ARG.equalsIgnoreCase(name)) {
                    create(primaryStage, value);
                }
            }
        }

        if (noArg) {
            showStage(primaryStage, null);
        }
    }

    private void showStage(Stage primaryStage, DependencyGraphAdapter dependencyGraphAdapter) {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());

        Scene scene = new Scene(new TreeBankPane(dependencyGraphAdapter));
        primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void open(Stage primaryStage, String displayName) {
        DependencyGraphAdapter dependencyGraphAdapter = null;
        DependencyGraph dependencyGraph = repositoryTool.getRepositoryUtil().getDependencyGraph(displayName);
        if (dependencyGraph != null) {
            dependencyGraphAdapter = new DependencyGraphAdapter(dependencyGraph);
            GraphMetaInfoAdapter graphMetaInfo = dependencyGraphAdapter.getGraphMetaInfo();
            for (GraphNodeAdapter nodeAdapter : dependencyGraphAdapter.getGraphNodes()) {
                if (isTerminal(nodeAdapter)) {
                    TerminalNodeAdapter terminalNodeAdapter = (TerminalNodeAdapter) nodeAdapter;
                    graphMetaInfo.setTerminalFont(fromFont(terminalNodeAdapter.getFont()));
                    graphMetaInfo.setTranslationFont(fromFont(terminalNodeAdapter.getTranslationFont()));
                    graphMetaInfo.setPosFont(fromFont(terminalNodeAdapter.getPartOfSpeeches().get(0).getFont()));
                    break;
                }
            }
        }
        showStage(primaryStage, dependencyGraphAdapter);
    }

    private void create(Stage stage, String displayName) {
        graphMetaInfoSelectionDialog.setGraphMetaInfo(null);
        Optional<GraphMetaInfoAdapter> result = graphMetaInfoSelectionDialog.showAndWait();
        result.ifPresent(metaInfo -> {
            int i = displayName.indexOf('|');
            Integer chapterNumber = parseInt(displayName.substring(0, i));
            VerseTokenPairGroup group = createGroup(chapterNumber, displayName.substring(i + 1));
            DependencyGraph dependencyGraph = canvasUtil.createDependencyGraph(group, metaInfo);
            DependencyGraphAdapter dependencyGraphAdapter = new DependencyGraphAdapter(new DependencyGraph());
            Alert alert = new Alert(INFORMATION);
            alert.setContentText(format("Graph Created {%s}", dependencyGraph.getDisplayName()));
            alert.showAndWait().filter(response -> response == OK).ifPresent(response -> {
                dependencyGraphAdapter.setDependencyGraph(dependencyGraph);
                dependencyGraphAdapter.setGraphMetaInfo(metaInfo);
            });
            showStage(stage, dependencyGraphAdapter);
        });

    }
}
