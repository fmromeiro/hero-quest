package br.ic.unicamp.mc322.heroquest.items;
import br.ic.unicamp.mc322.heroquest.entities.StatusModifier;

public abstract class Item {
    private final StatusModifier modifier;
    private final int range;

    public Item(StatusModifier modifier, int range) {
        this.range = range;
        this.modifier = modifier;
    }

    public int getRange() {
        return range;
    }

    public StatusModifier getModifier() {
        return modifier;
    }
}
