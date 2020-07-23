package br.ic.unicamp.mc322.heroquest.items;
import br.ic.unicamp.mc322.heroquest.entities.StatusModifier;

public abstract class Item {
    String name;
    private final StatusModifier modifier;
    private final int range;

    public Item(String name, StatusModifier modifier, int range) {
        this.name = name;
        this.range = range;
        this.modifier = modifier;
    }

    public int getRange() {
        return range;
    }

    public StatusModifier getModifier() {
        return modifier;
    }

    public abstract Item copy();
}
