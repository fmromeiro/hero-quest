package br.ic.unicamp.mc322.heroquest.entities;

import br.ic.unicamp.mc322.heroquest.auxiliars.Point;

import java.security.InvalidParameterException;

public abstract class Entity{
    private final Point position;
    protected final boolean seeThrough;

    public Entity(Point point, boolean seeThrough) {
        this.position = new Point(point);
        this.seeThrough = seeThrough;
    }

    public Point getPosition() { return new Point(this.position); }

    public boolean canSeeThrough() {
        return seeThrough;
    }

    public void moveTo(Point point) {
        this.position.moveTo(point);
    }
}
