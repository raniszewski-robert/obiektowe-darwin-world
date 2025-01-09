package agh.ics.oop.model;

import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Genotype;

public record Simulation(AbstractWorldMap map) {

    public void run(int energyOfPlant, int energyAllowingCopulation, int numberOfPlants) {
        map.removeDeadAnimals();
        map.moveAllAnimals();
        map.eatPlants(energyOfPlant);
        map.copulationAllAnimals(energyAllowingCopulation);
        map.growPlants(numberOfPlants);
    }

}
