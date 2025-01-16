package agh.ics.oop.model;

import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Square;
import agh.ics.oop.model.interfaces.WorldMap;
import agh.ics.oop.model.records.Boundary;

class AnimalTest {

    public static void main(String[] args) {
        testIsDead();
        testMove();
        testCreateChild();
        System.out.println("All tests passed!");
    }

    static void testIsDead() {
        Animal animal = new Animal(new Vector2d(5, 5), 0, 10);
        assert animal.isDead() : "Animal should be dead with energy 0";

        animal.setEnergy(-1);
        assert animal.isDead() : "Animal should be dead with negative energy";

        animal.setEnergy(10);
        assert !animal.isDead() : "Animal should be alive with positive energy";
    }

    static void testMove() {
        Vector2d initialPosition = new Vector2d(5, 5);
        Animal animal = new Animal(initialPosition, 50, 10);
        WorldMap mockMap = createMockMap();

        Vector2d positionBeforeMove = animal.getPosition();
        int energyBeforeMove = animal.getEnergy();

        animal.move(mockMap);

        assert !positionBeforeMove.equals(animal.getPosition()) : "Animal should change position after move";
        assert energyBeforeMove - 1 == animal.getEnergy() : "Animal energy should decrease by 1 after move";
    }

    static void testCreateChild() {
        Vector2d parentPosition = new Vector2d(5, 5);
        Animal parent1 = new Animal(parentPosition, 50, 10);
        Animal parent2 = new Animal(parentPosition, 50, 10);
        Animal.setChildrenEnergy(10);

        Animal child = parent1.createChild(parent2);

        assert child.getPosition().equals(parentPosition) : "Child should inherit parent's position";
        assert child.getEnergy() == 20 : "Child energy should be double the childrenEnergy value";
        assert !child.getGenotype().equals(parent1.getGenotype()) : "Child genotype should be a combination of parents' genotypes";
    }

    static WorldMap createMockMap() {
        return new WorldMap() {
            @Override
            public Boundary getCurrentBounds() {
                return new Boundary(new Vector2d(0, 0), new Vector2d(10, 10));
            }

            @Override
            public boolean isInMap(Vector2d position) {
                return false;
            }



            @Override
            public boolean isOccupied(Vector2d position) {
                return false;
            }

            @Override
            public boolean place(Square square, Vector2d position) {
                return false;
            }

            @Override
            public Square objectAt(Vector2d position) {
                return null;
            }
        };
    }
}
