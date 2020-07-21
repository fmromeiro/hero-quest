package br.ic.unicamp.mc322.heroquest.entities;

import br.ic.unicamp.mc322.heroquest.auxiliars.Point;

public class Wall extends Entity{
    public Wall(Point position) {
        super(position, false);
    }

    @Override
    public void moveTo(Point point) {}

    @Override
    public String getStringRepresentation() {
        return "▓▓";
    }
}
