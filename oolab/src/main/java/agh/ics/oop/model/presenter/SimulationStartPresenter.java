package agh.ics.oop.model.presenter;

import agh.ics.oop.model.SimulationApp;
import agh.ics.oop.model.enums.GenomeVariant;
import agh.ics.oop.model.enums.MapVariant;
import agh.ics.oop.model.records.WorldConfiguration;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SimulationStartPresenter extends SimulationPresenter {
    @FXML private TextField mapWidthField;
    @FXML private TextField mapHeightField;
    @FXML private ChoiceBox<String> mapVariantChoice;
    @FXML private TextField plantStartField;
    @FXML private TextField plantDailyField;
    @FXML private TextField plantEnergyField;
    @FXML private TextField animalStartField;
    @FXML private TextField animalStartEnergyField;
    @FXML private TextField energyAllowingReproductionField;
    @FXML private TextField animalEnergyDailyField;
    @FXML private TextField animalEnergyUsedToReproduceField;
    @FXML private TextField animalGenotypeLengthField;
    @FXML private TextField animalMutationMinimumField;
    @FXML private TextField animalMutationMaximumField;
    @FXML private ChoiceBox<String> genomeVariantChoice;
    @FXML private TextField fireFrequencyField;
    @FXML private TextField burnTimeField;

    @FXML
    private void onSimulationStartClicked() {
        try {
            WorldConfiguration configuration = new WorldConfiguration(
                    Integer.parseInt(mapHeightField.getText()),
                    Integer.parseInt(mapWidthField.getText()),
                    MapVariant.parser(mapVariantChoice.getValue()),
                    Integer.parseInt(plantStartField.getText()),
                    Integer.parseInt(plantDailyField.getText()),
                    Integer.parseInt(plantEnergyField.getText()),
                    Integer.parseInt(animalStartField.getText()),
                    Integer.parseInt(animalStartEnergyField.getText()),
                    Integer.parseInt(energyAllowingReproductionField.getText()),
                    Integer.parseInt(animalEnergyDailyField.getText()),
                    Integer.parseInt(animalEnergyUsedToReproduceField.getText()),
                    Integer.parseInt(animalMutationMinimumField.getText()),
                    Integer.parseInt(animalMutationMaximumField.getText()),
                    Integer.parseInt(animalGenotypeLengthField.getText()),
                    Integer.parseInt(fireFrequencyField.getText()),
                    Integer.parseInt(burnTimeField.getText()),
                    GenomeVariant.parser(genomeVariantChoice.getValue())
            );

            openNewWindow(configuration);
        } catch (Exception e) {
            showAlert("Błąd", "Nieprawidłowe dane", "Sprawdź wprowadzone wartości!", Alert.AlertType.ERROR);
        }
    }

    private void startSimulation(WorldConfiguration configuration) {

    }

    public void openNewWindow(WorldConfiguration config) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
        GridPane viewRoot = loader.load();
        SimulationStartPresenter additionalPresenter = loader.getController();
        Stage additionalStage = new Stage();

        SimulationApp.configureStage(additionalStage, viewRoot);
        additionalStage.show();
        startSimulation(config);

    }
}
