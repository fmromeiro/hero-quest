package br.ic.unicamp.mc322.heroquest.items;

import br.ic.unicamp.mc322.heroquest.entities.Character;
import br.ic.unicamp.mc322.heroquest.entities.StatusModifier;

import java.util.function.Consumer;

public class Consumable extends Item {
    Consumer<Character> effect;
    public Consumable(String name, StatusModifier modifier, int range, Consumer<Character> effect) {
        super(name, modifier, range);
        this.effect = effect;
    }
    public void consume(Character c) {
        effect.accept(c);
    }

    @Override
    public Item copy() {
        return new Consumable(this.name, this.getModifier(), this.getRange(), this.effect);
    }
}
