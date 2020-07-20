package br.ic.unicamp.mc322.heroquest.entities;

import br.ic.unicamp.mc322.heroquest.auxiliars.Point;
import br.ic.unicamp.mc322.heroquest.exceptions.ObjectOverlayException;
import br.ic.unicamp.mc322.heroquest.exceptions.RoomOverlayException;

import java.security.interfaces.RSAMultiPrimePrivateCrtKey;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class Dungeon {
    private final List<Entity> entities;
    private final List<Room> rooms;
    private final int width, height;

    public Dungeon(int width, int height) {
        this.width = width;
        this.height = height;
        this.entities = new LinkedList<>();
        this.rooms = new LinkedList<>();
    }

    public void addEntity(Entity entity) throws ObjectOverlayException {
        if (this.entities.stream().anyMatch(ent ->
                ent.getPosition().getX() == entity.getPosition().getX()
                        && ent.getPosition().getY() == entity.getPosition().getY()))
            throw new ObjectOverlayException("There's already a Entity at the specified position");

        this.entities.add(entity);

        if (entity instanceof Hero)
            this.rooms.stream()
                    .filter(room -> room.contains(entity.getPosition()))
                    .forEach(Room::setVisited);
    }

    public void updateHero() {
        this.entities.stream()
                .filter(entity -> entity instanceof Hero)
                .forEach(hero -> this.rooms.stream()
                        .filter(room -> room.contains(hero.getPosition()))
                        .forEach(Room::setVisited));
    }

    public void addRoom(Point topLeft, Point lowerRight) throws RoomOverlayException {
        if (this.rooms.stream().anyMatch(room ->
                        room.contains(topLeft) || room.contains(lowerRight)))
            throw new RoomOverlayException("Requested room would overlap with other room");

        this.rooms.add(new Room(topLeft, lowerRight));
    }

    public void addCorridor(Point topLeft, Point lowerRight) throws RoomOverlayException {
        if (this.rooms.stream().anyMatch(room ->
                room.contains(topLeft) || room.contains(lowerRight)))
            throw new RoomOverlayException("Requested corridor would overlap with other room");

        Corridor corridor = new Corridor();
        corridor.addRoom(topLeft, lowerRight);
        this.rooms.add(corridor);
    }

    public void addRoomToCorridor(int id, Point topLeft, Point lowerRight)
            throws RoomOverlayException, NoSuchElementException {
        if (this.rooms.stream().anyMatch(room ->
                room.contains(topLeft) || room.contains(lowerRight)))
            throw new RoomOverlayException("Requested corridor would overlap with other room");

        ((Corridor)this.rooms.stream()
                .filter(room -> room instanceof Corridor && room.getId() == id)
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("Couldn't find corridor with Id "+ id)))
                .addRoom(topLeft, lowerRight);
    }
}

class Room {
    protected static int nextId = 0;
    protected final int id;
    protected final Point topLeft, lowerRight;
    protected boolean visited;

    protected Room(Point topLeft, Point lowerRight) {
        this.id = nextId++;
        this.topLeft = topLeft;
        this.lowerRight = lowerRight;
        this.visited = false;
    }

    public int getId() {
        return id;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited() {
        this.visited = true;
    }

    public boolean contains(Point point) {
        return point.getX() >= topLeft.getX() && point.getX() <= lowerRight.getX()
                && point.getY() >= topLeft.getY() && point.getY() <= lowerRight.getY();
    }
}

class Corridor extends Room {
    protected final List<Room> rooms;

    protected Corridor() {
        super(null, null);
        this.rooms = new LinkedList<Room>();
    }

    protected void addRoom(Point topLeft, Point lowerRight) {
        this.rooms.add(new Room(topLeft, lowerRight));
    }

    @Override
    public boolean contains(Point point) {
        return this.rooms.stream().anyMatch(room -> room.contains(point));
    }
}