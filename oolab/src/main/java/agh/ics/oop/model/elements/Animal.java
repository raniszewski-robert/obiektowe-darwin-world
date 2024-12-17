package agh.ics.oop.model.elements;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.enums.GenomeVariant;
import agh.ics.oop.model.interfaces.WorldMap;

public class Animal {
    private int direction;
    private Vector2d position;
    private int energy;
    private Genotype genotype;
    private static GenomeVariant genomeVariant;

    public Animal(int direction, Vector2d position, int energy, Genotype genotype) {
        this.direction = direction;
        this.position = position;
        this.energy = energy;
        this.genotype = genotype;
    }

    public boolean isDead(){
        return energy <= 0;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public void move(WorldMap map){
        int currentDirection = this.direction;
        int currentGenotypeIndex = genotype.getCurrentGenomeIndex();
        int currentDirectionChange = genotype.getGenome().get(currentGenotypeIndex);

        currentDirection += currentDirectionChange;
        currentDirection %= 8;
        this.direction = currentDirection;
        moveInDirection(map);
        genotype.indexChange(genomeVariant);
        this.energy -= 1;
    }

    private void moveInDirection(WorldMap map) {
        int y = map.getCurrentBounds().upperRight().getY();
        int x = map.getCurrentBounds().upperRight().getX();
        Vector2d moveVector;
        switch (this.direction) {
            case 0 -> {
                if ((this.position.getY() + 1) >= y) {
                    this.direction = 4;
                } else {
                    moveVector = this.position.add(new Vector2d(0, 1));
                    this.position = moveVector;
                }
            }
            case 1 -> {
                if ((this.position.getY() + 1) >= y) {
                    this.direction = 4;
                } else {
                    moveVector = this.position.add(new Vector2d(1, 1));
                    moveVector = new Vector2d(moveVector.getX() % x, moveVector.getY());
                    this.position.add(moveVector);
                }
            }
            case 2 -> {
                moveVector = this.position.add(new Vector2d(1, 0));
                moveVector = new Vector2d(moveVector.getX() % x, moveVector.getY());
                this.position.add(moveVector);
                }
            case 3 -> {
                if ((this.position.getY() - 1) < 0) {
                    this.direction = 0;
                } else {
                    moveVector = this.position.add(new Vector2d(1, -1));
                    moveVector = new Vector2d(moveVector.getX() % x, moveVector.getY());
                    this.position.add(moveVector);
                }
            }
            case 4 -> {
                if ((this.position.getY() - 1) < 0) {
                    this.direction = 0;
                } else {
                    moveVector = this.position.add(new Vector2d(0, -1));
                    this.position = moveVector;
                }
            }
            case 5 -> {
                if ((this.position.getY() - 1) < 0) {
                    this.direction = 0;
                } else {
                    moveVector = this.position.add(new Vector2d(-1, -1));
                    moveVector = new Vector2d(moveVector.getX() % x, moveVector.getY());
                    this.position.add(moveVector);
                }
            }
            case 6 -> {
                moveVector = this.position.add(new Vector2d(-1, 0));
                moveVector = new Vector2d(moveVector.getX() % x, moveVector.getY());
                this.position.add(moveVector);
            }
            case 7 -> {
                if ((this.position.getY() + 1) >= y) {
                    this.direction = 4;
                } else {
                    moveVector = this.position.add(new Vector2d(-1, 1));
                    moveVector = new Vector2d(moveVector.getX() % x, moveVector.getY());
                    this.position.add(moveVector);
                }
            }
        }
    }
}
