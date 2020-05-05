package br.ic.unicamp.mc322.heroquest.spells;

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
    public Spell(String name, Element spellElement, int range) {
        this.name = name;
        this.element = spellElement;
        this.range = range;
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
}
