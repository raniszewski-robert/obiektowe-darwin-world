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
    protected final UUID id = UUID.randomUUID();
    private final List<MapChangeListener> observers = new ArrayList<>();
    protected List<Animal> animals;
    protected List<Plant> plants;
    private List<Animal> deadAnimals = new ArrayList<>();
    protected final HashMap<Vector2d, Square> mapSquares;
    public int width;
    public int height;

    protected AbstractWorldMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.animals = new ArrayList<>();
        this.plants = new ArrayList<>();
        this.mapSquares = new HashMap<>();
        this.jungleLowerY = (int) (0.4 * height);
        this.jungleUpperY = (int) (0.6 * height) - 1;
        this.lowerLeft = new Vector2d(0, 0);
        this.upperRight = new Vector2d(this.width-1, this.height-1);
    }

    public Collection<Square> getAllSquares(){
        return mapSquares.values();
    }

    public HashMap<Vector2d, Square> getMapSquares() {
        return mapSquares;
    }

    @Override
    public Boundary getCurrentBounds() {
        return new Boundary(new Vector2d(0,0), new Vector2d(width-1, height-1));
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    @Override
    public boolean place(Square square, Vector2d position) {
        if (!this.isInMap(position)) {
            throw new IllegalArgumentException(position + " is out of map");
        }
        List<Animal> newAnimals = square.getAnimals();
        Plant newPlant = square.getPlant();

        if (!this.isOccupied(position)){
            this.mapSquares.put(position, square);

        }else{
            if (newAnimals != null) {
                for (Animal animal : newAnimals) {
                    this.mapSquares.get(position).addAnimal(animal);
                }
            }
            if (newPlant != null) {
                this.mapSquares.get(position).addPlant(newPlant);
            }
        }

        if (newAnimals != null) {
            this.animals.addAll(newAnimals);
        }

        if (newPlant != null) {
            this.plants.add(newPlant);
        }

        return true;
    }

    @Override
    public Square objectAt(Vector2d position) {
        return mapSquares.get(position);
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    @Override
    public boolean isInMap(Vector2d position) {
        return (position.follows(lowerLeft) && position.precedes(upperRight));
    }

    private boolean isInJungle(Vector2d position) {
        return position.getY() >= jungleLowerY && position.getY() <= jungleUpperY;
    }

    public void growPlants(int numberOfPlants) {
        for(int i = 0; i < numberOfPlants; i++) {
            Random random = new Random();
            Vector2d position;
            double chance;
            Square currentSquare;

            do {
                int x = random.nextInt(width);
                int y = random.nextInt(height);
                position = this.getRandomPosition();
                currentSquare = this.mapSquares.get(position);
                chance = isInJungle(position) ? 0.8 : 0.2; // 80% dla dżungli, 20% poza
            } while ((currentSquare != null && currentSquare.hasPlant()) || random.nextDouble() > chance);

            Plant newPlant = new Plant(position);
            Square newSquare = new Square();
            newSquare.addPlant(newPlant);
            this.place(newSquare, newPlant.getPosition());
        }
    }

    public void eatPlants(int plantEnergy) {
        for (Square square : getAllSquares()) {

            if (square.hasPlant()){
                Plant currPlant = square.getPlant();
                PriorityQueue<Animal> currAnimals = new PriorityQueue<>(square.getAnimalsAsQueue());

                if (!currAnimals.isEmpty()) {
                    Animal strongestAnimal = (Animal) currAnimals.poll();
                    int animalEnergy = strongestAnimal.getEnergy();
                    int newAnimalEnergy = animalEnergy + plantEnergy;
                    strongestAnimal.setEnergy(newAnimalEnergy);
                    System.out.println(strongestAnimal.getEnergy());
                    strongestAnimal.addPlantCount();
                    square.setPlant(null);
                    this.plants.remove(currPlant);
                }
            }
        }
    }

    public void createAnimals(int numberOfAnimals, int genomeSize, int startEnergy) {
        for(int i = 0; i < numberOfAnimals; i++){
            Vector2d position = this.getRandomPosition();

            Animal newAnimal = new Animal(position, startEnergy, genomeSize);
            Square newSquare = new Square();
            newSquare.addAnimal(newAnimal);
            this.place(newSquare, position);
        }
    }

    public Vector2d getRandomPosition() {
        Random random = new Random();
        int x = random.nextInt(width) + lowerLeft.getX();
        int y = random.nextInt(height) + lowerLeft.getY();
        return new Vector2d(x, y);
    }

    public void createMap(int numberOfAnimals,int numberOfPlants,int startingEnergy, int genomeSize){
        createAnimals(numberOfAnimals,genomeSize,startingEnergy);
        growPlants(numberOfPlants);
    }

    public void moveAnimal(Animal animal){
        if (animal.isDead()){
            Square oldSquare = this.mapSquares.get(animal.getPosition());
            if(oldSquare != null){
                oldSquare.removeAnimal(animal);
                if(!oldSquare.hasPlant() && oldSquare.getAnimals().isEmpty()){
                    this.mapSquares.remove(animal.getPosition());
                }
            }
            return;
        }

        Square oldSquare = this.mapSquares.get(animal.getPosition());

        animal.move(this);
        Vector2d newPosition = animal.getPosition();

        if(oldSquare != null){
            oldSquare.removeAnimal(animal);
            if(!oldSquare.hasPlant() && oldSquare.getAnimals().isEmpty()){
                this.mapSquares.remove(animal.getPosition());
            }
        }

        Square newSquare = this.mapSquares.get(newPosition);

        if(newSquare == null){
            newSquare = new Square();
            newSquare.addAnimal(animal);
            this.mapSquares.put(newPosition,newSquare);
            return;
        }
        else{
            newSquare.addAnimal(animal);
        }
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
                Vector2d position = animal.getPosition();
                Square square = this.mapSquares.get(position);
                if(square != null){
                    square.removeAnimal(animal);
                    if(!square.hasPlant() && square.getAnimals().isEmpty()){
                        this.mapSquares.remove(animal.getPosition());
                    }
                }
                this.deadAnimals.add(animal);
            }
        }
        for(Animal animal : animalsToRemove) {
            this.animals.remove(animal);
        }
    }

    public List<Animal> getDeadAnimals() {
        return deadAnimals;
    }

    public void copulationAllAnimals(int energyAllowingCopulation){
        for(Square square: this.mapSquares.values()){
            if(square.getAnimals().size()>1){
                PriorityQueue<Animal> currAnimals = new PriorityQueue<>(square.getAnimalsAsQueue());
                currAnimals.removeIf(x -> x.getEnergy() < energyAllowingCopulation);
                while(currAnimals.size() > 1){
                    Animal firstParent = currAnimals.poll();
                    Animal otherParent = currAnimals.poll();
                    Animal child = firstParent.createChild(otherParent);

                    this.animals.add(child);
                    square.addAnimal(child);
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
