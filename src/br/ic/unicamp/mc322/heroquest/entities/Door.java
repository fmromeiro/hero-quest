package br.ic.unicamp.mc322.heroquest.entities;

import br.ic.unicamp.mc322.heroquest.auxiliars.Point;

public class Door extends StaticEntity {
    private boolean open;
    private Point position;

    public Door() {
        this.open = false;
    }

    @Override
    public Point getPosition() {
        return position;
    }

    @Override
    public void setPosition(Point position) {
        this.position = this.position == null ? position : null;
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
    public String getStringRepresentation() {
        return open ? "--" : "══";
    }

    public void open() {
        this.open = true;
    }
}
