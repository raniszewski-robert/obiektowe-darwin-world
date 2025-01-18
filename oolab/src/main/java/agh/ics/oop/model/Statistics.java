package agh.ics.oop.model;

import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Genotype;
import agh.ics.oop.model.elements.Plant;
import agh.ics.oop.model.elements.Square;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Statistics {
    private AbstractWorldMap map;

    public Statistics(AbstractWorldMap map) {
        this.map = map;
    }

    public int countAnimals(){
        int counter = 0;
        for(Square square : this.map.getAllSquares()){
            counter += square.getAnimals().size();
        }
        return counter;
    }

    public int countPlants(){
        int counter = 0;
        for(Square square : this.map.getAllSquares()){
            Plant currPlant = square.getPlant();
            if(currPlant != null) counter++;
        }
        return counter;
    }

    public int countFreeSquares(){
        int numberOfAllSquares = this.map.height * this.map.width;
        int numberOfOccupiedSquares = this.map.getAllSquares().size();
        return numberOfAllSquares - numberOfOccupiedSquares;
    }

    public Genotype getMostCommonGenotype(){
        List<Animal> animals = map.animals;
        int mostCommonGenotypeCounter = 0;
        HashMap<Genotype, Integer> mostCommonGenotypes = new HashMap<>();
        for(Animal animal : animals){
            if(mostCommonGenotypes.containsKey(animal.getGenotype())){
                mostCommonGenotypes.compute(animal.getGenotype(), (k, val) -> val + 1);
            }
            else{
                mostCommonGenotypes.put(animal.getGenotype(), 1);
            }
        }
        List<Genotype> mostCommonGenotypeList = mostCommonGenotypes.entrySet().stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue())) // Sortowanie malejące po wartościach
                .map(Map.Entry::getKey) // Ekstrakcja kluczy (Genotype)
                .toList();
        return mostCommonGenotypeList.getFirst();
    }

    public double getAverageEnergy(){
        double sum = 0;
        double counter = 0;
        for(Square square : this.map.getAllSquares()){
            List<Animal> currAnimals = square.getAnimals();
            for(Animal animal : currAnimals){
                if(animal.getEnergy() > 0){
                    sum += animal.getEnergy();
                    counter++;
                }

            }
        }
        if(counter == 0) return 0;
        return Math.round(sum/counter * 100.0) / 100.0;
    }

    public double getAverageLifeLength(){
        double sum = 0;
        double counter = 0;
        return map.getDeadAnimals().stream()
                .mapToInt(Animal::getAge)
                .average()
                .orElse(0.0);
    }

    public double getAverageChildrenCount(){
        double sum = 0;
        double counter = 0;
        for(Square square : this.map.getAllSquares()){
            List<Animal> currAnimals = square.getAnimals();
            for(Animal animal : currAnimals){
                if(animal.getEnergy() >0){
                    sum += animal.getChildrenCount();
                    counter++;
                }
            }
        }
        if(counter == 0) return 0;
        return Math.round(sum/counter * 100.0) / 100.0;
    }

    @Override
    public String toString() {
        return "count animals: " + countAnimals() + "\n" + "count plants" + countPlants() + "\n"
                + "count squares: " + countFreeSquares() + "\n" + "average energy: " + getAverageEnergy() + "\n"
                + "average life length: " + getAverageLifeLength()
                + "\n" + "average children count: " + getAverageChildrenCount()
                + "\n" + "average children life length: " + getAverageLifeLength()
                + "\n" + "average children children count: " + getAverageChildrenCount();
    }

}