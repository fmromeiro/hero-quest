package br.ic.unicamp.mc322.heroquest.entities;

import br.ic.unicamp.mc322.heroquest.auxiliars.Point;

import java.security.InvalidParameterException;

public abstract class Entity {
    private final Point position;

    public Entity(Point point) {
        this.position = new Point(point);
    }

    public Point getPosition() { return new Point(this.position); }

    public void moveTo(Point point) {
        this.position.moveTo(point);
    }

    public abstract Entity getCopy();
}
