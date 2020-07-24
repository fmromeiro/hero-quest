package br.ic.unicamp.mc322.heroquest.spells;

import br.ic.unicamp.mc322.heroquest.auxiliars.Dice;
import br.ic.unicamp.mc322.heroquest.auxiliars.Point;
import br.ic.unicamp.mc322.heroquest.auxiliars.TriConsumer;
import br.ic.unicamp.mc322.heroquest.entities.Character;
import br.ic.unicamp.mc322.heroquest.entities.Dungeon;
import br.ic.unicamp.mc322.heroquest.entities.Entity;
import br.ic.unicamp.mc322.heroquest.entities.Tile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

public class Spell {
    private final String name;
    private final int range;
    private int quantity;
    private final BiConsumer<Point, Point> runner;

    protected Spell(String name, int range, int quantity, BiConsumer<Point, Point> runner) {
        this.name = name;
        this.range = range;
        this.quantity = quantity;
        this.runner = runner;
    }

    public Spell getSimpleHeal(int quantity) {
        return new Spell("Simple Heal", Integer.MAX_VALUE, quantity,
                (source, goal) -> {
                    Entity targetEntity = Dungeon.getInstance().entityAt(goal);
                    if (targetEntity instanceof Character)
                        ((Character)targetEntity).healDamage(Dice.rollNumberDiceSum(1, 6));
                });
    }

    public Spell getFireball(int quantity) {
        return new Spell("Fireball", Integer.MAX_VALUE, quantity,
                (source, goal) -> {
                    Entity targetEntity = Dungeon.getInstance().entityAt(goal);
                    List<Point> adjacentPositions = Arrays.asList(new Point(0, -1), new Point(1, 0), new Point(0, 1), new Point(-1, 0));
                    if (targetEntity instanceof Character)
                        ((Character)targetEntity).takeDamage(6);
                    adjacentPositions.stream()
                            .map(point -> Point.sum(goal, point))
                            .filter(point -> Dungeon.getInstance().entityAt(point) instanceof Character)
                            .forEach(point -> ((Character)Dungeon.getInstance().entityAt(point)).takeDamage(3));
                });
    }

    public Spell getMagicMissile(int quantity) {
        return new Spell("Magic Missile", Integer.MAX_VALUE, quantity,
                (source, goal) -> {
                    Entity targetEntity = Dungeon.getInstance().entityAt(goal);
                    if (targetEntity instanceof Character)
                        ((Character)targetEntity).takeDamage(6);
                });
    }

    public Spell getTeleport(int quantity) {
        return new Spell("Teleport", Integer.MAX_VALUE, quantity,
                (source, goal) -> { Dungeon.getInstance().moveEntity(Dungeon.getInstance().entityAt(source), goal); });

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

    public void castSpell(Point source, Point target) {
        this.runner.accept(source, target);
    };

}
