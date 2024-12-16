package agh.ics.oop.model.elements;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.interfaces.WorldElement;

public class Plant implements WorldElement {
    private Vector2d position;

    public Plant(Vector2d position) {
        this.position = position;
    }

    @Override
    public Vector2d getPosition() {
        return null;
    }
}
