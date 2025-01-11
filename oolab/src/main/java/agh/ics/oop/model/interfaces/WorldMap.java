package agh.ics.oop.model.interfaces;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.elements.Square;
import agh.ics.oop.model.records.Boundary;


public interface WorldMap {

    boolean place(Square square, Vector2d position);
    Square objectAt(Vector2d position);
    boolean isOccupied(Vector2d position);
    Boundary getCurrentBounds();
    boolean isInMap(Vector2d position);
}

