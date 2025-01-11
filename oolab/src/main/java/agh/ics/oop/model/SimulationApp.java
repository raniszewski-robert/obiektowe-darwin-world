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

        stage.setTitle("Darwin World - Menu");
        //Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/a.png")));
        //stage.getIcons().add(icon);
        stage.setMaximized(true);
        stage.setScene(new Scene(viewRoot));
        stage.show();

    }
}
