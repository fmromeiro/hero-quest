package br.ic.unicamp.mc322.heroquest.entities;

import br.ic.unicamp.mc322.heroquest.auxiliars.Dice;
import br.ic.unicamp.mc322.heroquest.auxiliars.Point;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Enemy extends Character {
    protected final Function<Enemy, List<Point>> moverFunction;
    protected final BiConsumer<Enemy, List<Entity>> attackerFunction;
    protected final int movementSquares;

    protected Enemy(String name, int attackDice, int defendDice,
                    int baseBodyPoints, int mindPoints,
                    String stringRepresentation, int movementSquares,
                    Function<Enemy, List<Point>> moverFunction,
                    BiConsumer<Enemy, List<Entity>> attackerFunction, boolean isCaster) {
        super(name, attackDice, defendDice, baseBodyPoints, mindPoints, stringRepresentation, Dice.CombatDiceValue.MONSTER_SHIELD, isCaster);
        this.movementSquares = movementSquares;
        this.moverFunction = moverFunction;
        this.attackerFunction = attackerFunction;
    }

    @Override
    public int getSteps() {
        return this.movementSquares;
    }

    public List<Point> getMovement() { return this.moverFunction.apply(this); }
    public void getAttackerFunction(List<Entity> entities) { this.attackerFunction.accept(this, entities); };
}
