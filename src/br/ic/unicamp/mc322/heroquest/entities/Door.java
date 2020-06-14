package br.ic.unicamp.mc322.heroquest.entities;

import br.ic.unicamp.mc322.heroquest.auxiliars.Point;

public class Door { // Should this only be visible from Room?
    private final Point entry;
    private final Room room;

    public Door(Point entry, Room room){
        this.entry = new Point(entry); // Should I verify if this isn't a teleportation?
        this.room = room;
    }

    public boolean canExit() { return this.room.entityAt(this.entry) == null; }

    public boolean exit(Entity entity) {
        if (!this.canExit())
            return false;
        this.room.addEntity(entity);
        return true;
    }
}
