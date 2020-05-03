package br.ic.unicamp.mc322.heroquest.itens;

import br.ic.unicamp.mc322.heroquest.entities.StatusModifier;

public class Equipment extends Item {
    public enum EquipmentCategory {HEADGEAR, ARMOR, ONEHAND, TWOHAND, STACKABLE}
    private final EquipmentCategory category;

    public Equipment(StatusModifier modifier, int range, EquipmentCategory category) {
        super(modifier, range);
        this.category = category;
    }

    public EquipmentCategory getCategory() {
        return category;
    }

}