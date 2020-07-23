package br.ic.unicamp.mc322.heroquest.spells;

import br.ic.unicamp.mc322.heroquest.auxiliars.Point;
import br.ic.unicamp.mc322.heroquest.auxiliars.TriConsumer;
import br.ic.unicamp.mc322.heroquest.entities.Character;
import br.ic.unicamp.mc322.heroquest.entities.Tile;

import java.util.List;

public class Spell {
    private final String name;
    private final int range;
    private int quantity;
    private final TriConsumer<Point, Point, List<Tile>> runner;

    protected Spell(String name, int range, int quantity, TriConsumer<Point, Point, List<Tile>> runner) {
        this.name = name;
        this.range = range;
        this.quantity = quantity;
        this.runner = runner;
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

    public void castSpell(Point source, Point target, List<Tile> map) {
        this.runner.accept(source, target, map);
    };

}
