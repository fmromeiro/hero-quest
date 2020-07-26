package br.ic.unicamp.mc322.heroquest.entities;

import br.ic.unicamp.mc322.heroquest.auxiliars.Dice;
import br.ic.unicamp.mc322.heroquest.auxiliars.Point;
import br.ic.unicamp.mc322.heroquest.items.Item;
import br.ic.unicamp.mc322.heroquest.spells.Spell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Map<Integer, Spell> getSpells() {
        Map<Integer, Spell> spells = new HashMap<>();
        for (int i = 0; i < spellBook.size(); i++)
            spells.put(i, itemAt(i));
        return spells;
    }

    public boolean castSpell(int index, Point target) {
        if(itemAt(index).hasCharge()) {
            if (Dice.rollNumberDiceSum(1, 6) < caster.getAttribute(Character.Attribute.MINDPOINTS))
                itemAt(index).castSpell(caster.getPosition(), target);
            itemAt(index).useCharge();
            return true;
        } else return false;
    }

    public boolean castSpell(Spell spell, Point target) throws Exception{
        if (!spellBook.contains(spell))
            throw new Exception("Spell " + spell.getName() + " not found in spell book");
        if(spell.hasCharge()) {
            if (Dice.rollNumberDiceSum(1, 6) < caster.getAttribute(Character.Attribute.MINDPOINTS))
                spell.castSpell(caster.getPosition(), target);
            spell.useCharge();
            return true;
        } else return false;
    }
}