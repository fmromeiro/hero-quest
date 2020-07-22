package br.ic.unicamp.mc322.heroquest.entities;

import br.ic.unicamp.mc322.heroquest.auxiliars.Point;
import br.ic.unicamp.mc322.heroquest.items.Consumable;
import br.ic.unicamp.mc322.heroquest.items.Equipment;
import br.ic.unicamp.mc322.heroquest.items.Item;

import java.util.HashMap;
import java.util.Map;

public abstract class Character extends Entity {

    public enum Attribute {
        ATTACKDICE("Attack Dice"),
        DEFENDDICE("Defend Dice"),
        BASEBODYPOINTS("Base Body Points"),
        MINDPOINTS("Mind Points");

        private final String name;

        Attribute(String name) { this.name = name; }

        public String getName() { return this.name; }
    }

    private final HashMap<Integer, StatusModifier> statusModifiers;
    private int statusModifierIndex;

    private final String name;
    private int attackDice;
    private int defendDice;

    private int baseBodyPoints;
    private int mindPoints;
    private int currentBodyPoints;

    private Inventory inventory;
    private Map<String, Equipment> body;
    public Character(String name, int attackDice, int defendDice, int baseBodyPoints, int mindPoints, Point point, boolean seeThrough) {
        super(point, seeThrough);
        this.name = name;
        this.attackDice = attackDice;
        this.defendDice = defendDice;
        this.baseBodyPoints = baseBodyPoints;
        this.mindPoints = mindPoints;
        this.currentBodyPoints = baseBodyPoints;
        this.statusModifiers = new HashMap<>();
        this.statusModifierIndex = 0;
        this.inventory = new Inventory();
        this.body = new HashMap<>();
    }

    public void takeDamage(int damage) {
        this.currentBodyPoints = Math.max(this.currentBodyPoints - damage, 0);
    }

    public void healDamage(int heal) {
        this.currentBodyPoints = Math.min(this.currentBodyPoints + heal, this.baseBodyPoints);
    }

    public boolean isAlive() { return this.currentBodyPoints > 0; }

    public int getAttribute(Attribute attribute) {
        int modifier = this.getModifiersFor(attribute);
        switch (attribute) {
            case ATTACKDICE: return this.attackDice + modifier;
            case DEFENDDICE: return this.defendDice + modifier;
            case BASEBODYPOINTS: return this.baseBodyPoints + modifier;
            case MINDPOINTS: return this.mindPoints + modifier;
            default: return 0;
        }
    }

    public int addModifier(Attribute attribute, int modifier) {
        statusModifiers.put(this.statusModifierIndex, new StatusModifier(attribute, modifier));
        this.statusModifierIndex++;
        return this.statusModifierIndex - 1;
    }

    public void removeModifier(int index) {
        statusModifiers.remove(index);
    }

    private int getModifiersFor(Attribute attribute) {
        return this.statusModifiers
                .values().stream()
                .filter(x -> x.getAttribute() == attribute)
                .mapToInt(StatusModifier::getModifier)
                .sum() +
                this.body.values().stream()
                .filter(x -> x.getModifier().getAttribute() == attribute)
                .mapToInt(x -> x.getModifier().getModifier())
                .sum();
    }

    public void selectItem(int index) {
        Item item = inventory.getItem(index);
        if(item instanceof Equipment)
            equip((Equipment) item);
        else if (item instanceof Consumable)
            ((Consumable) item).consume(this);
        }

    public void equip(Equipment equipment) {
        String key = equipment.getCategory().getCategoryName();
        if(key.equals("ONE_HAND"))
            if(body.get("RIGHT_HAND") == null)
                body.put("RIGHT_HAND", equipment);
            else {
                inventory.addItem(body.remove("LEFT_HAND"));
                body.put("LEFT_HAND", equipment);
            }
        else
        if(key.equals("TWO_HAND")) {
            inventory.addItem(body.remove("RIGHT_HAND"));
            body.put("RIGHT_HAND", equipment);
            inventory.addItem(body.remove("LEFT_HAND"));
            body.put("LEFT_HAND", equipment);
        }
        else {
            inventory.addItem(body.remove(key));
            body.put(key, equipment);
        }
    }

    public void equip(int index, char hand) {
        Item item = inventory.itemAt(index);
        if(item instanceof Equipment) {
            Equipment equipment = (Equipment) inventory.getItem(index);
            if (equipment.getCategory() == Equipment.Category.ONEHAND) {
                if (hand == 'r') {
                    inventory.addItem(body.remove("RIGHT_HAND"));
                    body.put("RIGHT_HAND", equipment);
                } else if (hand == 'l') {
                    inventory.addItem(body.remove("LEFT_HAND"));
                    body.put("LEFT_HAND", equipment);
                }
            }
        }
    }

    @Override
    public boolean canBeOverlapped() {
        return false;
    }
}