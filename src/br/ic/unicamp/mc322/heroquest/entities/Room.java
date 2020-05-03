package br.ic.unicamp.mc322.heroquest.entities;

import br.ic.unicamp.mc322.heroquest.auxiliars.Point;

import java.util.*;

public class Room {
    protected final Point topLeft, bottomRight;
    protected static int nextId = 0;
    protected final HashMap<Integer, Entity> entities;
    protected boolean visited;
    private List<Door>

    public Room(Point topLeft, Point bottomRight) {
        this.topLeft = new Point(topLeft);
        this.bottomRight = new Point(bottomRight);
        this.entities = new HashMap<>();
        visited = false;
    }

    public Room(Room other) {
        this(other.topLeft, other.bottomRight);
        for (int id : other.entities.keySet())
            this.entities.put(id, other.entities.get(id).getCopy());
    }

    public int addEntity(Entity entity) {
        this.entities.put(nextId, entity.getCopy());
        if (entity instanceof Hero)
            this.visited = true;
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

    protected boolean hasHero() { return this.entities.values().stream().anyMatch(x -> x instanceof Hero); }

    public boolean isVisible() { return this.hasHero(); }

    public Collection<Room> visibleRooms() { return Arrays.asList(new Room(this)); }

    public Room getCopy() { return new Room(this); }

    public boolean contains(Point point) {
        return this.topLeft.getX() <= point.getX() && this.bottomRight.getX() >= point.getX()
                && this.topLeft.getY() <= point.getY() && this.bottomRight.getY() >= point.getY();
    }

    public Entity entityAt(Point point) throws IllegalArgumentException {
        if (!this.contains(point))
            throw new IllegalArgumentException("Point not in room");
        return this.entities.values().stream()
                .filter(x -> x.getPosition().equals(point))
                .findFirst()
                .orElse(null);
    }
}
