package br.ic.unicamp.mc322.heroquest.spells;

import br.ic.unicamp.mc322.heroquest.entities.Character;
import br.ic.unicamp.mc322.heroquest.entities.StatusModifier;

public class AttributeModifierSpell extends Spell{
    StatusModifier modifier;

    public AttributeModifierSpell(String name, Element spellElement, int range, int quantity, StatusModifier modifier) {
        super(name, spellElement, range, quantity);
        this.modifier = modifier;
    }

    @Override
    public void castSpell(Character target) {
        target.addModifier(modifier);
        useCharge();
    }
}
