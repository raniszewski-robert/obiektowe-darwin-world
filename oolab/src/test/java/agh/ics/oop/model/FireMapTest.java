package agh.ics.oop.model;

import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Fire;
import agh.ics.oop.model.elements.Plant;
import agh.ics.oop.model.elements.Square;
import agh.ics.oop.model.enums.GenomeVariant;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FireMapTest {

    @Test
    public void testFireMap() {
        FireMap map = new FireMap(10,10, GenomeVariant.NORMAL, 2,10);
        Plant plant1 = new Plant(new Vector2d(5,5));
        map.addPlant(plant1);
        map.startFire();
        Square fireSquare = map.mapSquares.get(new Vector2d(5, 5));
        Fire fire1 = fireSquare.getFire();

        assertTrue(fireSquare.onFire());
        assertEquals(2,fire1.getRemainingTurns());

        map.spreadFire();

        assertEquals(1,fire1.getRemainingTurns());

        map.spreadFire();

        assertEquals(0,fire1.getRemainingTurns());

        map.spreadFire();

        assertEquals(0,fire1.getRemainingTurns());

    }

    @Test
    public void testFireSpread() {
        FireMap map = new FireMap(10,10, GenomeVariant.NORMAL, 10,10);
        Plant plant1 = new Plant(new Vector2d(5,5));
        map.addPlant(plant1);
        map.startFire();

        Plant plant2 = new Plant(new Vector2d(5,4));
        Square fireSquare2 = map.mapSquares.get(new Vector2d(5,4));
        map.addPlant(plant2);

        Plant plant3 = new Plant(new Vector2d(4,3));
        Square fireSquare3 = map.mapSquares.get(new Vector2d(4,3));
        map.addPlant(plant3);

        Plant plant4 = new Plant(new Vector2d(5,6));
        Square fireSquare4 = map.mapSquares.get(new Vector2d(5,6));
        map.addPlant(plant4);

        Plant plant5 = new Plant(new Vector2d(6,5));
        Square fireSquare5 = map.mapSquares.get(new Vector2d(6,5));
        map.addPlant(plant5);

        map.spreadFire();
        assertTrue(fireSquare2.onFire());
        assertFalse(fireSquare3.onFire());
        assertTrue(fireSquare4.onFire());
        assertTrue(fireSquare5.onFire());
    }

    @Test
    public void testFireKillsAnimals() {
        FireMap map = new FireMap(10,10, GenomeVariant.NORMAL, 10,10);
        Plant plant1 = new Plant(new Vector2d(5,5));
        Animal animal1 = new Animal(new Vector2d(5,5), 50, 5, 5);
        Square square1 = map.mapSquares.get(new Vector2d(5,5));
        map.addPlant(plant1);
        map.addAnimal(animal1);

        assertTrue(square1.getAnimals().contains(animal1));

        map.startFire();

        assertTrue(square1.getAnimals().contains(animal1));

        map.spreadFire();

        assertFalse(square1.getAnimals().contains(animal1));
    }

}