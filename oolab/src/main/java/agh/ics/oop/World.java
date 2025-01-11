package agh.ics.oop;

import agh.ics.oop.model.SimulationApp;
import agh.ics.oop.model.presenter.SimulationStartPresenter;
import javafx.application.Application;

public class World {
    public static void main(String[] args) {
        System.out.println("System rozpoczal dzialanie");
        Application.launch(SimulationApp.class, args);
        System.out.println("System zakonczyl dzialanie");
    }
}
