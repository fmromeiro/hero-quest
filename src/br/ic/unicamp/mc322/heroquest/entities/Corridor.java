package br.ic.unicamp.mc322.heroquest.entities;

import br.ic.unicamp.mc322.heroquest.auxiliars.Point;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class Corridor extends Room{
    private final HashMap<Integer, Room> corridors;
    private static int nextCorridorId = 0;

    public Corridor() {
        super(new Point(0,0), new Point(0, 0));
        this.corridors = new HashMap<>();
    }

    public Corridor(Corridor other) {
        this();
        for(Room room : other.corridors.values())
            this.corridors.put(nextCorridorId++, room.getCopy());
    }

    @Override
    public Room getCopy() {
        return new Corridor(this);
    }

    public int addCorridor(Room corridor) {
        this.corridors.put(nextCorridorId, new Room(corridor));
        return nextCorridorId++;
    }

    public Room removeCorridor(int id) {
        return this.corridors.remove(id);
    }

    public HashMap<Integer, Room> listParts() {
        HashMap<Integer, Room> rooms = (HashMap<Integer, Room>)this.entities.clone();
        rooms.replaceAll((key, value) -> value.getCopy());
        return rooms;
    }

    @Override
    public Collection<Room> visibleRooms() {
        if (!this.hasHero())
            return null;
        Point heroPosition = this.entities
                .values().stream()
                .filter(x -> x instanceof Hero)
                .findFirst().get()
                .getPosition();
        return this.corridors.values().stream()
                .filter(x -> x.contains(heroPosition))
                .map(x -> x.getCopy()).collect(Collectors.toList());
    }

    @Override
    public boolean contains(Point point) {
        return this.corridors.values().stream().anyMatch(x -> x.contains(point));
    }
}
