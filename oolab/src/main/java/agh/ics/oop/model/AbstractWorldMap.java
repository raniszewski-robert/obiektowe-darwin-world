package agh.ics.oop.model;

import agh.ics.oop.model.elements.*;
import agh.ics.oop.model.enums.GenomeVariant;
import agh.ics.oop.model.interfaces.MapChangeListener;
import agh.ics.oop.model.interfaces.WorldMap;
import agh.ics.oop.model.records.Boundary;
import java.util.*;

public abstract class AbstractWorldMap implements WorldMap {
    private final Vector2d lowerLeft;
    private final Vector2d upperRight;
    protected final int jungleLowerY;
    protected final int jungleUpperY;
    private final List<MapChangeListener> observers = new ArrayList<>();
    protected List<Animal> animals;
    protected List<Plant> plants;
    private List<Animal> deadAnimals = new ArrayList<>();
    protected final HashMap<Vector2d, Square> mapSquares;
    public int width;
    public int height;
    private int freeFields;
    private GenomeVariant genomeVariant;

    protected AbstractWorldMap(int width, int height, GenomeVariant genomeVariant) {
        this.width = width;
        this.height = height;
        this.animals = new ArrayList<>();
        this.plants = new ArrayList<>();
        this.mapSquares = new HashMap<>();
        this.jungleLowerY = (int) (0.4 * height);
        this.jungleUpperY = (int) (0.6 * height) - 1;
        this.lowerLeft = new Vector2d(0, 0);
        this.upperRight = new Vector2d(this.width-1, this.height-1);
        this.freeFields = width * height;
        this.genomeVariant = genomeVariant;
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                Square square = new Square();
                mapSquares.put(new Vector2d(i, j), square);
            }
        }
    }

    public Collection<Square> getAllSquares(){
        return mapSquares.values();
    }

    public HashMap<Vector2d, Square> getMapSquares() {
        return mapSquares;
    }

    @Override
    public Boundary getCurrentBounds() {
        return new Boundary(lowerLeft, upperRight);
    }

    public GenomeVariant getGenomeVariant() {
        return genomeVariant;
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    @Override
    public Square squareAt(Vector2d position) {
        return mapSquares.get(position);
    }

    @Override
    public boolean isInMap(Vector2d position) {
        return (position.follows(lowerLeft) && position.precedes(upperRight));
    }

    public boolean isInJungle(Vector2d position) {
        return position.getY() >= jungleLowerY && position.getY() <= jungleUpperY;
    }

    public void addPlant(Plant plant){
        squareAt(plant.getPosition()).addPlant(plant);
        this.plants.add(plant);
    }

    public void removePlant(Plant plant){
        this.plants.remove(plant);
        squareAt(plant.getPosition()).removePlant();
    }

    public void addAnimal(Animal animal){
        squareAt(animal.getPosition()).addAnimal(animal);
        this.animals.add(animal);
    }

    public void removeAnimal(Animal animal){
        this.animals.remove(animal);
        squareAt(animal.getPosition()).removeAnimal(animal);
    }

    private void recalculateFreeFields() {
        freeFields = (int) mapSquares.values().stream()
                .filter(square -> !square.hasPlant())
                .count();
    }

    public void growPlants(int numberOfPlants) {
        if (freeFields < numberOfPlants) {
            numberOfPlants = freeFields;
        }
        for(int i = 0; i < numberOfPlants; i++) {
            Random random = new Random();
            Vector2d position;
            double chance;
            Square currentSquare;
            do {
                position = this.getRandomPosition();
                currentSquare = squareAt(position);
                chance = isInJungle(position) ? 0.8 : 0.2; // 80% dla dżungli, 20% poza
            } while (currentSquare.hasPlant() || random.nextDouble() > chance);

            freeFields--;
            addPlant(new Plant(position));
        }
    }

    public void eatPlants(int plantEnergy) {
        for (Square square : getAllSquares()) {
            if (square.hasPlant()){
                Plant currPlant = square.getPlant();
                PriorityQueue<Animal> currAnimals = new PriorityQueue<>(square.getAnimalsAsQueue());
                if (!currAnimals.isEmpty()) {
                    Animal strongestAnimal = currAnimals.poll();
                    strongestAnimal.setEnergy(strongestAnimal.getEnergy() + plantEnergy);
                    strongestAnimal.addPlantCount();
                    removePlant(currPlant);
                }
            }
        }

        recalculateFreeFields();
    }

    public void createAnimals(int numberOfAnimals, int genomeSize, int startEnergy, int childrenEnergy) {
        for(int i = 0; i < numberOfAnimals; i++){
            Vector2d position = this.getRandomPosition();
            Animal newAnimal = new Animal(position, startEnergy, genomeSize, childrenEnergy);
            addAnimal(newAnimal);
        }
    }

    public Vector2d getRandomPosition() {
        Random random = new Random();
        int x = random.nextInt(0, width);
        int y = random.nextInt(0, height);
        return new Vector2d(x, y);
    }

    public void createMap(int numberOfAnimals,int numberOfPlants,
                          int startingEnergy, int genomeSize, int childrenEnergy) {
        createAnimals(numberOfAnimals,genomeSize,startingEnergy, childrenEnergy);
        growPlants(numberOfPlants);
    }

    public void moveAnimal(Animal animal){
        Square oldSquare = squareAt(animal.getPosition());
        animal.move(this);
        oldSquare.removeAnimal(animal);

        Vector2d newPosition = animal.getPosition();
        Square newSquare = squareAt(newPosition);
        newSquare.addAnimal(animal);
    }

    public void moveAllAnimals(){
        for(Animal animal : this.animals) {
            moveAnimal(animal);
            animal.setAge(animal.getAge() + 1);
        }
    }

    public void removeDeadAnimals(){
        List<Animal> animalsToRemove = new ArrayList<>();
        for(Animal animal : this.animals) {
            if (animal.isDead()) {
                animalsToRemove.add(animal);
                this.deadAnimals.add(animal);
            }
        }
        for(Animal animal : animalsToRemove) {
            removeAnimal(animal);
        }
    }

    public List<Animal> getDeadAnimals() {
        return deadAnimals;
    }

    public void copulationAllAnimals(int energyAllowingCopulation, int minMutation, int maxMutation) {
        for(Square square: this.mapSquares.values()){
            if(square.getAnimals().size()>1){
                PriorityQueue<Animal> currAnimals = new PriorityQueue<>(square.getAnimalsAsQueue());
                currAnimals.removeIf(x -> x.getEnergy() < energyAllowingCopulation);
                while(currAnimals.size() > 1){
                    Animal firstParent = currAnimals.poll();
                    Animal otherParent = currAnimals.poll();
                    Animal child = null;
                    child = firstParent.createChild(otherParent, minMutation, maxMutation);
                    addAnimal(child);
                }
            }
        }
    }

    public void addObserver(MapChangeListener observer) {
        observers.add(observer);
    }

    public void removeObserver(MapChangeListener observer) {
        observers.remove(observer);
    }

    protected void notifyObservers() {
        for (MapChangeListener observer : observers) {
            observer.mapChanged(this, new ArrayList<>(),new ArrayList<>());
        }
    }
    public void mapChanged() {
        synchronized(this){
            notifyObservers();
        }
    }
}
