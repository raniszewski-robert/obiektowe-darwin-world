package agh.ics.oop.model;

import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Genotype;
import agh.ics.oop.model.elements.Plant;
import agh.ics.oop.model.elements.Square;

import java.util.*;
import java.util.stream.Collectors;

public class Statistics {
    private final AbstractWorldMap map;

    public Statistics(AbstractWorldMap map) {
        this.map = map;
    }

    public int countAnimals(){
        return this.map.getAllSquares().stream()
                .mapToInt(square -> square.getAnimals().size())
                .sum();
    }

    public int countPlants(){
        return (int)this.map.getAllSquares().stream()
                .filter(square -> square.getPlant() != null).count();
    }

    public int countFreeSquares(){
        return (int) this.map.getAllSquares().stream()
                .filter(Square::isEmpty).count();
    }

    public Genotype getMostCommonGenotype(){
        List<Animal> animals = map.animals;
         Genotype genotype = animals.stream()
                .collect(Collectors.groupingBy(Animal::getGenotype, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
         if(genotype != null) {
             return genotype;
         }
         return null;
    }

    public double getAverageEnergy(){
        double average = this.map.getAnimals().stream()
                .filter(animal -> animal.getEnergy() > 0)
                .mapToDouble(Animal::getEnergy)
                .average().orElse(0.0);

        return Math.round(average * 100.0) / 100.0;

    }

    public double getAverageLifeLength(){
        return map.getDeadAnimals().stream()
                .mapToInt(Animal::getAge)
                .average()
                .orElse(0.0);
    }

    public double getAverageChildrenCount(){
        double average = map.getAnimals().stream()
                    .mapToDouble(Animal::getChildrenCount)
                    .average().orElse(0.0);

        return Math.round(average * 100.0) / 100.0;
    }

    public static List<Integer> convertGenome(Genotype genotype) {

        return genotype.getGenome().stream()
                .map(value -> {
                    switch (value) {
                        case 5: return 7;
                        case 2: return 2;
                        case 1: return 3;
                        case 7: return 5;
                        case 3: return 1;
                        case 0: return 4;
                        case 4: return 0;
                        case 6: return 6;
                        default: throw new IllegalArgumentException("Unexpected value: " + value);
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "count animals: " + countAnimals() + "\n" + "count plants" + countPlants() + "\n"
                + "count squares: " + countFreeSquares() + "\n" + "average energy: " + getAverageEnergy() + "\n"
                + "average life length: " + getAverageLifeLength()
                + "\n" + "average children count: " + getAverageChildrenCount()
                + "\n" + "average children life length: " + getAverageLifeLength();
    }

}