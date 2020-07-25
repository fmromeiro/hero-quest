package br.ic.unicamp.mc322.heroquest.items;
import br.ic.unicamp.mc322.heroquest.entities.StatusModifier;

public abstract class Item {
    String name;
    private final StatusModifier modifier;
    public Item(String name, StatusModifier modifier) {
        this.name = name;
        this.modifier = modifier;
    }

    public String getName() { return name; }

    public StatusModifier getModifier() {
        return modifier;
    }
    public abstract Item copy();
}
