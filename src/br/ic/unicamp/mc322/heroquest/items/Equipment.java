package br.ic.unicamp.mc322.heroquest.items;

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

    public Equipment(String name, StatusModifier modifier, int range, Category category) {
        super(name, modifier, range);
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }
    @Override
    public Item copy() {
        return new Equipment(this.name, this.getModifier(), this.getRange(), this.category);
    }
}