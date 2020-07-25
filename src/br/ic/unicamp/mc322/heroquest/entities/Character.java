package br.ic.unicamp.mc322.heroquest.entities;

import br.ic.unicamp.mc322.heroquest.auxiliars.Dice;
import br.ic.unicamp.mc322.heroquest.auxiliars.Point;
import br.ic.unicamp.mc322.heroquest.items.Consumable;
import br.ic.unicamp.mc322.heroquest.items.Equipment;
import br.ic.unicamp.mc322.heroquest.items.Item;
import br.ic.unicamp.mc322.heroquest.items.Weapon;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.Map;
import java.util.List;

public class Character implements Entity {

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

    private Dice.CombatDiceValue defendDiceValue;

    private Point position;

    private final String stringRepresentation;

    private Inventory inventory;
    private Map<String, Equipment> body;
    private Equipment[] hands;
    private int selectedWeapon;

    private final int RIGHT_HAND = 0;
    private final int LEFT_HAND = 1;
    protected Character(String name, int attackDice, int defendDice, int baseBodyPoints, int mindPoints, String stringRepresentation, Dice.CombatDiceValue defendDiceValue) {
        this.name = name;
        this.attackDice = attackDice;
        this.defendDice = defendDice;
        this.baseBodyPoints = baseBodyPoints;
        this.mindPoints = mindPoints;
        this.currentBodyPoints = baseBodyPoints;
        this.statusModifiers = new HashMap<>();
        this.statusModifierIndex = 0;
        this.stringRepresentation = stringRepresentation;
        this.defendDiceValue = defendDiceValue;
        this.inventory = new Inventory();
        this.body = new HashMap<>();
        this.hands = new Equipment[2];
    }
    public static Character getDefaultHero(String name) {
        return new Character(name, 2, 2, 10, 5, "ME", Dice.CombatDiceValue.HERO_SHIELD);
    }

    public static Enemy getMeleeSkeleton(String name) {
        Enemy skeleton = new Enemy(name, 2, 2, 1, 0, "SK", 6, EnemyFunctions.moveRandomly, null);
        return skeleton;
    }

    public static Enemy getSkeletonMage(String name) {
        Enemy skeletonMage = new Enemy(name, 2, 2, 1, 0, "SM", 6, EnemyFunctions.moveRandomly, null);
        return skeletonMage;
    }
    public static Enemy getGoblin(String name) {
        Enemy goblin = new Enemy(name, 2, 1, 1, 1, "GB", 10, EnemyFunctions.followHero, null);
        return goblin;
    }
    public void takeDamage(int damage) {
        this.currentBodyPoints = Math.max(this.currentBodyPoints - damage, 0);
    }

    public void healDamage(int heal) {
        this.currentBodyPoints = Math.min(this.currentBodyPoints + heal, this.baseBodyPoints);
    }

    public boolean isAlive() { return this.currentBodyPoints > 0; }

