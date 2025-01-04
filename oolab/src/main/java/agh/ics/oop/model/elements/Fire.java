package agh.ics.oop.model.elements;

import agh.ics.oop.model.Vector2d;

public class Fire {
    private int remainingTurns;
    private Vector2d position;

    public Fire(Vector2d position, int turns) {
        this.position = position;
        this.remainingTurns = turns;
    }

    public void tick() {
        remainingTurns--;
    }

    public Vector2d getPosition() {
        return position;
    }

    public boolean isDead() {
        return remainingTurns <= 0;
    }
}
