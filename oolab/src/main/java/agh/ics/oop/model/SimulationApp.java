package agh.ics.oop.model;

import agh.ics.oop.model.presenter.SimulationPresenter;
import agh.ics.oop.model.presenter.SimulationStartPresenter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.util.Objects;

public class SimulationApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("start.fxml"));
        GridPane viewRoot = loader.load();
        SimulationStartPresenter presenter = loader.getController();
        configureStage(stage, viewRoot);
        stage.setTitle("Darwin World - Menu");
        stage.show();

    }

    public static void configureStage(Stage primaryStage, GridPane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation app");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
        //Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/a.png")));
        //stage.getIcons().add(icon);
    }
}
