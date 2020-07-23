package br.ic.unicamp.mc322.heroquest.spells;

import br.ic.unicamp.mc322.heroquest.auxiliars.Dice;
import br.ic.unicamp.mc322.heroquest.auxiliars.Point;
import br.ic.unicamp.mc322.heroquest.auxiliars.TriConsumer;
import br.ic.unicamp.mc322.heroquest.entities.Character;
import br.ic.unicamp.mc322.heroquest.entities.Entity;
import br.ic.unicamp.mc322.heroquest.entities.Tile;

import java.util.Arrays;
import java.util.List;

public class Spell {
    private final String name;
    private final int range;
    private int quantity;
    private final TriConsumer<Point, Point, Tile[][]> runner;

    protected Spell(String name, int range, int quantity, TriConsumer<Point, Point, Tile[][]> runner) {
        this.name = name;
        this.range = range;
        this.quantity = quantity;
        this.runner = runner;
    }

    public Spell getSimpleHeal(int quantity) {
        return new Spell("Simple Heal", Integer.MAX_VALUE, quantity,
                (target, goal, tiles) -> {
                    Entity targetEntity = tiles[goal.getY()][goal.getX()].getEntity();
                    if (targetEntity instanceof Character)
                        ((Character)targetEntity).healDamage(Dice.rollNumberDice(1, 6).stream().mapToInt(x -> x).sum());
                });
    }

    public String getName() {
        return this.name;
    }

    public int getRange() {
        return range;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean hasCharge() {
        return quantity > 0;
    }

    public void useCharge() {
        if(hasCharge()) quantity--;
    }

    public void castSpell(Point source, Point target, Tile[][] map) throws Exception {
        List<Point> line = Arrays.asList(Point.bresenhamLine(source, target));
        if (line.size() > 2
                && line.subList(1, line.size()).stream()
                .anyMatch(point -> {
                    Entity current = map[point.getY()][point.getX()].getEntity();
                    return current != null && !current.canSeeThrough();
                }))
            throw new Exception("Can't see target");
        this.runner.accept(source, target, map);
    };

}
