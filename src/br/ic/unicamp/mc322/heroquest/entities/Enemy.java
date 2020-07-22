package br.ic.unicamp.mc322.heroquest.entities;

import br.ic.unicamp.mc322.heroquest.auxiliars.Point;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class Enemy extends Character {
    private final BiFunction<Enemy, List<Entity>,  List<Point>> moverFunction;
    private final BiConsumer<Enemy, List<Entity>> attackerFunction;

    public Enemy(String name, int attackDice, int defenedDice, int baseBodyPoints,
                 int mindPoints, String stringRepresentation,
                 BiFunction<Enemy, List<Entity>, List<Point>> moverFunction,
                 BiConsumer<Enemy, List<Entity>> attackerFunction) {
        super(name, attackDice, defenedDice, baseBodyPoints, mindPoints,
                stringRepresentation, false);

        this.moverFunction = moverFunction;
        this.attackerFunction = attackerFunction;
    }

    public void enemyTurn(Dungeon map) {
        List<Entity> entities = map.getEntities();
        List<Point> path = moverFunction.apply(this, entities);
        if (path.size() >= 2)
            map.moveEntity(this, path.get(path.size() - 1));
        attackerFunction.accept(this, entities);
    }
}
