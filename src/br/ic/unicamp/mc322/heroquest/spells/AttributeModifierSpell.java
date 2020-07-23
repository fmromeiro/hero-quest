package br.ic.unicamp.mc322.heroquest.spells;

import br.ic.unicamp.mc322.heroquest.entities.StatusModifier;

public class AttributeModifierSpell extends Spell{
    StatusModifier modifier;

    public AttributeModifierSpell(String name, Element spellElement, int range, StatusModifier modifier) {
        super(name, spellElement, range);
        this.modifier = modifier;
    }
}
