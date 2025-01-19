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
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class SimulationWorldPresenter extends SimulationPresenter implements MapChangeListener {

    private CSVLogger csvLogger;
    public Button pauseResumeButton;
    public Label clickedGenome;
    public Label clickedEnergy;
    public Label clickedHeader;
    public Label clickedAge;
    public Label currentDay;
    public Label clickedChildrenNumber;
    public Button popularGenomeButton;
    public Slider zoomSlider;
    public BorderPane rootPane;
    public Button highlightGrassButton;
    public Label clickedDescendantsNumber;
    public Label clickedEatenGrass;
    public Label clickedDayOfDeath;
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

    AbstractWorldMap temporaryWorldMap;
    private boolean paused = false;
    private boolean saveEveryDayToCSV = false;
    private boolean highlightField = false;
    private boolean highlightGenome = false;

    private Image dirt = new Image("dirt.png");
    private Image grass = new Image("grass.png");
    private Image animal0 = new Image("animal_0.png");
    private Image animal1 = new Image("animal_1.png");
    private Image animal2 = new Image("animal_2.png");
    private Image animal3 = new Image("animal_3.png");
    private Image animal4 = new Image("animal_4.png");
    private Image animal5 = new Image("animal_5.png");
    private Image animal6 = new Image("animal_6.png");
    private Image animal7 = new Image("animal_7.png");
    private Image fire = new Image("firegrass.png");
    private List<Image> animalImages = new ArrayList<>();
    private int energyForBeingFull;
    private Statistics stats;
    public Animal selectedAnimal = null;
    private double translateX = 0;
    private double translateY = 0;

    private FileWriter writer = null;

    public void startSimulation(WorldConfiguration config, Stage additionalStage) throws InterruptedException {
        additionalStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Potwierdzenie");
                alert.setHeaderText("Czy na pewno chcesz zamknąć aplikację?");
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        System.out.println("Okno zostalo zamkniete");
                    } else {
                        event.consume(); // Anuluje zamknięcie okna
                    }
                });
                simulation.setClosedWindow(true);
            }
        });

        if (config.saveToCSV()) {
            try {
                // Inicjalizacja loggera CSV
                csvLogger = new CSVLogger();
            } catch (IOException e) {
                System.out.println("Error creating CSV logger: " + e.getMessage());
            }
        }

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

        zoomSlider.setValue(40);
        zoomSlider.setMax(300);
        rootPane.addEventFilter(KeyEvent.ANY, event -> {
            if (event.getEventType() == KeyEvent.KEY_RELEASED && event.getCode() == KeyCode.SPACE) {
                pauseResume();
            } else {
                movePane(event.getCode());
            }
            event.consume();
        });

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

    public void setAnimalColorBasedOnEnergy(ImageView imageView, double energy) {
        ColorAdjust colorAdjust = new ColorAdjust();
        double normalizedEnergy = Math.max(-1, Math.min(1, (energy - 50) / 50.0));
        double hue = normalizedEnergy * 0.27;
        colorAdjust.setSaturation(1);
        colorAdjust.setHue(hue);
        imageView.setEffect(colorAdjust);
    }
    public void setSelectedAnimalColor(){
        int y = selectedAnimal.getPosition().getY();
        int x = selectedAnimal.getPosition().getX();
        ImageView imageView = gridLabels.get(y).get(x);
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setSaturation(-1);
        colorAdjust.setBrightness(0.5);
        imageView.setEffect(colorAdjust);
    }

    public void drawMap() {
        for (int y = 0; y<height; y++) {
            for (int x = 0; x < width; x++) {
                ImageView imageView = gridLabels.get(y).get(x);
                imageView.setImage(dirt);
                imageView.setEffect(null);
            }
        }

        for(Map.Entry<Vector2d, Square> entry : worldMap.getMapSquares().entrySet()){
            int x = entry.getKey().getX();
            int y = entry.getKey().getY();
            ImageView imageView = null;
            imageView = gridLabels.get(y).get(x);

            Square square = entry.getValue();

            if(square.hasPlant()){
                imageView.setImage(grass);
                if(square.onFire()) {
                    imageView.setImage(fire);
                }
            }

            Queue<Animal> AnimalsQueue = square.getAnimalsAsQueue();
            if (!AnimalsQueue.isEmpty()) {
                Animal currAnimal = AnimalsQueue.poll();
                setAnimalPicture(imageView, currAnimal);
                setAnimalColorBasedOnEnergy(imageView, currAnimal.getEnergy());
            }
        }
        if (selectedAnimal != null){
            setAnimalStatistics(selectedAnimal);
            setSelectedAnimalColor();
        }
        highlightField = false;
        highlightGenome = false;
    }

    private void setAnimalPicture(ImageView imageView, Animal animal) {
        int direction = animal.getDirection();
        switch (direction) {
            case 0 -> imageView.setImage(animal4);
            case 1 -> imageView.setImage(animal3);
            case 2 -> imageView.setImage(animal2);
            case 3 -> imageView.setImage(animal1);
            case 4 -> imageView.setImage(animal0);
            case 5 -> imageView.setImage(animal7);
            case 6 -> imageView.setImage(animal6);
            case 7 -> imageView.setImage(animal5);
            default -> System.out.println("Unknown direction: " + direction);
        }
    }
    private void handleMouseClick(int y, int x) {
        if(!paused){return;}
        Vector2d position = new Vector2d(x, y);
        PriorityQueue<Animal> animals = new PriorityQueue<>(Comparator.comparingInt(Animal::getEnergy).reversed()
                .thenComparingInt(Animal::getAge).reversed()
                .thenComparingInt(Animal::getChildrenCount).reversed());
        for (Animal animal : this.worldMap.getAnimals()) {
            if(animal.getPosition().equals(position)){
                animals.add(animal);
            }
        }
        if (!animals.isEmpty()) {
            if(selectedAnimal != null){
                int _y = selectedAnimal.getPosition().getY();
                int _x = selectedAnimal.getPosition().getX();
                ImageView imageView = gridLabels.get(_y).get(_x);
                setAnimalColorBasedOnEnergy(imageView, selectedAnimal.getEnergy());
            }
            selectedAnimal = animals.poll();
            setSelectedAnimalColor();
            setAnimalStatistics(selectedAnimal);
        }
        else{
            clearAnimalStatistics();
        }
    }

    public void clearAnimalStatistics() {
        clickedHeader.setText("");
        clickedGenome.setText("");
        clickedEnergy.setText("");
        clickedEatenGrass.setText("");
        clickedChildrenNumber.setText("");
        clickedDescendantsNumber.setText("");
        clickedAge.setText("");
    }
    public void setAnimalStatistics(Animal animal) {
        if(animal == null) return;
        clickedHeader.setText("Animal at: " + animal.getPosition());
        Genotype genotype = animal.getGenotype();
        clickedGenome.setText("Genome:" + highlightElement(genotype.getGenome(), genotype.getCurrentGenomeIndex() ) );
        clickedEnergy.setText("Energy: " + animal.getEnergy());
        clickedEatenGrass.setText("Eaten plant: " + animal.getPlantCount());
        clickedChildrenNumber.setText("Children number: " + animal.getChildrenCount());
        clickedDescendantsNumber.setText("Descendant number: "+ animal.getDescendantNumber());
        clickedAge.setText("Age: " + animal.getAge());
        if(animal.isDead()){
            clickedDayOfDeath.setText("Day of death: " + simulation.getDayCounter());
            selectedAnimal = null;
        }
    }
    public void highlightPopularGenome(ActionEvent actionEvent) {
        Genotype popularGenome = stats.getMostCommonGenotype();
        PriorityQueue<Animal> animals = new PriorityQueue<>(Comparator.comparingInt(Animal::getEnergy).reversed()
                .thenComparingInt(Animal::getAge).reversed()
                .thenComparingInt(Animal::getChildrenCount).reversed());
        for (Animal animal : this.worldMap.getAnimals()) {
            if(animal.getGenotype().equals(popularGenome)){
                animals.add(animal);
            }
        }
        if (!animals.isEmpty()) {
            for(Animal animal : animals) {
                int _y = animal.getPosition().getY();
                int _x = animal.getPosition().getX();
                ImageView imageView = gridLabels.get(_y).get(_x);
                if(!highlightGenome){
                highlightView(imageView);
                }
                else{
                    ColorAdjust colorAdjust = new ColorAdjust();
                    colorAdjust.setSaturation(0);
                    colorAdjust.setBrightness(0);
                    imageView.setEffect(colorAdjust);
                }
            }
        }
        highlightGenome = !highlightGenome;
    }

    public void pauseResume() {
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

    public void highlightDominantGrassButton(ActionEvent actionEvent) {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Vector2d position = new Vector2d(col, row);
                if(worldMap.isInJungle(position)){
                    ImageView imageView = gridLabels.get(row).get(col);
                    ColorAdjust colorAdjust = new ColorAdjust();
                    if(!highlightField){
                        highlightView(imageView);
                    }
                    else{
                        colorAdjust.setSaturation(0);
                        colorAdjust.setBrightness(0);
                        imageView.setEffect(colorAdjust);
                    }
                }
            }
        }
        highlightField = !highlightField;
    }

    public void highlightView(ImageView imageView){
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setSaturation(-1);
        colorAdjust.setBrightness(0.5);
        imageView.setEffect(colorAdjust);
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
        temporaryWorldMap = ((AbstractWorldMap) worldMap);
    }

    public void setSaveEveryDayToCSV(boolean saveEveryDayToCSV) {
        this.saveEveryDayToCSV = saveEveryDayToCSV;
    }

    public void updateStatistics(int currDay, boolean saveToCSV) {
        int animalsCount = stats.countAnimals();
        int plantCount = stats.countPlants();
        double avgEnergy = stats.getAverageEnergy();
        int freeSpaces = stats.countFreeSquares();
        Genotype dominantGenotype = stats.getMostCommonGenotype();
        String avgAge = (String.format("%.2f", stats.getAverageLifeLength()));
        double avgOffspring = stats.getAverageChildrenCount();

        if (saveToCSV && csvLogger != null) {
            csvLogger.logStatistics(currDay, animalsCount, plantCount, avgEnergy, freeSpaces,
                    dominantGenotype != null ? dominantGenotype.toString() : null, avgAge, avgOffspring);
        }

        Platform.runLater(() -> {
            currentDay.setText("DAY: " + currDay);
            animalsCountLabel.setText(Integer.toString(animalsCount));
            plantCountLabel.setText(Integer.toString(plantCount));
            avgEnergyLabel.setText(Double.toString(avgEnergy));
            freeSpaceLabel.setText(Integer.toString(freeSpaces));
            if (dominantGenotype != null) {
                dominantGenotypePositionsLabel.setText(dominantGenotype.toString());
            }
            avgAgeLabel.setText(avgAge);
            avgOffspringLabel.setText(Double.toString(avgOffspring));
            currentDay.setText("Day number: " + simulation.getDayCounter());
        });
    }

    public static List<Object> highlightElement(List<Integer> numbers, int indexToHighlight) {
        List<Object> result = new ArrayList<>();
        for (int i = 0; i < numbers.size(); i++) {
            if (i == indexToHighlight) {
                result.add(List.of(numbers.get(i)));
            } else {
                result.add(numbers.get(i));
            }
        }
        return result;
    }
}
