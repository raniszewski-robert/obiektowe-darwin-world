package agh.ics.oop.model;

import agh.ics.oop.model.elements.*;
import agh.ics.oop.model.enums.GenomeVariant;
import agh.ics.oop.model.interfaces.WorldMap;
import agh.ics.oop.model.records.Boundary;

import java.util.*;

public abstract class AbstractWorldMap implements WorldMap {
    private final Vector2d lowerLeft;
    private final Vector2d upperRight;
    protected final int jungleLowerY;
    protected final int jungleUpperY;
    protected final UUID id = UUID.randomUUID();
    protected final List<Animal> animals;
    protected final List<Plant> plants;
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

    @Override
    public Boundary getCurrentBounds() {
        return new Boundary(new Vector2d(0,0), new Vector2d(width-1, height-1));
    }

    @Override
    public boolean place(MapFieldElement element, Vector2d position) {
        if (!this.isInMap(position)) {
            throw new IllegalArgumentException(position + " is out of map");
        }

        Square square = new Square(position, element);
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
    public MapFieldElement objectAt(Vector2d position) {
        return null;
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    @Override
    public boolean isInMap(Vector2d position) {
        return (position.follows(lowerLeft) && position.follows(upperRight));
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
                position = new Vector2d(x, y);
                currentSquare = this.mapSquares.get(position);
                chance = isInJungle(position) ? 0.8 : 0.2; // 80% dla dżungli, 20% poza
            } while ((currentSquare != null && currentSquare.hasPlant()) || random.nextDouble() > chance);

            Plant newPlant = new Plant(position);
            MapFieldElement newElement = new MapFieldElement();
            newElement.addPlant(newPlant);
            this.place(newElement, newPlant.getPosition());
        }
    }

    public void eatPlants(int plantEnergy) {
        for (Square square : getAllSquares()) {
            if (square.getElement().hasPlant()){
                Plant currPlant = square.getPlant();
                PriorityQueue<Animal> currAnimals = new PriorityQueue<>(square.getElement().getAnimalsAsQueue());

                if (!currAnimals.isEmpty()) {
                    Animal strongestAnimal = (Animal) currAnimals.poll();
                    int animalEnergy = strongestAnimal.getEnergy();
                    int newAnimalEnergy = animalEnergy + plantEnergy;
                    strongestAnimal.setEnergy(newAnimalEnergy);
                    square.setPlant(null);
                    this.plants.remove(currPlant);
                }
            }
        }
    }

    public void createAnimals(int numberOfAnimals, int genomeSize, int startEnergy) {
        for(int i = 0; i < numberOfAnimals; i++){
            Vector2d position = this.getRandomPosition();
            Genotype genotype = new Genotype(genomeSize);
            Animal newAnimal = new Animal(0, position, startEnergy, genotype);
            MapFieldElement newElement = new MapFieldElement();
            newElement.addAnimal(newAnimal);
            this.place(newElement, position);
        }
    }

    public Vector2d getRandomPosition() {
        Random random = new Random();
        int x = random.nextInt(width+1) + lowerLeft.getX();
        int y = random.nextInt(height+1) + lowerLeft.getY();
        return new Vector2d(x, y);
    }

    public void createMap(int numberOfAnimals,int numberOfPlants,int startingEnergy, int energyOfPlant, int genomeSize){
        createAnimals(numberOfAnimals,genomeSize,startingEnergy);
        growPlants(numberOfPlants);
    }

    public void moveAnimal(Animal animal){
        if (animal.isDead()){
            Square oldSquare = this.mapSquares.get(animal.getPosition());
            if(oldSquare != null){
                oldSquare.removeAnimal(animal);
            }
            return;
        }

        Square oldSquare = this.mapSquares.get(animal.getPosition());

        animal.move(this);
        Vector2d newPosition = animal.getPosition();

        if(oldSquare != null){
            oldSquare.removeAnimal(animal);
        }

        Square newSquare = this.mapSquares.get(newPosition);

        if(newSquare == null){
            MapFieldElement element = new MapFieldElement();
            element.addAnimal(animal);
            newSquare = new Square(newPosition,element);
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
        for(Animal animal : this.animals) {
            if (animal.isDead()) {
                this.animals.remove(animal);
                Vector2d position = animal.getPosition();
                Square square = this.mapSquares.get(position);
                if(square != null){
                    square.removeAnimal(animal);
                }
            }
        }
    }

    public void copulationAllAnimals(int energyAllowingCopulation){
        for(Square square: this.mapSquares.values()){
            if(square.getAnimals().size()>1){
                PriorityQueue<Animal> currAnimals = new PriorityQueue<>(square.getElement().getAnimalsAsQueue());
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
}
