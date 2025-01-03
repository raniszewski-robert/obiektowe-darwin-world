package agh.ics.oop.model;

import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Plant;

import java.util.Comparator;
import java.util.PriorityQueue;

public class MapFieldElement {

    private Plant plant;
    private PriorityQueue<Animal> animals;

    public MapFieldElement() {
        this.plant = null;
        this.animals = new PriorityQueue<>(Comparator.comparingInt(Animal::getEnergy).reversed());
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
    public PriorityQueue<Animal> getAnimalsAsQueue() {return this.animals;}
    public void setPlant(Plant plant) {this.plant = plant;}
}
