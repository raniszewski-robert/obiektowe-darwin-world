package agh.ics.oop.model.interfaces;

import agh.ics.oop.model.Vector2d;

import java.util.List;

public interface MapChangeListener {
    void mapChanged(WorldMap worldMap, List<String> messages, List<Vector2d> list);
}
