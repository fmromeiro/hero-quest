package br.ic.unicamp.mc322.heroquest.spells;

import br.ic.unicamp.mc322.heroquest.auxiliars.Point;
import br.ic.unicamp.mc322.heroquest.entities.Character;
import br.ic.unicamp.mc322.heroquest.entities.Dungeon;
import br.ic.unicamp.mc322.heroquest.entities.Entity;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

class Fireball extends Spell {
    protected Fireball(int quantity) {
        super("Fireball", Integer.MAX_VALUE, quantity);
    }

    @Override
    public void castSpell(Point source, Point target) {

        Entity targetEntity = Dungeon.getInstance().entityAt(target);
        List<Point> adjacentPositions = Point.crossAdjacents;
        if (targetEntity instanceof Character) {
            ((Character) targetEntity).takeDamage(6);
            if (!((Character) targetEntity).isAlive())
                Dungeon.getInstance().removeEntity(target);
        }
        adjacentPositions.stream()
                .map(point -> Point.sum(target, point))
                .filter(point -> Dungeon.getInstance().entityAt(point) instanceof Character)
                .forEach(point -> {
                    ((Character) Dungeon.getInstance().entityAt(point)).takeDamage(3);
                    if (!((Character) Dungeon.getInstance().entityAt(point)).isAlive())
                        Dungeon.getInstance().removeEntity(point);
                });
        Dungeon.getInstance().getSessionRenderer().printFireball(target);
    }
}
