package br.ic.unicamp.mc322.heroquest.entities;

import br.ic.unicamp.mc322.heroquest.auxiliars.Point;
import br.ic.unicamp.mc322.heroquest.spells.Spell;

import java.util.ArrayList;
import java.util.List;

public class SpellBook {
    private final List<Spell> spellBook;

    public SpellBook() {
        this.spellBook = new ArrayList<>();
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

    public boolean castSpell(int index, Character source, Point target, Tile[][] map) throws Exception {
        if(itemAt(index).hasCharge()) {
            itemAt(index).castSpell(source.getPosition(), target, map);
            itemAt(index).useCharge();
            return true;
        } else return false;
    }
}