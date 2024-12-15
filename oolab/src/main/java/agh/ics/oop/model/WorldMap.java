package agh.ics.oop.model;

import agh.ics.oop.model.Vector2d;

public interface WorldMap {

    boolean place(WorldElement element, Vector2d position);
    boolean canMoveTo(Vector2d position);
    Object objectAt(Vector2d position);
    boolean isOccupied(Vector2d position);

}
