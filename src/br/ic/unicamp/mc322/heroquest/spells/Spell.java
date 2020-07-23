package br.ic.unicamp.mc322.heroquest.spells;

import br.ic.unicamp.mc322.heroquest.auxiliars.Dice;
import br.ic.unicamp.mc322.heroquest.auxiliars.Point;
import br.ic.unicamp.mc322.heroquest.entities.Character;
import br.ic.unicamp.mc322.heroquest.entities.Dungeon;
import br.ic.unicamp.mc322.heroquest.entities.Entity;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

public class Spell {
    private final String name;
    private final int range;
    private int quantity;

    protected Spell(String name, int range, int quantity) {
        this.name = name;
        this.range = range;
        this.quantity = quantity;
    }

    public static Spell getSimpleHeal(int quantity) {
        return new SimpleHeal(quantity);
    }

    public static Spell getFireball(int quantity) {
        return new Fireball(quantity);
    }

    public static Spell getMagicMissile(int quantity) {
        return new MagicMissile(quantity);
    }

    public static Spell getTeleport(int quantity) {
        return new Teleport(quantity);

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

    public abstract void castSpell(Point source, Point target);

}
