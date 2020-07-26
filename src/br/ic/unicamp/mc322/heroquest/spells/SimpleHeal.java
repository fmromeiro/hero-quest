package br.ic.unicamp.mc322.heroquest.spells;

import br.ic.unicamp.mc322.heroquest.auxiliars.Dice;
import br.ic.unicamp.mc322.heroquest.auxiliars.Point;
import br.ic.unicamp.mc322.heroquest.entities.Character;
import br.ic.unicamp.mc322.heroquest.entities.Dungeon;
import br.ic.unicamp.mc322.heroquest.entities.Entity;

import java.util.function.BiConsumer;

class SimpleHeal extends Spell{
    protected SimpleHeal(int quantity) {
        super("Simple Heal", Integer.MAX_VALUE, quantity);
    }

    @Override
    public void castSpell(Point source, Point target) {
        Entity targetEntity = Dungeon.getInstance().entityAt(target);
        if (targetEntity instanceof Character)
            ((Character)targetEntity).healDamage(Dice.rollNumberDiceSum(1, 6));
    }
}
