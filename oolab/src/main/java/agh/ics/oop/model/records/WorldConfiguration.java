package agh.ics.oop.model.records;

import agh.ics.oop.model.enums.GenomeVariant;
import agh.ics.oop.model.enums.MapVariant;

public record WorldConfiguration(int mapHeight,
                                 int mapWidth,
                                 MapVariant mapVariant,
                                 int plantStart,
                                 int plantDaily,
                                 int plantEnergy,
                                 int animalStart,
                                 int animalStartEnergy,
                                 int animalEnergyReproduction,
                                 int animalEnergyDaily,
                                 int animalEnergyUsedToReproduce,
                                 int animalMutationMinimum,
                                 int animalMutationMaximum,
                                 int animalGenotypeLength,
                                 int fireFrequency,
                                 int burnTime,
                                 GenomeVariant genomeVariant) {
}
