package br.ic.unicamp.mc322.heroquest.entities;

import br.ic.unicamp.mc322.heroquest.auxiliars.Point;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Room {
    private final Point topLeft, bottomRight;
    private static int nextId = 0;
    private final HashMap<Integer, Entity> entities;
    private boolean visited;

    public Room(Point topLeft, Point bottomRight) {
        this.topLeft = new Point(topLeft);
        this.bottomRight = new Point(bottomRight);
        this.entities = new HashMap<>();
    }

    public int addEntity(Entity entity) {
        this.entities.put(nextId, entity.getCopy());
        return this.nextId++;
    }

    public Entity removeEntity(int id) {
        return this.entities.remove(id);
    }

    public Entity removeEntity(Entity entity) {
        for (int id : this.entities.keySet())
            if (this.entities.get(id).equals(entity))
                return this.removeEntity(id);
        return null;
    }

    private boolean hasHero() { return this.entities.values().stream().anyMatch(x -> x instanceof Hero); }

    public boolean contains(Point point) {
        return this.topLeft.getX() <= point.getX() && this.bottomRight.getX() >= point.getX()
                && this.topLeft.getY() <= point.getY() && this.bottomRight.getY() >= point.getY();
    }
}
