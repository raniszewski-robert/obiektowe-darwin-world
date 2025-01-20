package agh.ics.oop.model.elements;

import agh.ics.oop.model.AbstractWorldMap;
import agh.ics.oop.model.GlobeMap;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.enums.GenomeVariant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AnimalTest {

    private Animal animal;
    private Genotype genotype;

    @BeforeEach
    void setUp() {
        genotype = new Genotype(10); // Przykladowy rozmiar genotypu
        animal = new Animal(new Vector2d(2, 2), 100, genotype, 20);
    }

    @Test
    void testInitialization() {
        assertEquals(new Vector2d(2, 2), animal.getPosition());
        assertEquals(100, animal.getEnergy());
        assertEquals(0, animal.getAge());
        assertEquals(0, animal.getChildrenCount());
        assertEquals(0, animal.getPlantCount());
        assertNotNull(animal.getGenotype());
    }

    @Test
    void testIsDead() {
        assertFalse(animal.isDead());

        animal.setEnergy(0);
        assertTrue(animal.isDead());

        animal.setEnergy(-10);
        assertTrue(animal.isDead());
    }

    @Test
    void testMove() {
        AbstractWorldMap map = new GlobeMap(10, 10, GenomeVariant.NORMAL);
        Vector2d initialPosition = animal.getPosition();
        int initialDirection = animal.getDirection();
        int initialEnergy = animal.getEnergy();

        animal.move(map);

        assertNotEquals(initialPosition, animal.getPosition());
        assertNotEquals(initialDirection, animal.getDirection());
        assertEquals(initialEnergy - 1, animal.getEnergy());
    }

    @Test
    void testAddPlantCount() {
        assertEquals(0, animal.getPlantCount());

        animal.addPlantCount();
        assertEquals(1, animal.getPlantCount());
    }

    @Test
    void testCreateChild() {
        Animal otherParent = new Animal(new Vector2d(0, 0), 100, genotype, 20);
        int initialEnergy = animal.getEnergy();
        int initialOtherParentEnergy = otherParent.getEnergy();
        int initialChildrenCount = animal.getChildrenCount();

        Animal child = animal.createChild(otherParent, 1, 3);

        assertEquals(initialEnergy - 20, animal.getEnergy());
        assertEquals(initialOtherParentEnergy - 20, otherParent.getEnergy());
        assertEquals(initialChildrenCount + 1, animal.getChildrenCount());
        assertEquals(40, child.getEnergy());
        assertEquals(animal.getPosition(), child.getPosition());
        assertEquals(1, animal.getChildrenCount());
    }

    @Test
    void testGetDescendantNumber() {
        Animal otherParent = new Animal(new Vector2d(0, 0), 100, genotype, 20);
        assertEquals(0, animal.getDescendantNumber());
        Animal child1 = animal.createChild(otherParent, 1, 3);
        assertEquals(1, animal.getDescendantNumber());
        Animal child2 = animal.createChild(otherParent, 1, 3);

        assertEquals(2, animal.getDescendantNumber());

        Animal grandChild = child1.createChild(otherParent, 1, 3);
        assertEquals(3, animal.getDescendantNumber());
        assertEquals(1, child1.getDescendantNumber());
    }

    @Test
    void testGetDescendants() {
        Animal otherParent = new Animal(new Vector2d(0, 0), 100, genotype, 20);
        Animal child1 = animal.createChild(otherParent, 1, 3);
        Animal child2 = animal.createChild(otherParent, 1, 3);
        Animal grandChild = child1.createChild(otherParent, 1, 3);

        List<Animal> descendants = List.copyOf(animal.getDescendants());

        assertTrue(descendants.contains(child1));
        assertTrue(descendants.contains(child2));
        assertTrue(descendants.contains(grandChild));
        assertEquals(3, descendants.size());
    }
}

