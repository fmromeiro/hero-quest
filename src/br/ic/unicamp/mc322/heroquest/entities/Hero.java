package br.ic.unicamp.mc322.heroquest.entities;

import br.ic.unicamp.mc322.heroquest.auxiliars.Point;

import java.security.InvalidParameterException;
import java.util.Collection;

public class Hero extends Character{

    public Hero(String name, int attackDice, int defendDice, int baseBodyPoints, int mindPoints, Point point) {
        super(name, attackDice, defendDice, baseBodyPoints, mindPoints, point);
    }

    public Hero(Hero other) {
        super(other);
    }

    @Override
    public Entity getCopy(){
        return new Hero(this);
    }

    @Override
    public boolean equals(Entity other) {
        if (!(other instanceof Hero))
            return false;
        return super.equals(other);
    }

    public void step(Point.Direction direction) {
        Point finalPos = this.getPosition();
        finalPos.sum(direction.getPosition());
        this.moveTo(finalPos);
    }
}
