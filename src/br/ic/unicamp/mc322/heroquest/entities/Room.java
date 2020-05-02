package br.ic.unicamp.mc322.heroquest.entities;

import br.ic.unicamp.mc322.heroquest.auxiliars.Point;

import java.util.LinkedList;
import java.util.List;

public class Room {
    private final Point topLeft, bottomRight;
    private final LinkedList<Entity> entities;
    private boolean visited;

    public Room(Point topLeft, Point bottomRight) {
        this.topLeft = new Point(topLeft);
        this.bottomRight = new Point(bottomRight);
        this.entities = new LinkedList<>();
    }

    public void addEntity(Entity entity) {
        this.entities.add(entity.getCopy());
    }

    public void removeEntity(Entity entity) {
        this.entities.removeIf(x -> x.equals(entity));
    }

    public boolean contains(Point point) {
        return this.topLeft.getX() <= point.getX() && this.bottomRight.getX() >= point.getX()
                && this.topLeft.getY() <= point.getY() && this.bottomRight.getY() >= point.getY();
    }
}
