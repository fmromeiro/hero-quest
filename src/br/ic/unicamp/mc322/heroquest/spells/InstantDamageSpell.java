package br.ic.unicamp.mc322.heroquest.spells;

import br.ic.unicamp.mc322.heroquest.entities.Character;

public class InstantDamageSpell extends Spell{
    private final int damage;
    public InstantDamageSpell(String name, Element spellElement, int range, int quantity, int damage) {
        super(name, spellElement, range, quantity);
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }

    @Override
    public void castSpell(Character target) {
        target.takeDamage(damage);
        useCharge();
    }
}
