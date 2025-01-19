package agh.ics.oop.model.presenter;

import agh.ics.oop.model.*;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Genotype;
import agh.ics.oop.model.elements.Square;
import agh.ics.oop.model.interfaces.MapChangeListener;
import agh.ics.oop.model.interfaces.WorldMap;
import agh.ics.oop.model.records.WorldConfiguration;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;

public class SimulationWorldPresenter extends SimulationPresenter implements MapChangeListener {

    public Button pauseResumeButton;
    public Label followedGenome;
    public Label followedEnergy;
    public Label followedEatenGrass;
    public Label followedHeader;
    public Label followedAge;
    public Label currentDay;
    public Label followedDateOfDeath;
    public Label followedChildrenNumber;
    public Label followedOffspringNumber;
    public Button popularGenomeButton;
    public Slider zoomSlider;
    public BorderPane rootPane;
    public Button highlightGrassButton;
    private AbstractWorldMap worldMap;
    private Simulation simulation;
    @FXML
    private GridPane mapGrid = new GridPane();
    private int height;
    private int width;
    private boolean showEnergy;
    @FXML
    List<List<ImageView>> gridLabels = new ArrayList<>();

    @FXML
    private Label animalsCountLabel;
    @FXML
    private Label plantCountLabel;
    @FXML
    private Label avgEnergyLabel;
    @FXML
    private Label freeSpaceLabel;
    @FXML
    private Label genotypeLabel;
    @FXML
    private Label avgAgeLabel;
    @FXML
    private Label avgOffspringLabel;
    @FXML
    private Label dominantGenotypePositionsLabel;
    @FXML
    private VBox infoVBox;
    @FXML
    private HBox deathHBox;


    private boolean paused = false;
    private boolean saveEveryDayToCSV = false;
    private boolean highlight = false;
    private boolean highlightEQ=true;
    private boolean mapVersion;

    private Animal followedAnimal = null;


    private Image dirt = new Image("dirt.png");
    private Image grass = new Image("grass.png");
    private Image animal = new Image("animal.png");
    private Image fire = new Image("fire.png");
    private List<Image> animalImages = new ArrayList<>();
    private int energyForBeingFull;
    private Statistics stats;

    //przesunięcie GridPane
    private double translateX = 0;
    private double translateY = 0;

    private FileWriter writer = null;



    public void startSimulation(WorldConfiguration config) throws InterruptedException {
        height = config.mapHeight();
        width = config.mapWidth();
        simulation = new Simulation(config, this);
        this.worldMap = simulation.getWorldMap();
        stats = new Statistics(worldMap);
        drawEmptyMap();
        SimulationEngine simulationEngine = new SimulationEngine();
        simulationEngine.runAsyncInThreadPool(List.of(simulation));


    }

    public void drawEmptyMap(){
        for (int row = 0; row < height; row++) {
            List<ImageView> rowLabels = new ArrayList<>();
            for (int col = 0; col < width; col++) {
                ImageView imageView = new ImageView(dirt);

                imageView.setFitWidth(20);
                imageView.setFitHeight(20);
                int finalRow = row;
                int finalCol = col;
                imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        handleMouseClick(finalRow, finalCol);
                    }
                });
                rowLabels.add(imageView);
                mapGrid.add(imageView, col, row);
            }
            gridLabels.add(rowLabels);
        }
        for (int row=0; row < height; row++) {
            mapGrid.getRowConstraints().add(new RowConstraints(20));
        }
        for (int col=0; col< width; col++) {
            mapGrid.getColumnConstraints().add(new ColumnConstraints(20));
        }
    }

    public void drawMap() {
        for (int y = 0; y<height; y++) {
            for (int x = 0; x < width; x++) {
                ImageView imageView = gridLabels.get(y).get(x);
                imageView.setImage(dirt);
            }
        }

        for(Map.Entry<Vector2d, Square> entry : worldMap.getMapSquares().entrySet()){
            int x = entry.getKey().getX();
            int y = entry.getKey().getY();
            ImageView imageView = null;
            try {
                imageView = gridLabels.get(y).get(x);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            Square square = entry.getValue();

            if(square.hasPlant()){
                imageView.setImage(grass);
                if(square.onFire()) {
                    imageView.setImage(fire);
                }
            }
            if(!square.getAnimals().isEmpty()){
                    imageView.setImage(animal);
            }

        }
    }
    private void handleMouseClick(int finalRow, int finalCol) {
    }
    public void highlightPopularGenome(ActionEvent actionEvent) {
    }

    public void pauseResume(ActionEvent actionEvent) {
        if (paused) {
            simulation.resume();
            paused = false;
            pauseResumeButton.setText("Pause");
        } else {
            simulation.pause();
            paused = true;
            pauseResumeButton.setText("Resume");
        }
    }

    public void highlightDominantGrass(ActionEvent actionEvent) {
    }

    public void changeZoom() {
        mapGrid.setScaleX((zoomSlider.getValue()+1)/40);
        mapGrid.setScaleY((zoomSlider.getValue()+1)/40);
    }

    public void movePane(KeyCode keyCode) {
        switch (keyCode) {
            case LEFT -> translateX+=(zoomSlider.getValue()+1)/4;
            case RIGHT -> translateX-=(zoomSlider.getValue()+1)/4;
            case UP -> translateY+=(zoomSlider.getValue()+1)/4;
            case DOWN -> translateY-=(zoomSlider.getValue()+1)/4;
        }
        mapGrid.setTranslateX(translateX);
        mapGrid.setTranslateY(translateY);
    }

    @Override
    public void mapChanged(WorldMap worldMap, List<String> messages, List<Vector2d> list) {
        Platform.runLater(this::drawMap);
    }

    public void updateStatistics() {
        int animalsCount = stats.countAnimals();
        int plantCount = stats.countPlants();
        double avgEnergy = stats.getAverageEnergy();
        int freeSpaces = stats.countFreeSquares();
        Genotype dominantGenotype = stats.getMostCommonGenotype();
        String avgAge = (String.format("%.2f", stats.getAverageLifeLength()));
        double avgOffspring = stats.getAverageChildrenCount();
        Platform.runLater(() -> {
            animalsCountLabel.setText(Integer.toString(animalsCount));
            plantCountLabel.setText(Integer.toString(plantCount));
            avgEnergyLabel.setText(Double.toString(avgEnergy));
            freeSpaceLabel.setText(Integer.toString(freeSpaces));
            if (dominantGenotype != null) {
                dominantGenotypePositionsLabel.setText(dominantGenotype.toString());
            }
            avgAgeLabel.setText(avgAge);
            avgOffspringLabel.setText(Double.toString(avgOffspring));
        });
    }
}
