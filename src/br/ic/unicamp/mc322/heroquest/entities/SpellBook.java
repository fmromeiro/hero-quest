package br.ic.unicamp.mc322.heroquest.entities;

import br.ic.unicamp.mc322.heroquest.auxiliars.Dice;
import br.ic.unicamp.mc322.heroquest.auxiliars.Point;
import br.ic.unicamp.mc322.heroquest.spells.Spell;

import java.util.ArrayList;
import java.util.List;

public class SpellBook {
    private final List<Spell> spellBook;
    private final Character caster;

    public SpellBook(Character caster) {
        this.spellBook = new ArrayList<>();
        this.caster = caster;
    }

    public void addItem(Spell item) {
        spellBook.add(item);
    }

    public Spell getItem(int index) {
        Spell item = spellBook.get(index);
        spellBook.remove(index);
        return item;
    }

    public Spell itemAt(int index) {
        return spellBook.get(index);
    }

    public boolean castSpell(int index, Point target) {
        if(itemAt(index).hasCharge()) {
            if (Dice.rollNumberDiceSum(1, 6) < caster.getAttribute(Character.Attribute.MINDPOINTS))
                itemAt(index).castSpell(caster.getPosition(), target);
            itemAt(index).useCharge();
            return true;
        } else return false;
    }
}