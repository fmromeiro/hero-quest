package br.ic.unicamp.mc322.heroquest.entities;

import br.ic.unicamp.mc322.heroquest.auxiliars.Point;

import java.util.HashSet;
import java.util.Set;

public class Tile {
    private Entity currentEntity;
    private Entity secondaryEntity;
    private final Set<Integer> rooms;
    private final Point position;

    protected Tile(int x, int y) {
        this.currentEntity = null;
        this.secondaryEntity = null;
        this.rooms = new HashSet<>();
        this.position = new Point(x, y);
    }

    public void setToRoom(int roomIndex) {
        this.rooms.add(roomIndex);
    }

    public void removeFromRoom(int roomIndex) {
        this.rooms.remove(roomIndex);
    }

    public Entity getEntity() { return this.currentEntity; }

    public Entity removeEntity() {
        Entity current = this.currentEntity;
        if (this.secondaryEntity != null) {
            this.currentEntity = this.secondaryEntity;
            this.secondaryEntity = null;
        }
        else
            this.currentEntity = null;
        return current;
    }

    public void setEntity(Entity entity) {
        if (this.currentEntity!= null && this.currentEntity.canBeOverlapped())
            this.secondaryEntity = this.currentEntity;
        this.currentEntity = entity;
        if (entity != null)
            entity.moveTo(this.position);
    }

    protected boolean isInRoom(int roomId) {
        return this.rooms.contains(roomId);
    }

    public String getStringRepresentation() {
        return this.currentEntity != null
                ? this.currentEntity.getStringRepresentation()
                : this.rooms.isEmpty()
                    ? "░░"
                    : "..";
    }

    public Set<Integer> getRooms() {
        return new HashSet<>(this.rooms);
    }

    public boolean canSetEntity() { return this.currentEntity == null || this.currentEntity.canBeOverlapped(); }
}