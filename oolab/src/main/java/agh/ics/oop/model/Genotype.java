package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.round;

public class Genotype {
    List<Integer> genome;
    int currentGenomeIndex;
    int genomeSize;
    public Genotype(int genomeSize){
        this.genome = new ArrayList<>();
        this.genomeSize = genomeSize;
        for (int i = 0; i < genomeSize; i++){
            this.genome.add((int) round(Math.random()*8));
        }
        this.currentGenomeIndex = (int) round(Math.random()*genomeSize);
    }

    public void setCurrentGenomeIndex(int currentGenomeIndex) {
        this.currentGenomeIndex = currentGenomeIndex;
    }

    public List<Integer> getGenome() {
        return genome;
    }

    public int getCurrentGenomeIndex() {
        return currentGenomeIndex;
    }
}
