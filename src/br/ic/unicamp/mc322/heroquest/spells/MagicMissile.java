package br.ic.unicamp.mc322.heroquest.spells;

import br.ic.unicamp.mc322.heroquest.auxiliars.Point;
import br.ic.unicamp.mc322.heroquest.entities.Character;
import br.ic.unicamp.mc322.heroquest.entities.Dungeon;
import br.ic.unicamp.mc322.heroquest.entities.Entity;

class MagicMissile extends Spell {

    protected MagicMissile(int quantity) {
        super("Magic Missile", Integer.MAX_VALUE, quantity);
    }

    @Override
    public void castSpell(Point source, Point target) {
        Entity targetEntity = Dungeon.getInstance().entityAt(target);
        if (targetEntity instanceof Character) {
            ((Character) targetEntity).takeDamage(6);
            if(!((Character) targetEntity).isAlive())
                Dungeon.getInstance().removeEntity(target);
        }
    }
}
