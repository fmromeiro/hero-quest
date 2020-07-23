package br.ic.unicamp.mc322.heroquest.spells;

import br.ic.unicamp.mc322.heroquest.entities.Character;

public abstract class Spell {
    public enum Element {
        WATER("Water Spell"),
        EARTH("Earth Spell"),
        FIRE("Fire Spell"),
        AIR("Air Spell"),
        CHAOS("Chaos Spell");

        private final String name;

        Element(String name) { this.name = name; }

        public String getName() { return name; }
    }
    private final String name;
    private final Element element;
    private final int range;
    private int quantity;
    public Spell(String name, Element spellElement, int range, int quantity) {
        this.name = name;
        this.element = spellElement;
        this.range = range;
        this.quantity = quantity;
    }

    public Element getElement() {
        return element;
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

    public abstract void castSpell(Character target);

}
