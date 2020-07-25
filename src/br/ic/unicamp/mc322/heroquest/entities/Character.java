package br.ic.unicamp.mc322.heroquest.entities;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ic.unicamp.mc322.heroquest.auxiliars.Dice;
import br.ic.unicamp.mc322.heroquest.auxiliars.Point;
import br.ic.unicamp.mc322.heroquest.items.Consumable;
import br.ic.unicamp.mc322.heroquest.items.Equipment;
import br.ic.unicamp.mc322.heroquest.items.Item;
import br.ic.unicamp.mc322.heroquest.items.Weapon;

public class Character implements Entity {

    // Class attributes
    public static enum Attribute {
        ATTACKDICE("Attack Dice"),
        DEFENDDICE("Defend Dice"),
        BASEBODYPOINTS("Base Body Points"),
        MINDPOINTS("Mind Points");

        private final String name;

        Attribute(String name) { this.name = name; }

        public String getName() { return this.name; }
    }

    private static final int RIGHT_HAND = 0;
    private static final int LEFT_HAND = 1;


    // Instance attributes
    private final HashMap<Integer, StatusModifier> statusModifiers; // TODO: aparentemente não vai ter uso - rever e remover
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


    // Entity methods
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

    @Override
    public boolean canBeOverlapped() {
        return false;
    }

    @Override
    public String getStringRepresentation() {
        return this.stringRepresentation;
    }


    // Character builders
    public static Character getDefaultHero(String name) {
        return new Character(name, 2, 2, 10, 5, "ME", Dice.CombatDiceValue.HERO_SHIELD);
    }

    public static Enemy getMeleeSkeleton(String name) {
        Enemy skeleton = new Enemy(name, 2, 2, 1, 0, "SK", 6, EnemyFunctions.moveRandomly, null);
        Weapon weapon;
        switch ((int)Math.floor(Math.random()*4)) {
            default:
            case 0: weapon = Weapon.getShortSword(); break;
            case 1: weapon = Weapon.getLongSword(); break;
            case 2: weapon = Weapon.getBrokenDagger(); break;
            case 3: weapon = Weapon.getLongStaff(); break;
        }
        skeleton.addToInventory(weapon);
        skeleton.equip(weapon);
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


    // Accessors
    // Hit Points accessors
    public void takeDamage(int damage) {
        this.currentBodyPoints = Math.max(this.currentBodyPoints - damage, 0);
    }

    public void healDamage(int heal) {
        this.currentBodyPoints = Math.min(this.currentBodyPoints + heal, this.baseBodyPoints);
    }

    public boolean isAlive() { return this.currentBodyPoints > 0; }

    // Attributes accessors
    public int getAttribute(Attribute attribute) {
        switch (attribute) {
            case ATTACKDICE: return this.attackDice + this.getModifiersFor(attribute, false);
            case DEFENDDICE: return this.defendDice + this.getModifiersFor(attribute, true);
            case BASEBODYPOINTS: return this.baseBodyPoints + this.getModifiersFor(attribute, true);
            case MINDPOINTS: return this.mindPoints + this.getModifiersFor(attribute, true);
            default: return 0;
        }
    }

    public int addModifier(StatusModifier modifier) {
        statusModifiers.put(this.statusModifierIndex, new StatusModifier(modifier.getAttribute(), modifier.getModifier()));
        this.statusModifierIndex++;
        return this.statusModifierIndex - 1;
    }

    public void removeModifier(int index) {
        statusModifiers.remove(index);
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

    // Miscellaneous accessors
    public int getSteps() {
        return Dice.throwMovementDice(2);
    }

    public boolean isHero() { return this.defendDiceValue == Dice.CombatDiceValue.HERO_SHIELD; }

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

    public int getAttackDamage() {
        return attackDice + getModifiersFor(Attribute.ATTACKDICE, false);
    }

    public int getDefendDice() {
        return defendDice + getModifiersFor(Attribute.DEFENDDICE, true);
    }

    public void chooseWeapon(int hand) {
        this.selectedWeapon = hand;
    }

    public void addToInventory(Item item) {
        inventory.addItem(equipment);
    }


    // Actions
    public void attack(Character target) throws Exception {
        List<Point> line = Arrays.asList(Point.bresenhamLine(this.getPosition(), target.getPosition()));
        Tile[][] map = Dungeon.getInstance().getMap();
        if (line.size() > 2 && line.subList(1, line.size()).stream().anyMatch(point -> {
                    Entity current = map[point.getY()][point.getX()].getEntity();
                    return current != null && !current.canSeeThrough();
                }))
            throw new Exception("Can't see target");

        int distance = Point.manhattanDistance(this.getPosition(), target.getPosition());
        boolean isWeapon = hands[selectedWeapon] != null && hands[selectedWeapon] instanceof Weapon;
        int weaponDamage = isWeapon ? hands[selectedWeapon].getModifier().getModifier() : 0;
        int weaponRange = isWeapon ? ((Weapon)hands[selectedWeapon]).getRange() : 1;

        if(weaponRange < distance)
            throw new Exception("Out of range target");

        int attackDamage = Dice.rollCombatDice(this.getAttackDamage() + weaponDamage, Dice.CombatDiceValue.SKULL);
        for(Map.Entry<String, Equipment> entry : body.entrySet()) {
            Equipment e = entry.getValue();
            if (e.getModifier().getAttribute() == Attribute.ATTACKDICE && e.isSingleUse())
                body.remove(entry.getKey());
        }
        if(isWeapon && hands[selectedWeapon].isSingleUse())
            hands[selectedWeapon] = null;

        target.takeDamage(attackDamage - target.defend());
    }

    public int defend() {
        int defenseValue = Dice.rollCombatDice(this.getDefendDice(), defendDiceValue);
        for(Map.Entry<String, Equipment> entry : body.entrySet()) {
            Equipment e = entry.getValue();
            if (e.getModifier().getAttribute() == Attribute.DEFENDDICE && e.isSingleUse())
                body.remove(entry.getKey());
        }
        for(int i = 0; i < hands.length; i++)
            if(hands[i].getModifier().getAttribute() == Attribute.DEFENDDICE)
                hands[i] = null;
        return defenseValue;
    }
}