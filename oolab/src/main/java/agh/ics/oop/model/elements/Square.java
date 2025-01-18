package agh.ics.oop.model.elements;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Square {
    private Plant plant;
    private PriorityQueue<Animal> animals;
    private Fire fire;
    public Square() {
        this.plant = null;
        this.animals = new PriorityQueue<>(Comparator.comparingInt(Animal::getEnergy).reversed()
                .thenComparingInt(Animal::getAge).reversed()
                .thenComparingInt(Animal::getChildrenCount).reversed());
        this.fire = null;
    }

    public void addAnimal(Animal animal) {
        animals.add(animal);
    }
    public void removeAnimal(Animal animal) {
        animals.remove(animal);
    }
    public void addPlant(Plant plant){
        this.plant = plant;
    }
    public void removePlant(){
        this.plant = null;
    }

    public boolean hasPlant() {return this.plant != null;}
    public Plant getPlant() {return this.plant;}

    public List<Animal> getAnimals() {return List.copyOf(animals);}

    public PriorityQueue<Animal> getAnimalsAsQueue() {return this.animals;}
    public boolean isEmpty() {return this.animals.isEmpty() && this.plant == null && this.fire == null;}
    public void addFire(Fire fire) {this.fire = fire;}
    public void removeFire() {this.fire = null;}
    public boolean onFire() {return this.fire != null;}
    public Fire getFire() {return this.fire;}
}
