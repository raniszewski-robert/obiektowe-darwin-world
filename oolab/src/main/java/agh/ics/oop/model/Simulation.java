package agh.ics.oop.model;

import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Genotype;
import agh.ics.oop.model.enums.MapVariant;
import agh.ics.oop.model.records.WorldConfiguration;

public class Simulation {
    WorldConfiguration config;
    AbstractWorldMap worldMap;
    public Simulation(WorldConfiguration config) {
        this.config = config;
        if(config.mapVariant() == MapVariant.NORMAL){
            AbstractWorldMap worldMap = new GlobeMap(config.mapWidth(), config.mapHeight());
        }
        else{
            AbstractWorldMap worldMap = new FireMap(config.mapWidth(), config.mapHeight(), config.burnTime(), config.fireFrequency());
        }
        worldMap.createMap(config.animalStart(), config.plantStart(), config.animalStartEnergy(), config.animalGenotypeLength());
        

    }
    public void run(int energyOfPlant, int energyAllowingCopulation, int numberOfPlants) {
        worldMap.removeDeadAnimals();
        worldMap.moveAllAnimals();
        worldMap.eatPlants(energyOfPlant);
        worldMap.copulationAllAnimals(energyAllowingCopulation);
        worldMap.growPlants(numberOfPlants);
    }

}
