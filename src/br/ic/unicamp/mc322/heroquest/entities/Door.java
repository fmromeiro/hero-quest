package br.ic.unicamp.mc322.heroquest.entities;

import br.ic.unicamp.mc322.heroquest.auxiliars.Point;

public class Door extends Entity {
    public Door(Point position) {
        super(position, false);
    }

    @Override
    public void moveTo(Point point) {}

    public void open() {
        this.seeThrough = true;
    }

    public void close() {
        this.seeThrough = false;
    }
}
