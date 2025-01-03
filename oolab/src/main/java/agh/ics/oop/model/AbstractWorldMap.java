package agh.ics.oop.model;

import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Plant;
import agh.ics.oop.model.elements.Square;
import agh.ics.oop.model.interfaces.WorldMap;

import java.util.*;

public abstract class AbstractWorldMap implements WorldMap {
    private final Vector2d lowerLeft;
    private final Vector2d upperRight;
    protected final int jungleLowerY;
    protected final int jungleUpperY;
    protected final UUID id = UUID.randomUUID();
    protected final Map<Vector2d, Animal> animals = new HashMap<>();
    protected final Map<Vector2d, Plant> plants = new HashMap<>();
    private HashMap<Vector2d, Square> mapSquares;
    public int width;
    public int height;

    protected AbstractWorldMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.jungleLowerY = (int) (0.4 * height);
        this.jungleUpperY = (int) (0.6 * height) - 1;
        this.lowerLeft = new Vector2d(0, 0);
        this.upperRight = new Vector2d(this.width-1, this.height-1);
    }

    public Collection<Square> getAllSquares(){
        return mapSquares.values();
    }

    @Override
    public boolean place(MapFieldElement element, Vector2d position) {
        return false;
    }

    @Override
    public MapFieldElement objectAt(Vector2d position) {
        return null;
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    private boolean isInJungle(Vector2d position) {
        return position.getY() >= jungleLowerY && position.getY() <= jungleUpperY;
    }

    public void growPlants() {
        Random random = new Random();
        Vector2d position;
        double chance;

        do {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            position = new Vector2d(x, y);
            chance = isInJungle(position) ? 0.8 : 0.2; // 80% dla dżungli, 20% poza
        } while (plants.containsKey(position) || random.nextDouble() > chance);

        plants.put(position, new Plant(position));
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
