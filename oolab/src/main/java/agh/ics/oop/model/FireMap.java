package agh.ics.oop.model;

import agh.ics.oop.model.elements.*;
import agh.ics.oop.model.enums.GenomeVariant;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class FireMap extends AbstractWorldMap{
    private List<Fire> fires;
    public int burnTime;

    protected FireMap(int width, int height, int burnTime, GenomeVariant genomeVariant) {
        super(width, height);
        this.fires = new ArrayList<>();
    }

    public void spreadFire() {
        List<Fire> newFires = new ArrayList<>();
        for (Fire fire : fires) {
            Vector2d position = fire.getPosition();
            List <Vector2d> neighbors = getNeighbors(position);

            for (Vector2d neighbor : neighbors) {
                Square square = mapSquares.get(position);
                if (square != null && square.hasPlant() && !square.onFire()) {
                    newFires.add(new Fire(neighbor, burnTime));
                }
            }

            fire.tick();
            if (fire.isDead()) {
                fires.remove(fire);
                this.mapSquares.get(position).removeFire();
            }
        }
        fires.addAll(newFires);
    }

    private List<Vector2d> getNeighbors(Vector2d position) {
        List<Vector2d> neighbors = new ArrayList<>();
        Vector2d up = position.add(new Vector2d(0, 1));
        Vector2d right = position.add(new Vector2d(1, 0));
        Vector2d down = position.add(new Vector2d(0, -1));
        Vector2d left = position.add(new Vector2d(-1, 0));

        if (isInMap(up)){neighbors.add(up);}
        if (isInMap(right)){neighbors.add(right);}
        if (isInMap(down)){neighbors.add(down);}
        if (isInMap(left)){neighbors.add(left);}

        return neighbors;
    }

}
