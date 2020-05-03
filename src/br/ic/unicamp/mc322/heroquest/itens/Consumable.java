package br.ic.unicamp.mc322.heroquest.itens;

import br.ic.unicamp.mc322.heroquest.entities.StatusModifier;

public class Consumable extends Item {
    private int charges;

    public Consumable(StatusModifier modifier, int range, int charges) {
        super(modifier, range);
        this.charges = charges;
    }

    public int getCharges() {
        return charges;
    }

    public void setCharges(int charges) {
        this.charges = charges;
    }

    public void consume() {
        this.charges--;
    }

    public void storeConsumable() {
        this.charges++;
    }

}
