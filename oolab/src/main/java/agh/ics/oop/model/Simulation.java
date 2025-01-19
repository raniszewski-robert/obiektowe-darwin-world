package agh.ics.oop.model;

import agh.ics.oop.model.enums.MapVariant;
import agh.ics.oop.model.presenter.SimulationWorldPresenter;
import agh.ics.oop.model.records.WorldConfiguration;

public class Simulation implements Runnable {
    private WorldConfiguration config;
    private AbstractWorldMap worldMap;
    private SimulationWorldPresenter presenter;
    private boolean running = true;
    private boolean closedWindow = false;
    private int dayCounter = 0;
    private final Object lock = new Object();
    public Simulation(WorldConfiguration config, SimulationWorldPresenter presenter) {
        this.presenter = presenter;
        this.config = config;
        if(config.mapVariant() == MapVariant.NORMAL){
            this.worldMap = new GlobeMap(config.mapWidth(), config.mapHeight(), config.genomeVariant());
        }
        else{
            this.worldMap = new FireMap(config.mapWidth(), config.mapHeight(), config.genomeVariant(), config.burnTime(), config.fireFrequency());
        }
        this.worldMap.createMap(config.animalStart(), config.plantStart(),
                config.animalStartEnergy(), config.animalGenotypeLength(), config.animalEnergyUsedToReproduce());
        worldMap.addObserver(presenter);
    }

    public Object getLock() {
        return lock;
    }

    public AbstractWorldMap getWorldMap() {
        return worldMap;
    }

    public int getDayCounter() {
        return dayCounter;
    }

    public void setClosedWindow(boolean closedWindow) {
        this.closedWindow = closedWindow;
    }

    @Override
    public void run() {
        while (!closedWindow) {
            synchronized (lock) {
                while (!running) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            worldMap.removeDeadAnimals();
            worldMap.moveAllAnimals();
            worldMap.eatPlants(config.plantEnergy());
            worldMap.copulationAllAnimals(config.energyAllowingReproduction(), config.animalMutationMinimum(), config.animalMutationMaximum());
            worldMap.growPlants(config.plantDaily());
            if (worldMap instanceof FireMap) {
                ((FireMap) worldMap).spreadFire();
                if (dayCounter % config.fireFrequency() == 0) { // Execute every `fireFrequency` turns
                    ((FireMap) worldMap).startFire();
                }
            }
            worldMap.eatPlants(config.plantEnergy());
            worldMap.copulationAllAnimals(config.energyAllowingReproduction(), config.animalMutationMinimum(), config.animalMutationMaximum());
            worldMap.growPlants(config.plantDaily());
            worldMap.mapChanged();
            presenter.updateStatistics(dayCounter, config.saveToCSV());
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            dayCounter++;
        }
    }
    public void pause() {
        running = false;
    }

    public void resume() {
        synchronized (lock) {
            running = true;
            lock.notify();
        }
    }
}
