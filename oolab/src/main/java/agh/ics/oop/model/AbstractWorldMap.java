package agh.ics.oop.model;

import agh.ics.oop.model.elements.*;
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

                if (currAnimals.size() == 0) {
                    continue;
                } else {
                    Animal strongestAnimal = (Animal) currAnimals.poll();
                    int animalEnergy = strongestAnimal.getEnergy();
                    int newAnimalEnergy = animalEnergy + plantEnergy;
                    strongestAnimal.setEnergy(newAnimalEnergy);
                    square.setPlant(null);
                    this.plants.remove(currPlant.getPosition());
                }
            }
        }
    }


}
