package agh.ics.oop.model.interfaces;
import agh.ics.oop.model.Animal;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.records.Boundary;

import java.util.List;


public interface WorldMap {
    boolean place(Animal animal);
    boolean canMoveTo(Vector2d position);
    WorldElement objectAt(Vector2d position);
    boolean isOccupied(Vector2d position);
    Boundary getCurrentBounds();

}

