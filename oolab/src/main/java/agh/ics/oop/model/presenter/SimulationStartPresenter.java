package agh.ics.oop.model.presenter;

import agh.ics.oop.model.SimulationApp;
import agh.ics.oop.model.enums.GenomeVariant;
import agh.ics.oop.model.enums.MapVariant;
import agh.ics.oop.model.records.WorldConfiguration;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

public class SimulationStartPresenter extends SimulationPresenter {
    @FXML private Spinner<Integer> mapWidthSpinner;
    @FXML private Spinner<Integer> mapHeightSpinner;
    @FXML private ChoiceBox<String> mapVariantChoice;
    @FXML private Spinner<Integer> plantStartSpinner;
    @FXML private Spinner<Integer> plantDailySpinner;
    @FXML private Spinner<Integer> plantEnergySpinner;
    @FXML private Spinner<Integer> animalStartSpinner;
    @FXML private Spinner<Integer> animalStartEnergySpinner;
    @FXML private Spinner<Integer> energyAllowingReproductionSpinner;
    @FXML private Spinner<Integer> animalEnergyDailySpinner;
    @FXML private Spinner<Integer> animalEnergyUsedToReproduceSpinner;
    @FXML private Spinner<Integer> animalGenotypeLengthSpinner;
    @FXML private Spinner<Integer> animalMutationMinimumSpinner;
    @FXML private Spinner<Integer> animalMutationMaximumSpinner;
    @FXML private ChoiceBox<String> genomeVariantChoice;
    @FXML private Spinner<Integer> fireFrequencySpinner;
    @FXML private Spinner<Integer> burnTimeSpinner;

    @FXML
    private void onSimulationStartClicked() {
        try {
            WorldConfiguration configuration = new WorldConfiguration(
                    mapHeightSpinner.getValue(),
                    mapWidthSpinner.getValue(),
                    MapVariant.parser(mapVariantChoice.getValue()),
                    plantStartSpinner.getValue(),
                    plantDailySpinner.getValue(),
                    plantEnergySpinner.getValue(),
                    animalStartSpinner.getValue(),
                    animalStartEnergySpinner.getValue(),
                    energyAllowingReproductionSpinner.getValue(),
                    animalEnergyDailySpinner.getValue(),
                    animalEnergyUsedToReproduceSpinner.getValue(),
                    animalMutationMinimumSpinner.getValue(),
                    animalMutationMaximumSpinner.getValue(),
                    animalGenotypeLengthSpinner.getValue(),
                    fireFrequencySpinner.getValue(),
                    burnTimeSpinner.getValue(),
                    GenomeVariant.parser(genomeVariantChoice.getValue())
            );

            openNewWindow(configuration);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            showAlert("Błąd", "Nieprawidłowe dane", "Sprawdź wprowadzone wartości!", Alert.AlertType.ERROR);
        }
    }

    public void openNewWindow(WorldConfiguration config) throws IOException, InterruptedException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
        BorderPane viewRoot = loader.load();
        SimulationWorldPresenter additionalPresenter = loader.getController();
        Stage additionalStage = new Stage();
        SimulationApp.configureStage(additionalStage, viewRoot);
        additionalStage.show();


        additionalPresenter.startSimulation(config);

    }
}
