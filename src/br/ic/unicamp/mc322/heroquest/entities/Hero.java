package br.ic.unicamp.mc322.heroquest.entities;

import br.ic.unicamp.mc322.heroquest.auxiliars.Point;

public class Hero extends Character{

    public Hero(String name, int attackDice, int defendDice, int baseBodyPoints, int mindPoints, Point point) {
        super(name, attackDice, defendDice, baseBodyPoints, mindPoints, point, true);
    }

    public void move(Point.Direction direction) {
        this.moveTo(Point.sum(this.getPosition(), direction.getPosition()));
    }
}
