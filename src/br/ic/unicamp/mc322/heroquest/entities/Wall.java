package br.ic.unicamp.mc322.heroquest.entities;

import br.ic.unicamp.mc322.heroquest.auxiliars.Point;

public class Wall extends StaticEntity {
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
}