    public int getAttribute(Attribute attribute) {
        switch (attribute) {
            case ATTACKDICE: return this.attackDice + this.getModifiersFor(attribute, false);
            case DEFENDDICE: return this.defendDice + this.getModifiersFor(attribute, true);
            case BASEBODYPOINTS: return this.baseBodyPoints + this.getModifiersFor(attribute, true);
            case MINDPOINTS: return this.mindPoints + this.getModifiersFor(attribute, true);
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

    public int getSteps() {
        return Dice.throwMovementDice(2);
    }

    private int getModifiersFor(Attribute attribute, boolean handItemsModifiers) {
        return this.statusModifiers
                .values().stream()
                .filter(x -> x.getAttribute() == attribute)
                .mapToInt(StatusModifier::getModifier)
                .sum() +
                this.body.values().stream()
                .filter(x -> x.getModifier().getAttribute() == attribute)
                .mapToInt(x -> x.getModifier().getModifier())
                .sum() +
                (handItemsModifiers ? getHandItemsModifierFor(attribute) : 0);
    }

    private int getHandItemsModifierFor(Attribute attribute) {
        return Arrays.stream(this.hands)
                .filter(x -> x.getModifier().getAttribute() == attribute)
                .mapToInt(x -> x.getModifier().getModifier())
                .sum();
    }

    public boolean isHero() { return this.defendDiceValue == Dice.CombatDiceValue.HERO_SHIELD; }

    @Override
    public Point getPosition() {
        return this.position;
    }

    @Override
    public void setPosition(Point position) {
        this.position = position;
    }

    @Override
    public boolean canSeeThrough() {
        return this.isHero();
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
            if(hands[RIGHT_HAND] == null)
                hands[RIGHT_HAND] = equipment;
            else {
                inventory.addItem(hands[LEFT_HAND]);
                hands[LEFT_HAND] = equipment;
            }
        else
        if(key.equals("TWO_HAND")) {
            inventory.addItem(hands[RIGHT_HAND]);
            hands[RIGHT_HAND] = equipment;
            inventory.addItem(hands[LEFT_HAND]);
            hands[LEFT_HAND] = equipment;
        }
        else {
            inventory.addItem(body.remove(key));
            body.put(key, equipment);
        }
    }

    public int getAttackDamage() {
        return attackDice + getModifiersFor(Attribute.ATTACKDICE, false);
    }

    public int getDefendDice() {
        return defendDice + getModifiersFor(Attribute.DEFENDDICE, true);
    }

    public void equip(int index, char hand) {
        Item item = inventory.itemAt(index);
        if(item instanceof Equipment) {
            Equipment equipment = (Equipment) inventory.getItem(index);
            if (equipment.getCategory() == Equipment.Category.ONEHAND) {
                if (hand == 'r') {
                    inventory.addItem(hands[RIGHT_HAND]);
                    hands[RIGHT_HAND] = equipment;
                } else if (hand == 'l') {
                    inventory.addItem(hands[LEFT_HAND]);
                    hands[LEFT_HAND] = equipment;
                }
            }
        }
    }

    public void chooseWeapon(int hand) {
        this.selectedWeapon = hand;
    }



    public void attack(Character target) throws Exception {
        List<Point> line = Arrays.asList(Point.bresenhamLine(this.getPosition(), target.getPosition()));
        Tile[][] map = Dungeon.getInstance().getMap();
        if (line.size() > 2 && line.subList(1, line.size()).stream().anyMatch(point -> {
                    Entity current = map[point.getY()][point.getX()].getEntity();
                    return current != null && !current.canSeeThrough();
                }))
            throw new Exception("Can't see target");
        int distance = Point.manhattanDistance(this.getPosition(), target.getPosition());
        int weaponDamage = hands[selectedWeapon] != null && hands[selectedWeapon] instanceof Weapon ? hands[selectedWeapon].getModifier().getModifier() : 0;
        int weaponRange = hands[selectedWeapon] != null && hands[selectedWeapon] instanceof Weapon ? ((Weapon)hands[selectedWeapon]).getRange() : 1;

        if(weaponRange < distance)
            throw new Exception("Out of range target");

        int attackDamage = Dice.throwCombatDice(this.getAttackDamage() + weaponDamage, Dice.CombatDiceValue.SKULL);
        for(Map.Entry<String, Equipment> entry : body.entrySet()) {
            Equipment e = entry.getValue();
            if (e.getModifier().getAttribute() == Attribute.ATTACKDICE && e.isSingleUse())
                body.remove(entry.getKey());
        }
        target.takeDamage(attackDamage - target.defend());
    }

    public int defend() {
        int defenseValue = Dice.throwCombatDice(this.getDefendDice(), defendDiceValue);
        for(Map.Entry<String, Equipment> entry : body.entrySet()) {
            Equipment e = entry.getValue();
            if (e.getModifier().getAttribute() == Attribute.DEFENDDICE && e.isSingleUse())
                body.remove(entry.getKey());
        }
        return defenseValue;
    }

    @Override
    public boolean canBeOverlapped() {
        return false;
    }

    @Override
    public String getStringRepresentation() {
        return this.stringRepresentation;
    }
}