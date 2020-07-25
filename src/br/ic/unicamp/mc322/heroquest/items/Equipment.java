package br.ic.unicamp.mc322.heroquest.items;

import br.ic.unicamp.mc322.heroquest.entities.Character;
import br.ic.unicamp.mc322.heroquest.entities.StatusModifier;

public class Equipment extends Item {
    public enum Category {
        HEADGEAR("HEAD"),
        ARMOR("ARMOR"),
        ONEHAND("ONE_HAND"),
        TWOHAND("TWO_HAND"),
        ARTIFACT("ARTIFACT");

        private final String categoryName;

        Category(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getCategoryName() {
            return categoryName;
        }
    }
    private final Category category;
    private final boolean singleUse;

    public Equipment(String name, StatusModifier modifier, Category category, boolean singleUse) {
        super(name, modifier);
        this.category = category;
        this.singleUse = singleUse;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isSingleUse() {
        return singleUse;
    }

    @Override
    public Item copy() {
        return new Equipment(this.name, this.getModifier(), this.category, this.singleUse);
    }

    public Equipment getHelmet() {
        return new Equipment("Simple Helmet", new StatusModifier(Character.Attribute.DEFENDDICE, 1), Category.HEADGEAR, false);
    }

    public Equipment getPlateMail() {
        return new Equipment("Plate Mail", new StatusModifier(Character.Attribute.DEFENDDICE, 3), Category.ARMOR, false);
    }

    public Equipment getShield() {
        return new Equipment("Simple Shield", new StatusModifier(Character.Attribute.DEFENDDICE, 1), Category.ONEHAND, false);
    }
}