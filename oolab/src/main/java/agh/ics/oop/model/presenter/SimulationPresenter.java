package agh.ics.oop.model.presenter;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Set;

abstract public class SimulationPresenter extends Stage {
    @FXML
    protected Pane rootPane;

    protected void showAlert(String header, String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(header);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

}