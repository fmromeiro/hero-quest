package br.ic.unicamp.mc322.heroquest.spells;

import br.ic.unicamp.mc322.heroquest.auxiliars.Point;
import br.ic.unicamp.mc322.heroquest.entities.Dungeon;

class Teleport extends Spell {

    protected Teleport(int quantity) {
        super("Teleport", Integer.MAX_VALUE, quantity);
    }

    @Override
    public void castSpell(Point source, Point target) {
        Dungeon.getInstance().moveEntity(Dungeon.getInstance().entityAt(source), target);
    }
}
