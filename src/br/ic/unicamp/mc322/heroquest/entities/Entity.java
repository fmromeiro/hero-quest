package br.ic.unicamp.mc322.heroquest.entities;

import br.ic.unicamp.mc322.heroquest.auxiliars.Point;

import java.util.Collection;

public abstract class Entity {
    private final Point position;

    public Entity(int x, int y) {
        this.position = new Point(x, y);
    }

    public int getX() { return this.position.getX(); }

    public int getY() { return this.position.getY(); }

    public void moveTo(int x, int y) {
        this.position.moveTo(x, y);
    }
}
