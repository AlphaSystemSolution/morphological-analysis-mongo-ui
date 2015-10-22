package com.alphasystem.morphologicalanalysis.ui.dependencygraph;

import com.alphasystem.morphologicalanalysis.graph.model.DependencyGraph;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.DependencyGraphAdapter;
import com.alphasystem.morphologicalanalysis.util.RepositoryTool;
import com.alphasystem.morphologicalanalysis.util.SpringContextHelper;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author sali
 */
public class TreeBankApp extends Application {

    static {
        SpringContextHelper.getInstance();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Quranic Morphological Dependency Graph Builder");

        DependencyGraphAdapter dependencyGraphAdapter = null;
        Parameters parameters = getParameters();
        if (parameters != null) {
            List<String> args = parameters.getRaw();
            if (args != null && !args.isEmpty()) {
                String displayName = args.get(0);
                if (!isBlank(displayName)) {
                    DependencyGraph dependencyGraph = RepositoryTool.getInstance().getDependencyGraph(displayName);
                    if (dependencyGraph != null) {
                        dependencyGraphAdapter = new DependencyGraphAdapter(dependencyGraph);
                    }
                }
            }
        }

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
}
