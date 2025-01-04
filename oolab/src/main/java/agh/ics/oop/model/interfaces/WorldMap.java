package agh.ics.oop.model.interfaces;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.MapFieldElement;
import agh.ics.oop.model.records.Boundary;


public interface WorldMap {

    boolean place(MapFieldElement element, Vector2d position);
    MapFieldElement objectAt(Vector2d position);
    boolean isOccupied(Vector2d position);
    Boundary getCurrentBounds();
    boolean isInMap(Vector2d position);
}

