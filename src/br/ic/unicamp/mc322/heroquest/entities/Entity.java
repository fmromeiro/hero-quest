package br.ic.unicamp.mc322.heroquest.entities;

import br.ic.unicamp.mc322.heroquest.auxiliars.Point;

public abstract class Entity {
    private final Point position;

    public Entity(Point point) {
        this.position = new Point(point);
    }

    public int getX() { return this.position.getX(); }

    public int getY() { return this.position.getY(); }

    public void moveTo(Point point) {
        this.position.moveTo(point);
    }
}
