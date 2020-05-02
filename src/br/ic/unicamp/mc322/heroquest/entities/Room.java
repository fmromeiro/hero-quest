package br.ic.unicamp.mc322.heroquest.entities;

import br.ic.unicamp.mc322.heroquest.auxiliars.Point;

import java.util.LinkedList;
import java.util.List;

public class Room {
    private final Point universalPosition;
    private final LinkedList<Entity> entities;

    public Room(Point universalPosition) {
        this.universalPosition = new Point(universalPosition);
        this.entities = new LinkedList<>();
    }

    public void addEntity(Entity entity) {
        this.entities.add(entity.getCopy());
    }
}
