package br.ic.unicamp.mc322.heroquest.entities;

import br.ic.unicamp.mc322.heroquest.auxiliars.Point;

public class Wall extends Entity{
    private boolean seen;

    public Wall(Point position) {
        super(position, false);
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
    public void moveTo(Point point) {}

    @Override
    public String getStringRepresentation() {
        return "▓▓";
    }

    public void setAsSeen() { this.seen = true; }

    public boolean isSeen() { return this.seen; }
}
