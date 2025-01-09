package agh.ics.oop.model.elements;

import agh.ics.oop.model.enums.GenomeVariant;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.round;

public class Genotype {
    List<Integer> genome;
    int currentGenomeIndex;
    int genomeSize;
    private static GenomeVariant genomeVariant;
    private static int minMutateNumber;
    private static int maxMutateNumber;
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

    public static void setMinMutateNumber(int minMutateNumber) {
        Genotype.minMutateNumber = minMutateNumber;
    }

    public static void setMaxMutateNumber(int maxMutateNumber) {
        Genotype.maxMutateNumber = maxMutateNumber;
    }

    public List<Integer> getGenome() {
        return genome;
    }

    public void setGenome(List<Integer> genome) {
        this.genome = genome;
    }

    public static void setGenomeVariant(GenomeVariant genomeVariant) {
        Genotype.genomeVariant = genomeVariant;
    }

    public int getCurrentGenomeIndex() {
        return currentGenomeIndex;
    }

    public void indexChange(){
        switch (genomeVariant){
            case NORMAL -> {
                setCurrentGenomeIndex(this.currentGenomeIndex+1 % genomeSize);
            }
            case CRAZY -> {
                if(Math.random() <= 0.2){
                    setCurrentGenomeIndex((int) round(Math.random()*genomeSize));
                }
                else{
                    setCurrentGenomeIndex(this.currentGenomeIndex+1 % genomeSize);
                }
            }
        }
    }

    public int getGenomeSize() {
        return genomeSize;
    }

    public void mutate(){
        Random random = new Random();
        int mutateNumber = random.nextInt(maxMutateNumber - minMutateNumber + 1) + minMutateNumber;
        for (int i = 0; i < mutateNumber; i++){
            int index = random.nextInt(genomeSize);
            int value = random.nextInt(8);
            this.genome.set(index, value);
        }
    }
    public Genotype createChildGenotype(Genotype otherParentGenotype, float energyPercent){
        int partIndex = (int)(energyPercent * genomeSize);

        int genomeSize = otherParentGenotype.getGenomeSize();
        Genotype childGenotype = new Genotype(genomeSize);
        double genomeSide = Math.random();
        List<Integer> childGenome = new ArrayList<>();
        if(genomeSide < 0.5){
            childGenome.addAll(this.genome.subList(0, partIndex));
            childGenome.addAll(otherParentGenotype.genome.subList(partIndex, genomeSize));
        }
        else{
            childGenome.addAll(otherParentGenotype.genome.subList(0, partIndex));
            childGenome.addAll(this.genome.subList(partIndex, genomeSize));
        }
        childGenotype.setGenome(childGenome);
        childGenotype.mutate();

        return childGenotype;
    }
}
