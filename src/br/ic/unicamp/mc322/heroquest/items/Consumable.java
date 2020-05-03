package br.ic.unicamp.mc322.heroquest.items;

import br.ic.unicamp.mc322.heroquest.entities.StatusModifier;

public class Consumable extends Item {
    private int charges;

    public Consumable(String name, StatusModifier modifier, int range, int charges) {
        super(name, modifier, range);
        this.charges = charges;
    }

    public int getCharges() {
        return charges;
    }

    public void consume() {
        this.charges--;
    }

    public void storeConsumable() {
        this.charges++;
    }

}
