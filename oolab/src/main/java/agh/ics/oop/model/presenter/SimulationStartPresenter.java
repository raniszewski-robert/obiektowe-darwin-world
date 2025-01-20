package agh.ics.oop.model.presenter;

import agh.ics.oop.model.ConfigReader;
import agh.ics.oop.model.SimulationApp;
import agh.ics.oop.model.enums.GenomeVariant;
import agh.ics.oop.model.enums.MapVariant;
import agh.ics.oop.model.records.WorldConfiguration;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.swing.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class SimulationStartPresenter extends SimulationPresenter {
    @FXML public ChoiceBox<String> configChoice;
    public CheckBox saveConfig;
    @FXML private Spinner<Integer> mapWidthSpinner;
    @FXML private Spinner<Integer> mapHeightSpinner;
    @FXML private ChoiceBox<String> mapVariantChoice;
    @FXML private Spinner<Integer> plantStartSpinner;
    @FXML private Spinner<Integer> plantDailySpinner;
    @FXML private Spinner<Integer> plantEnergySpinner;
    @FXML private Spinner<Integer> animalStartSpinner;
    @FXML private Spinner<Integer> animalStartEnergySpinner;
    @FXML private Spinner<Integer> energyAllowingReproductionSpinner;
    @FXML private Spinner<Integer> animalEnergyUsedToReproduceSpinner;
    @FXML private Spinner<Integer> animalGenotypeLengthSpinner;
    @FXML private Spinner<Integer> animalMutationMinimumSpinner;
    @FXML private Spinner<Integer> animalMutationMaximumSpinner;
    @FXML private ChoiceBox<String> genomeVariantChoice;
    @FXML private Spinner<Integer> fireFrequencySpinner;
    @FXML private Spinner<Integer> burnTimeSpinner;
    @FXML private VBox fireOptions;
    @FXML private CheckBox saveToCSVCheckBox;

    @FXML
    private void onSimulationStartClicked() {
        try {
            boolean saveEveryDayToCSV = saveToCSVCheckBox.isSelected();
            if(animalMutationMaximumSpinner.getValue() < animalMutationMinimumSpinner.getValue()) {
                showAlert("Błąd", "Nieprawidłowe dane", "Minimalna liczba mutacji musi być mniejsza od maksymalnej!", Alert.AlertType.ERROR);
                return;
            }
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
                    animalEnergyUsedToReproduceSpinner.getValue(),
                    animalMutationMinimumSpinner.getValue(),
                    animalMutationMaximumSpinner.getValue(),
                    animalGenotypeLengthSpinner.getValue(),
                    fireFrequencySpinner.getValue(),
                    burnTimeSpinner.getValue(),
                    GenomeVariant.parser(genomeVariantChoice.getValue()),
                    saveEveryDayToCSV
            );
            openNewWindow(configuration);

            if(saveConfig.isSelected()) {
                saveChosenConfig();
            }
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

        additionalPresenter.startSimulation(config, additionalStage);
    }

    @FXML
    public void initialize() {
        // Nasłuchiwanie zmian wyboru w mapVariantChoice
        mapVariantChoice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if ("Pożary".equals(newValue)) {
                fireOptions.setVisible(true); // Pokaż elementy związane z pożarami
            } else {
                fireOptions.setVisible(false); // Ukryj elementy związane z pożarami
            }
        });

        configChoice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if ("wybierz z pliku...".equals(newValue)) {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Pliki tekstowe", "*.txt"));

                    Stage stage = new Stage();
                    File file = fileChooser.showOpenDialog(stage);

                    // Sprawdź, czy plik został wybrany
                    if (file != null) {
                        // Zrób coś z wybranym plikiem
                        setChosenConfig(file.getAbsolutePath());
                    }
                } else {
                    setChosenConfig(newValue);
                }
            }
        });
    }

    public void setChosenConfig(String value) {
        String file;
        ConfigReader configReader = new ConfigReader();

        if(configChoice.getValue().equals("wybierz z pliku...")){
            file = value;
        }
        else{
        file = "oolab/src/main/resources/configs/" + value + ".txt";
        }
        String [] configs = configReader.readFile(file);


        try {
            mapWidthSpinner.getValueFactory().setValue(Integer.parseInt(configs[0]));
            mapHeightSpinner.getValueFactory().setValue(Integer.parseInt(configs[1]));
            animalStartSpinner.getValueFactory().setValue(Integer.parseInt(configs[2]));
            animalStartEnergySpinner.getValueFactory().setValue(Integer.parseInt(configs[3]));
            genomeVariantChoice.setValue(configs[4]);
            mapVariantChoice.setValue(configs[5]);
            fireFrequencySpinner.getValueFactory().setValue(Integer.parseInt(configs[6]));
            burnTimeSpinner.getValueFactory().setValue(Integer.parseInt(configs[7]));
            animalGenotypeLengthSpinner.getValueFactory().setValue(Integer.parseInt(configs[8]));
            animalMutationMinimumSpinner.getValueFactory().setValue(Integer.parseInt(configs[9]));
            animalMutationMaximumSpinner.getValueFactory().setValue(Integer.parseInt(configs[10]));
            plantStartSpinner.getValueFactory().setValue(Integer.parseInt(configs[11]));
            plantDailySpinner.getValueFactory().setValue(Integer.parseInt(configs[12]));
            plantEnergySpinner.getValueFactory().setValue(Integer.parseInt(configs[13]));
            energyAllowingReproductionSpinner.getValueFactory().setValue(Integer.parseInt(configs[14]));
            animalEnergyUsedToReproduceSpinner.getValueFactory().setValue(Integer.parseInt(configs[15]));
        } catch (NumberFormatException e) {
            System.out.println("NIEPRAWIDŁOWY TEXT!!");
            showAlert("Błąd", "Nieprawidłowe dane", "Sprawdź wprowadzone wartości w pliku!", Alert.AlertType.ERROR);
            configChoice.getSelectionModel().clearSelection();
        }
    }

    public void saveChosenConfig() throws IOException{
        StringBuilder configData = new StringBuilder();

        // Pobieranie wartości z elementów UI
        configData.append(mapWidthSpinner.getValue()).append(",");
        configData.append(mapHeightSpinner.getValue()).append(",");
        configData.append(animalStartSpinner.getValue()).append(",");
        configData.append(animalStartEnergySpinner.getValue()).append(",");
        configData.append(genomeVariantChoice.getValue()).append(",");
        configData.append(mapVariantChoice.getValue()).append(",");

        // Pożary (jeśli widoczne)
        if (mapVariantChoice.getValue().equals("Pożary")) {
            configData.append(fireFrequencySpinner.getValue()).append(",");
            configData.append(burnTimeSpinner.getValue()).append(",");
        } else {
            configData.append(",,"); // Puste wartości, gdy opcja pożarów jest niewidoczna
        }

        configData.append(animalGenotypeLengthSpinner.getValue()).append(",");
        configData.append(animalMutationMinimumSpinner.getValue()).append(",");
        configData.append(animalMutationMaximumSpinner.getValue()).append(",");
        configData.append(plantStartSpinner.getValue()).append(",");
        configData.append(plantDailySpinner.getValue()).append(",");
        configData.append(plantEnergySpinner.getValue()).append(",");
        configData.append(energyAllowingReproductionSpinner.getValue()).append(",");
        configData.append(animalEnergyUsedToReproduceSpinner.getValue());

        File directory = new File("Configs");
        if (!directory.exists()) {
            directory.mkdir();
        }

        // Generowanie unikalnej nazwy pliku z timestampem
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "Configs/simulation_configs_" + timestamp + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(String.valueOf(configData));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
