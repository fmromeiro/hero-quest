package br.ic.unicamp.mc322.heroquest.spells;

public class InstantDamageSpell extends Spell{
    private final int damage;
    public InstantDamageSpell(String name, Element spellElement, int range, int damage) {
        super(name, spellElement, range);
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }
}
