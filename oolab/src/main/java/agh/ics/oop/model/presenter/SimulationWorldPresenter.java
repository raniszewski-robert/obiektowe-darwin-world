package agh.ics.oop.model.presenter;

import agh.ics.oop.model.AbstractWorldMap;
import agh.ics.oop.model.Simulation;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Square;
import agh.ics.oop.model.records.WorldConfiguration;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;

public class SimulationWorldPresenter extends SimulationPresenter {

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
    private Label grassCountLabel;
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
    private List<Image> animalImages = new ArrayList<>();
    private int energyForBeingFull;


    //przesunięcie GridPane
    private double translateX = 0;
    private double translateY = 0;

    private FileWriter writer = null;



    public void startSimulation(WorldConfiguration config){
        height = config.mapHeight();
        width = config.mapWidth();
        Simulation simulation = new Simulation(config);
        this.worldMap = simulation.getWorldMap();
        drawEmptyMap();
        drawMap();


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
            }
            else{
                if(!square.getAnimals().isEmpty()){
                    imageView.setImage(animal);
                }
            }
        }
    }
    private void handleMouseClick(int finalRow, int finalCol) {
    }

    public void changeZoom(MouseEvent mouseEvent) {
    }

    public void highlightPopularGenome(ActionEvent actionEvent) {
    }

    public void pauseResume(ActionEvent actionEvent) {
    }

    public void highlightDominantGrass(ActionEvent actionEvent) {
    }
}
