package br.ic.unicamp.mc322.heroquest.entities;

import br.ic.unicamp.mc322.heroquest.auxiliars.Point;

public class Hero extends Character{

    public Hero(String name, int attackDice, int defendDice, int baseBodyPoints, int mindPoints, Point point) {
        super(name, attackDice, defendDice, baseBodyPoints, mindPoints, point, true);
    }

    public Hero(Hero other) {
        super(other);
    }

    public void move(Point.Direction direction) {
        Point finalPos = this.getPosition();
        finalPos.sum(direction.getPosition());
        this.moveTo(finalPos);
    }
}
