package agh.ics.oop.model;


import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Plant;
import agh.ics.oop.model.elements.Square;
import agh.ics.oop.model.interfaces.MapChangeListener;
import agh.ics.oop.model.interfaces.WorldMap;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Test.*;

public class AbstractWorldMapTest {

    // Implementacja klasy do testowania abstrakcyjnej klasy
    private static class TestWorldMap extends AbstractWorldMap {
        public TestWorldMap(int width, int height) {
            super(width, height);
        }
    }

    @Test
    public void testMapInitialization() {
        AbstractWorldMap map = new TestWorldMap(10, 10);

        assertEquals(10, map.width);
        assertEquals(10, map.height);
        assertTrue(map.getMapSquares().isEmpty());
        assertTrue(map.getAnimals().isEmpty());
        assertEquals(0.4 * map.height, map.jungleLowerY, 0.1);
        assertEquals(0.6 * map.height - 1, map.jungleUpperY, 0.1);
    }

    @Test
    public void testPlaceSquareOnMap() {
        AbstractWorldMap map = new TestWorldMap(10, 10);
        Vector2d position = new Vector2d(5, 5);
        Square square = new Square();

        boolean result = map.place(square, position);

        assertTrue(result);
        assertTrue(map.isOccupied(position));
        assertEquals(square, map.objectAt(position));
    }

    @Test
    public void testPlaceAnimalOnMap() {
        AbstractWorldMap map = new TestWorldMap(10, 10);
        Vector2d position = new Vector2d(3, 3);
        Animal animal = new Animal(position, 100, 5);
        Square square = new Square();
        square.addAnimal(animal);

        boolean result = map.place(square, position);

        assertTrue(result);
        assertEquals(1, map.getAnimals().size());
        assertTrue(map.getAnimals().contains(animal));
    }

    @Test
    public void testAddPlantToSquare() {
        AbstractWorldMap map = new TestWorldMap(10, 10);
        Vector2d position = new Vector2d(2, 2);
        Plant plant = new Plant(position);
        Square square = new Square();
        square.addPlant(plant);

        boolean result = map.place(square, position);

        assertTrue(result);
        assertTrue(map.isOccupied(position));
        assertEquals(plant, map.objectAt(position).getPlant());
    }

    @Test
    public void testGrowPlants() {
        AbstractWorldMap map = new TestWorldMap(10, 10);

        map.growPlants(5);

        int plantCount = 0;
        for (Square square : map.getMapSquares().values()) {
            if (square.hasPlant()) {
                plantCount++;
            }
        }

        assertEquals(5, plantCount);
    }

    @Test
    public void testRemoveDeadAnimals() {
        AbstractWorldMap map = new TestWorldMap(10, 10);
        Animal aliveAnimal = new Animal(new Vector2d(2, 2), 50, 5);
        Animal deadAnimal = new Animal(new Vector2d(3, 3), 0, 5);

        Square aliveSquare = new Square();
        aliveSquare.addAnimal(aliveAnimal);

        Square deadSquare = new Square();
        deadSquare.addAnimal(deadAnimal);

        map.place(aliveSquare, new Vector2d(2, 2));
        map.place(deadSquare, new Vector2d(3, 3));

        map.removeDeadAnimals();

        assertEquals(1, map.getAnimals().size());
        assertTrue(map.getAnimals().contains(aliveAnimal));
        assertFalse(map.getAnimals().contains(deadAnimal));
        assertNull(map.objectAt(new Vector2d(3, 3)));
    }

    @Test
    public void testAnimalMovement() {
        AbstractWorldMap map = new TestWorldMap(10, 10);
        Animal animal = new Animal(new Vector2d(2, 2), 50, 5);
        Square square = new Square();
        square.addAnimal(animal);

        map.place(square, new Vector2d(2, 2));

        animal.move(map);
        Vector2d newPosition = animal.getPosition();

        map.moveAnimal(animal);

        //assertNull(map.objectAt(new Vector2d(2, 2)));
        //assertNotNull(map.objectAt(newPosition));
        assertTrue(map.getAnimals().contains(animal));
    }

    @Test
    public void testMapBounds() {
        AbstractWorldMap map = new TestWorldMap(10, 10);

        Vector2d insidePosition = new Vector2d(5, 5);
        Vector2d outsidePosition = new Vector2d(15, 15);

        assertTrue(map.isInMap(insidePosition));
        assertFalse(map.isInMap(outsidePosition));
    }

    @Test
    public void testGetRandomPosition() {
        AbstractWorldMap map = new TestWorldMap(10, 10);

        Vector2d randomPosition = map.getRandomPosition();

        assertTrue(map.isInMap(randomPosition));
    }

    @Test
    public void testCopulationAllAnimals() {
        AbstractWorldMap map = new TestWorldMap(10, 10);

        Animal parent1 = new Animal(new Vector2d(5, 5), 100, 10);
        Animal parent2 = new Animal(new Vector2d(5, 5), 100, 10);
        Square square = new Square();
        square.addAnimal(parent1);
        square.addAnimal(parent2);

        map.place(square, new Vector2d(5, 5));
        map.copulationAllAnimals(50);

        assertTrue(map.getAnimals().size() > 2);
    }

    @Test
    public void testNotifyObservers() {
        AbstractWorldMap map = new TestWorldMap(10, 10);
        TestObserver observer = new TestObserver();

        map.addObserver(observer);
        map.mapChanged();

        assertTrue(observer.wasNotified());
    }

    // Klasa pomocnicza do testowania obserwatora
    private static class TestObserver implements MapChangeListener {
        private boolean notified = false;

        @Override
        public void mapChanged(WorldMap map, List<String> animals, List<Vector2d> plants) {
            notified = true;
        }
        public boolean wasNotified() {
            return notified;
        }

    }
}

