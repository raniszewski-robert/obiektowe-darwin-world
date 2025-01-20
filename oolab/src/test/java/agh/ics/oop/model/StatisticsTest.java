package agh.ics.oop.model;

import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Genotype;
import agh.ics.oop.model.elements.Plant;
import agh.ics.oop.model.elements.Square;
import agh.ics.oop.model.enums.GenomeVariant;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StatisticsTest {

    @Test
    public void testCountAnimals() {
        AbstractWorldMap map = new GlobeMap(10, 10, GenomeVariant.NORMAL);
        Statistics stats = new Statistics(map);

        // Dodaj zwierzęta
        Square square1 = map.squareAt(new Vector2d(1, 1));
        square1.addAnimal(new Animal(null, 10, null, 5));
        square1.addAnimal(new Animal(null, 20, null, 5));

        Square square2 = map.squareAt(new Vector2d(2, 2));
        square2.addAnimal(new Animal(null, 15, null, 5));

        assertEquals(3, stats.countAnimals());
    }

    @Test
    public void testCountPlants() {
        AbstractWorldMap map = new GlobeMap(10, 10, GenomeVariant.NORMAL);
        Statistics stats = new Statistics(map);

        // Dodaj rośliny
        Square square1 = map.squareAt(new Vector2d(1, 1));
        square1.addPlant(new Plant(new Vector2d(1, 1)));

        Square square2 = map.squareAt(new Vector2d(2, 2));
        square2.addPlant(new Plant(new Vector2d(2, 2)));

        assertEquals(2, stats.countPlants());
    }

    @Test
    public void testCountFreeSquares() {
        AbstractWorldMap map = new GlobeMap(10, 10, GenomeVariant.NORMAL);
        Statistics stats = new Statistics(map);

        // Zapełnij niektóre pola
        Square square1 = map.squareAt(new Vector2d(1, 1));
        square1.addAnimal(new Animal(null, 10, null, 5));

        Square square2 = map.squareAt(new Vector2d(2, 2));
        square2.addPlant(new Plant(new Vector2d(2, 2)));

        assertEquals(98, stats.countFreeSquares()); // Na mapie 10x10 są 100 pól
    }

    @Test
    public void testGetMostCommonGenotype() {
        AbstractWorldMap map = new GlobeMap(10, 10, GenomeVariant.NORMAL);
        Statistics stats = new Statistics(map);

        // Dodaj zwierzęta z różnymi genotypami
        Genotype genotype1 = new Genotype(5);
        Genotype genotype2 = new Genotype(5);

        Animal animal1 = new Animal(null, 10, genotype1, 5);
        Animal animal2 = new Animal(null, 20, genotype1, 5);
        Animal animal3 = new Animal(null, 15, genotype2, 5);

        map.getAnimals().add(animal1);
        map.getAnimals().add(animal2);
        map.getAnimals().add(animal3);

        assertEquals(genotype1, stats.getMostCommonGenotype());
    }

    @Test
    public void testGetAverageEnergy() {
        AbstractWorldMap map = new GlobeMap(10, 10, GenomeVariant.NORMAL);
        Statistics stats = new Statistics(map);

        // Dodaj zwierzęta
        Animal animal1 = new Animal(null, 10, null, 5);
        Animal animal2 = new Animal(null, 20, null, 5);
        Animal animal3 = new Animal(null, 30, null, 5);

        map.getAnimals().add(animal1);
        map.getAnimals().add(animal2);
        map.getAnimals().add(animal3);

        assertEquals(20.0, stats.getAverageEnergy(), 0.01);
    }

    @Test
    public void testGetAverageLifeLength() {
        AbstractWorldMap map = new GlobeMap(10, 10, GenomeVariant.NORMAL);
        Statistics stats = new Statistics(map);

        // Dodaj martwe zwierzęta
        Animal deadAnimal1 = new Animal(null, 0, null, 5);
        deadAnimal1.setAge(5);

        Animal deadAnimal2 = new Animal(null, 0, null, 5);
        deadAnimal2.setAge(15);

        map.getDeadAnimals().add(deadAnimal1);
        map.getDeadAnimals().add(deadAnimal2);

        assertEquals(10.0, stats.getAverageLifeLength(), 0.01);
    }

}

