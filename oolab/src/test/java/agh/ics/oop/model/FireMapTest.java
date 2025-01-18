package agh.ics.oop.model;

import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Fire;
import agh.ics.oop.model.elements.Plant;
import agh.ics.oop.model.elements.Square;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FireMapTest {

    @Test
    public void testFireMap() {
        FireMap map = new FireMap(10,10,2,10);
        Plant plant1 = new Plant(new Vector2d(5,5));

        Square square = new Square();
        square.addPlant(plant1);
        map.place(square, new Vector2d(5,5));
        map.startFire();
        Fire fire1 = square.getFire();

        assertTrue(square.onFire());
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
        FireMap map = new FireMap(10,10,10,10);
        Plant plant1 = new Plant(new Vector2d(5,5));
        Square square1 = new Square();
        square1.addPlant(plant1);
        map.place(square1, new Vector2d(5,5));
        map.startFire();

        Plant plant2 = new Plant(new Vector2d(5,4));
        Square square2 = new Square();
        square2.addPlant(plant2);
        map.place(square2, new Vector2d(5,4));

        Plant plant3 = new Plant(new Vector2d(4,3));
        Square square3 = new Square();
        square3.addPlant(plant3);
        map.place(square3, new Vector2d(4,5));

        Plant plant4 = new Plant(new Vector2d(5,6));
        Square square4 = new Square();
        square4.addPlant(plant4);
        map.place(square4, new Vector2d(5,6));

        Plant plant5 = new Plant(new Vector2d(6,5));
        Square square5 = new Square();
        square5.addPlant(plant5);
        map.place(square5, new Vector2d(6,5));

        assertTrue(square1.onFire());
        map.spreadFire();
        assertTrue(square2.onFire());
        assertTrue(square3.onFire());
        assertTrue(square4.onFire());
        assertTrue(square5.onFire());
    }

    @Test
    public void testFireKillsAnimals() {
        FireMap map = new FireMap(10,10,10,10);
        Plant plant1 = new Plant(new Vector2d(5,5));
        Animal animal1 = new Animal(new Vector2d(5,5), 50, 5);
        Square square1 = new Square();
        square1.addPlant(plant1);
        square1.addAnimal(animal1);
        map.place(square1, new Vector2d(5,5));

        assertTrue(square1.getAnimals().contains(animal1));

        map.startFire();

        assertTrue(square1.getAnimals().contains(animal1));

        map.spreadFire();

        assertFalse(square1.getAnimals().contains(animal1));
    }

}