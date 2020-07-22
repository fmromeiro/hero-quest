package br.ic.unicamp.mc322.heroquest.entities;

import br.ic.unicamp.mc322.heroquest.auxiliars.Point;

import java.security.InvalidParameterException;

public abstract class Entity{
    private Point position;
    public Entity(Point point, boolean seeThrough) {
        this.position = new Point(point);
    }

    public Point getPosition() { return new Point(this.position); }

    public abstract boolean canSeeThrough();

    public abstract boolean canBeOverlapped();

    public void moveTo(Point point) {
        this.position = point;
    }

    public abstract String getStringRepresentation();
}
