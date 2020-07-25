package br.ic.unicamp.mc322.heroquest.items;

import br.ic.unicamp.mc322.heroquest.entities.Character;
import br.ic.unicamp.mc322.heroquest.entities.StatusModifier;

public class Weapon extends Equipment{
    private final int range;
    public Weapon(String name, int damage, int range, Category category, boolean singleUse) {
        super(name, new StatusModifier(Character.Attribute.ATTACKDICE, damage), category, singleUse);
        this.range = range;
    }
    public int getRange() {
        return range;
    }
    public static Weapon getLongSword() {
        return new Weapon("Long Sword", 3, 1, Category.TWOHAND, false);
    }
    public static Weapon getShortSword() {
        return new Weapon("Short Sword", 2, 1, Category.ONEHAND, false);
    }
    public static Weapon getBrokenDagger() {
        return new Weapon("Broken Dagger", 1, 1, Category.ONEHAND, true);
    }
    public static Weapon getLongStaff() {
        return new Weapon("Long Staff", 2, 2, Category.ONEHAND, false);
    }
    public static Weapon getLongBow() {
        return new Weapon("Long Bow", 2, 4, Category.TWOHAND, false);
    }
}
