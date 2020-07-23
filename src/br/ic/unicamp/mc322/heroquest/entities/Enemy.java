package br.ic.unicamp.mc322.heroquest.entities;

import br.ic.unicamp.mc322.heroquest.auxiliars.Point;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class Enemy extends Character {
    protected final BiFunction<Enemy, List<Entity>,  List<Point>> moverFunction;
    protected final BiConsumer<Enemy, List<Entity>> attackerFunction;

    protected Enemy(String name, int attackDice, int defendDice,
                    int baseBodyPoints, int mindPoints,
                    String stringRepresentation,
                    BiFunction<Enemy, List<Entity>,  List<Point>> moverFunction,
                    BiConsumer<Enemy, List<Entity>> attackerFunction) {
        super(name, attackDice, defendDice, baseBodyPoints, mindPoints, stringRepresentation, false);
        this.moverFunction = moverFunction;
        this.attackerFunction = attackerFunction;
    }

    public List<Point> getMovement(List<Entity> entities) { return this.moverFunction.apply(this, entities); }
    public void getAttackerFunction(List<Entity> entities) { this.attackerFunction.accept(this, entities); };
}
