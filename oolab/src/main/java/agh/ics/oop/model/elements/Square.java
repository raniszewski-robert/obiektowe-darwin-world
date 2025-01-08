package agh.ics.oop.model.elements;

import agh.ics.oop.model.MapFieldElement;
import agh.ics.oop.model.Vector2d;

import java.util.List;

public class Square {
    private final Vector2d position;
    private MapFieldElement element;

    public Square(Vector2d position) {
        this.position = position;
        this.element = new MapFieldElement();
    }

    public Square(Vector2d position, MapFieldElement element) {
        this.position = position;
        this.element = element;
    }

    public void addAnimal(Animal animal) {
        this.element.addAnimal(animal);
    }

    public void removeAnimal(Animal animal) {
        this.element.removeAnimal(animal);
    }

    public void addPlant(Plant plant) {
        this.element.addPlant(plant);
    }

    public void removePlant(Plant plant) {
        this.element.removePlant();
    }

    public MapFieldElement getElement() {
        return element;
    }

    public Plant getPlant() {
        return this.element.getPlant();
    }

    public boolean hasPlant(){
        return this.element.hasPlant();
    }

    public List<Animal> getAnimals() {
        return this.element.getAnimals();
    }

    public void setPlant(Plant plant){
        this.element.setPlant(plant);
    }

    public boolean onFire() {
        return this.element.onFire();
    }

    public void removeFire() {
        this.element.removeFire();
    }
}
