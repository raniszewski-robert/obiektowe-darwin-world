package agh.ics.oop.model;

import agh.ics.oop.model.elements.*;
import agh.ics.oop.model.enums.GenomeVariant;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FireMap extends AbstractWorldMap{
    private final List<Fire> fires;
    public int burnTime;
    public int fireFrequency;

    protected FireMap(int width, int height, int burnTime, int fireFrequency) {
        super(width, height);
        this.fires = new ArrayList<>();
        this.burnTime = burnTime;
        this.fireFrequency = fireFrequency;
    }

    public void startFire() {
        if (!plants.isEmpty()) {
            Random random = new Random();
            int randomIndex = random.nextInt(plants.size()); // Wybierz losowy indeks
            Plant randomPlant = plants.get(randomIndex);
            Vector2d position = randomPlant.getPosition();
            Square square = mapSquares.get(position);
            Fire fire = new Fire(position, burnTime);
            square.addFire(fire);
            fires.add(fire);
        }
    }

    public void spreadFire() {
        List<Fire> newFires = new ArrayList<>();
        List<Fire> deadFires = new ArrayList<>();
        for (Fire fire : fires) {
            Vector2d position = fire.getPosition();
            List <Vector2d> neighbors = getNeighbors(position);

            for (Vector2d neighbor : neighbors) {
                Square square = mapSquares.get(neighbor);
                if (square != null && square.hasPlant() && !square.onFire()) {
                    newFires.add(new Fire(neighbor, burnTime));
                    square.addFire(new Fire(neighbor, burnTime));
                }
            }

            Square currSquare = mapSquares.get(position);
            fire.tick();
            if (fire.isDead()) {
                deadFires.add(fire);
                Plant currPlant = currSquare.getPlant();
                plants.remove(currPlant);
                currSquare.removePlant();
                currSquare.removeFire();
                mapSquares.remove(position);
            }
        }
        fires.removeAll(deadFires);
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
