package br.ic.unicamp.mc322.heroquest.items;

import br.ic.unicamp.mc322.heroquest.entities.StatusModifier;

public class Equipment extends Item {
    public enum Category {HEADGEAR, ARMOR, ONEHAND, TWOHAND, ARTIFACT}
    private final Category category;

    public Equipment(String name, StatusModifier modifier, int range, Category category) {
        super(name, modifier, range);
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

}