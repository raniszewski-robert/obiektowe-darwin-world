package agh.ics.oop.model;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimulationEngine {
    public void runAsyncInThreadPool(List<Simulation> simulations) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        for (Simulation simulation : simulations) {
            executorService.submit(simulation);
        }
        executorService.shutdown();
    }
}