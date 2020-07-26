package br.ic.unicamp.mc322.heroquest.entities;

import br.ic.unicamp.mc322.heroquest.auxiliars.Point;
import br.ic.unicamp.mc322.heroquest.items.Equipment;
import br.ic.unicamp.mc322.heroquest.items.Item;
import br.ic.unicamp.mc322.heroquest.items.Weapon;

import java.util.Random;

public class Treasure extends StaticEntity {
    private Item loot;
    private Point position;

    public Treasure (Item item) {
        this.loot = item;
    }

    public Item seeLoot() { return this.loot; }

    public Item getLoot() { return loot.copy(); }

    @Override
    public Point getPosition() {
        return this.position;
    }

    @Override
    public void setPosition(Point position) {
        this.position = this.position == null ? position : null;
    }

    @Override
    public boolean canSeeThrough() {
        return true;
    }

    @Override
    public boolean canBeOverlapped() {
        return true;
    }

    @Override
    public String getStringRepresentation() {
        return "$$";
    }

    public static Treasure randomTreasure () {
        Random rng = new Random();
        Item item = null;
        switch (rng.nextInt(8)) {
            case 0:
                item = Weapon.getBrokenDagger();
                break;
            case 1:
                item = Weapon.getLongBow();
                break;
            case 2:
                item = Weapon.getLongStaff();
                break;
            case 3:
                item = Weapon.getLongSword();
                break;
            case 4:
                item = Weapon.getShortSword();
                break;
            case 5:
                item = Equipment.getHelmet();
                break;
            case 6:
                item = Equipment.getPlateMail();
                break;
            case 7:
                item = Equipment.getShield();
                break;
        }
        return new Treasure(item);
    }
}
