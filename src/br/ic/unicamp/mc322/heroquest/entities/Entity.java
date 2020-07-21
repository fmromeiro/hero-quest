package br.ic.unicamp.mc322.heroquest.entities;

import br.ic.unicamp.mc322.heroquest.auxiliars.Point;

import java.security.InvalidParameterException;

public interface Entity {
    Point getPosition();

    void setPosition(Point position);

    boolean canSeeThrough();

    boolean canBeOverlapped();

    String getStringRepresentation();
}
