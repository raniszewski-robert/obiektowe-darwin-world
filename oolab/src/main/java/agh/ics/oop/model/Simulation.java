package agh.ics.oop.model;

import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Genotype;
import agh.ics.oop.model.enums.MapVariant;
import agh.ics.oop.model.records.WorldConfiguration;

public class Simulation implements Runnable {
    private WorldConfiguration config;
    private AbstractWorldMap worldMap;
    public Simulation(WorldConfiguration config) {
        try {
            System.out.println(config.toString());
            this.config = config;
            if(config.mapVariant() == MapVariant.NORMAL){
                this.worldMap = new GlobeMap(config.mapWidth(), config.mapHeight());
            }
            else{
                this.worldMap = new FireMap(config.mapWidth(), config.mapHeight(), config.burnTime(), config.fireFrequency());
            }
            this.worldMap.createMap(config.animalStart(), config.plantStart(), config.animalStartEnergy(), config.animalGenotypeLength());
            Animal.setChildrenEnergy(config.animalEnergyUsedToReproduce());
            Genotype.setMinMutateNumber(config.animalMutationMinimum());
            Genotype.setMaxMutateNumber(config.animalMutationMaximum());
            Genotype.setGenomeVariant(config.genomeVariant());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public AbstractWorldMap getWorldMap() {
        return worldMap;
    }

    @Override
    public void run() {
        worldMap.removeDeadAnimals();
        worldMap.moveAllAnimals();
        worldMap.eatPlants(config.plantEnergy());
        worldMap.copulationAllAnimals(config.energyAllowingReproduction());
        worldMap.growPlants(config.plantDaily());
    }

}
