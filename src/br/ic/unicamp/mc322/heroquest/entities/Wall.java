package br.ic.unicamp.mc322.heroquest.entities;

import br.ic.unicamp.mc322.heroquest.auxiliars.Point;

public class Wall implements StaticEntity {
    private boolean seen;
    private Point position;

    @Override
    public Point getPosition() {
        return this.position;
    }

    @Override
    public void setPosition(Point position) {
        this.position = this.position == null ? position : null;
    }

    @Override
    public boolean canSeeThrough() {
        return false;
    }

    @Override
    public boolean canBeOverlapped() {
        return false;
    }

    @Override
    public String getStringRepresentation() {
        return "▓▓";
    }

    public void setAsSeen() { this.seen = true; }

    public boolean isSeen() { return this.seen; }
}
