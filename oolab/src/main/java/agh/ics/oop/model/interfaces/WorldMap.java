package agh.ics.oop.model.interfaces;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.records.Boundary;


public interface WorldMap {

    boolean place(WorldElement element, Vector2d position);
    boolean canMoveTo(Vector2d position);
    WorldElement objectAt(Vector2d position);
    boolean isOccupied(Vector2d position);
    Boundary getCurrentBounds();

}

