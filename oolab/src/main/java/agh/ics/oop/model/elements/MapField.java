package agh.ics.oop.model.elements;

import agh.ics.oop.model.MapFieldElement;
import agh.ics.oop.model.Vector2d;

public class MapField {

    private final Vector2d position;
    private MapFieldElement element;
    public MapField(Vector2d position) {
        this.position = position;
        this.element = new MapFieldElement();
    }
}
