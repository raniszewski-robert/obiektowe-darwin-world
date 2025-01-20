package agh.ics.oop.model.elements;

import agh.ics.oop.model.AbstractWorldMap;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.interfaces.WorldMap;

import java.util.*;

public class Animal {
    private int direction;
    private Vector2d position;
    private int energy;
    private final Genotype genotype;
    private List<Animal> children;
    private int age;
    private int childrenCount;
    private int plantCount;
    private int childrenEnergy;

    // Metoda inicjalizująca wspólne pola konstruktorow
    private void initializeCommonFields(Vector2d position, int energy, int childrenEnergy) {
        this.position = position;
        this.energy = energy;
        this.childrenEnergy = childrenEnergy;
        this.age = 0;
        this.childrenCount = 0;
        this.plantCount = 0;
        this.children = new ArrayList<>();
        Random random = new Random();
        this.direction = random.nextInt(0, 8);
    }

    public Animal(Vector2d position, int energy, Genotype genotype, int childrenEnergy) {
        initializeCommonFields(position, energy, childrenEnergy);
        this.genotype = genotype;
    }
    public Animal(Vector2d position, int energy, int genotypeSize, int childrenEnergy) {
        this(position, energy, new Genotype(genotypeSize), childrenEnergy);
        Random random = new Random();
        this.genotype.setCurrentGenomeIndex(random.nextInt(0, genotypeSize));
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

    public void move(AbstractWorldMap map){


        moveInDirection(map);

        genotype.indexChange(map.getGenomeVariant());
        int currentGenotypeIndex = genotype.getCurrentGenomeIndex();
        this.direction = genotype.getGenome().get(currentGenotypeIndex);
        this.energy -= 1;
    }

    public Vector2d getPosition() {
        return position;
    }

    public int getDirection() { return direction; }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getChildrenCount() {
        return childrenCount;
    }

    public Genotype getGenotype() {
        return genotype;
    }

    public int getPlantCount() {
        return plantCount;
    }

    public void addPlantCount() {
        this.plantCount += 1;
    }

    private void moveInDirection(WorldMap map) {
        int y = map.getCurrentBounds().upperRight().getY();
        int x = map.getCurrentBounds().upperRight().getX() + 1;
        Vector2d moveVector;
        switch (this.direction) {
            case 0 -> {
                if ((this.position.getY() + 1) > y) {
                    this.direction = 4;
                } else {
                    moveVector = this.position.add(new Vector2d(0, 1));
                    this.position = moveVector;
                }
            }
            case 1 -> {
                if ((this.position.getY() + 1) > y) {
                    this.direction = 4;
                } else {
                    moveVector = this.position.add(new Vector2d(1, 1));
                    moveVector = new Vector2d(moveVector.getX() % x, moveVector.getY());
                    this.position = moveVector;
                }
            }
            case 2 -> {
                moveVector = this.position.add(new Vector2d(1, 0));
                moveVector = new Vector2d(moveVector.getX() % x, moveVector.getY());
                this.position = moveVector;
                }
            case 3 -> {
                if ((this.position.getY() - 1) < 0) {
                    this.direction = 0;
                } else {
                    moveVector = this.position.add(new Vector2d(1, -1));
                    moveVector = new Vector2d(moveVector.getX() % x, moveVector.getY());
                    this.position = moveVector;
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
                    moveVector = new Vector2d((moveVector.getX()+x) % x, moveVector.getY());
                    this.position = moveVector;
                }
            }
            case 6 -> {
                moveVector = this.position.add(new Vector2d(-1, 0));
                moveVector = new Vector2d((moveVector.getX()+x) % x, moveVector.getY());
                this.position = moveVector;
            }
            case 7 -> {
                if ((this.position.getY() + 1) > y) {
                    this.direction = 4;
                } else {
                    moveVector = this.position.add(new Vector2d(-1, 1));
                    moveVector = new Vector2d((moveVector.getX()+x) % x, moveVector.getY());
                    this.position = moveVector;
                }
            }
        }
    }

    public Animal createChild(Animal otherParent, int minMutateNumber, int maxMutateNumber) {
        int sumOfEnergy = this.energy + otherParent.energy;
        float energyPercent = (float) this.energy / sumOfEnergy;
        this.energy -= childrenEnergy;
        otherParent.energy -= childrenEnergy;

        Genotype childGenotype = this.genotype.createChildGenotype(otherParent.genotype, energyPercent);


        Random random = new Random();
        int genomeSize = childGenotype.getGenomeSize();
        int mutateNumber = random.nextInt(maxMutateNumber - minMutateNumber + 1) + minMutateNumber;
        for (int i = 0; i < mutateNumber; i++){
            int index = random.nextInt(0, genomeSize);
            int value = random.nextInt(8);
            childGenotype.getGenome().set(index, value);
        }

        this.childrenCount += 1;
        otherParent.childrenCount += 1;

        Animal child = new Animal(this.position, childrenEnergy*2, childGenotype, childrenEnergy);
        this.children.add(child);
        otherParent.children.add(child);
        return child;
    }

    public Set<Animal> getDescendants() {
        Set<Animal> descendantSet = new HashSet<>();

        for (Animal child : children) {
            descendantSet.add(child);
            descendantSet.addAll(child.getDescendants());
        }
        return descendantSet;
    }
    public int getDescendantNumber(){
        Set<Animal> descendants = getDescendants();
        if(!descendants.isEmpty()){
            return descendants.size();
        }
        return 0;

    }
}
