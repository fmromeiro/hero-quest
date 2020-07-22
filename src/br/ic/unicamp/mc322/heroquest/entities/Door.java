package br.ic.unicamp.mc322.heroquest.entities;

import br.ic.unicamp.mc322.heroquest.auxiliars.Point;

public class Door extends Entity {
    private boolean open;

    public Door(Point position) {
        super(position, false);
        this.open = false;
    }

    @Override
    public boolean canSeeThrough() {
        return this.open;
    }

    @Override
    public boolean canBeOverlapped() {
        return this.open;
    }

    @Override
    public void moveTo(Point point) {}

    @Override
    public String getStringRepresentation() {
        return open ? "--" : "══";
    }

    public void open() {
        this.open = true;
    }

    public void close() {
        this.open = false;
    }
}
