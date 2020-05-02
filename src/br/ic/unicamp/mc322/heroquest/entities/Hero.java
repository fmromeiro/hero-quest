package br.ic.unicamp.mc322.heroquest.entities;

import java.util.Collection;

public class Hero extends Character{

    public Hero(String name, int attackDice, int defendDice, int baseBodyPoints, int mindPoints, int x, int y) {
        super(name, attackDice, defendDice, baseBodyPoints, mindPoints, x, y);
    }
}
