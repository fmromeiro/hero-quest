package br.ic.unicamp.mc322.heroquest.entities;

import br.ic.unicamp.mc322.heroquest.auxiliars.Point;

import java.util.*;

public class Room {
    protected final Point topLeft, bottomRight;
    protected final LinkedList<Entity> entities;
    private final List<Door> doors;
    private boolean visited;

    public Room(Point topLeft, Point bottomRight) {
        this.topLeft = new Point(topLeft);
        this.bottomRight = new Point(bottomRight);
        this.entities = new LinkedList<>();
        this.doors = new ArrayList<Door>();
        this.visited = false;
    }

    public Room(Room other) {
        this(other.topLeft, other.bottomRight);
        this.entities.addAll(other.entities);
    }

    public void addEntity(Entity entity) {
        this.entities.add(entity);
        if (entity instanceof Hero)
            this.visited = true;
    }

    public boolean removeEntity(Entity entity) {
        return this.entities.remove(entity);
    }

    protected boolean hasHero() { return this.entities.stream().anyMatch(x -> x instanceof Hero); }

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
        return this.entities.stream()
                .filter(x -> x.getPosition().equals(point))
                .findFirst()
                .orElse(null);
    }
}
