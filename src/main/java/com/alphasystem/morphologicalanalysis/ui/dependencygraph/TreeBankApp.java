package com.alphasystem.morphologicalanalysis.ui.dependencygraph;

import com.alphasystem.morphologicalanalysis.graph.model.DependencyGraph;
import com.alphasystem.morphologicalanalysis.ui.common.GraphMetaInfoSelectionDialog;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.DependencyGraphAdapter;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.GraphMetaInfoAdapter;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.CanvasUtil;
import com.alphasystem.morphologicalanalysis.util.RepositoryTool;
import com.alphasystem.morphologicalanalysis.util.SpringContextHelper;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import com.alphasystem.morphologicalanalysis.wordbyword.repository.TokenRepository;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

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
        if (parameters != null) {
            Map<String, String> namedParameters = parameters.getNamed();
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

    private void open(Stage primaryStage, String value) {
        DependencyGraphAdapter dependencyGraphAdapter = null;
        DependencyGraph dependencyGraph = repositoryTool.getDependencyGraph(value);
        if (dependencyGraph != null) {
            dependencyGraphAdapter = new DependencyGraphAdapter(dependencyGraph);
        }
        showStage(primaryStage, dependencyGraphAdapter);
    }

    private void create(Stage stage, String value) {
        DependencyGraphAdapter dependencyGraphAdapter = new DependencyGraphAdapter(new DependencyGraph());
        String[] values = value.split("_");
        Integer chapterNumber;
        Integer verseNumber;
        Integer firstTokenIndex;
        Integer lastTokenIndex;
        try {
            chapterNumber = parseInt(values[0]);
            verseNumber = parseInt(values[1]);
            firstTokenIndex = parseInt(values[2]);
            lastTokenIndex = parseInt(values[3]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }

        TokenRepository tokenRepository = repositoryTool.getRepositoryUtil().getTokenRepository();
        List<Token> tokens = tokenRepository.findByChapterNumberAndVerseNumberAndTokenNumberBetween(chapterNumber,
                verseNumber, firstTokenIndex - 1, lastTokenIndex + 1);
        graphMetaInfoSelectionDialog.setGraphMetaInfo(null);
        Optional<GraphMetaInfoAdapter> result = graphMetaInfoSelectionDialog.showAndWait();
        result.ifPresent(adapterInfo -> {
            DependencyGraph dependencyGraph = canvasUtil.createDependencyGraph(tokens, adapterInfo);
            Alert alert = new Alert(INFORMATION);
            alert.setContentText(format("Graph Created {%s}", dependencyGraph.getDisplayName()));
            alert.showAndWait().filter(response -> response == OK).ifPresent(response ->
                    dependencyGraphAdapter.setDependencyGraph(dependencyGraph));
            showStage(stage, dependencyGraphAdapter);
        });

    }
}
