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
import br.ic.unicamp.mc322.heroquest.spells.Spell;

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

    public static final int RIGHT_HAND = 0;
    public static final int LEFT_HAND = 1;


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
    private final SpellBook spellBook;


    protected Character(String name, int attackDice, int defendDice, int baseBodyPoints, int mindPoints, String stringRepresentation, Dice.CombatDiceValue defendDiceValue, boolean isCaster) {
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
        this.spellBook = isCaster ? new SpellBook(this) : null;
    }


    // Entity methods
    @Override
    public Point getPosition() {
        return this.position;
    }

    public boolean isCaster() { return this.spellBook != null; }

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
        return baseBodyPoints <= 0? "XX" : this.stringRepresentation;
    }


    // Character builders
    public static Character getBarbarian() {
        Character barbarian = new Character("The Barbarian", 3, 2, 8, 2, "BB", Dice.CombatDiceValue.HERO_SHIELD, false);
        barbarian.addToInventory(Weapon.getLongSword());
        return barbarian;
    }

    public static Character getDwarf() {
        Character dwarf = new Character("The Dwarf", 2, 2, 7, 3, "DW", Dice.CombatDiceValue.HERO_SHIELD, false);
        dwarf.addToInventory(Weapon.getShortSword());
        return dwarf;
    }

    public static Character getElf() {
        Character elf = new Character("The Elf", 2, 2, 6, 4, "EL", Dice.CombatDiceValue.HERO_SHIELD, true);
        elf.addToInventory(Weapon.getShortSword());
        elf.loadSpell(Spell.getSimpleHeal(1));
        return elf;
    }

    public static Character getSorcerer() {
        Character sorcerer = new Character("The Sorcerer", 1, 2, 4, 6, "SO", Dice.CombatDiceValue.HERO_SHIELD, true);
        for (int i = 0; i < 3; i++) sorcerer.addToInventory(Weapon.getDagger());
        sorcerer.loadSpell(Spell.getMagicMissile(3));
        sorcerer.loadSpell(Spell.getFireball(1));
        sorcerer.loadSpell(Spell.getTeleport(1));
        return sorcerer;
    }

    public static Enemy getMeleeSkeleton(char id) {
        Enemy skeleton = new Enemy("Skeleton " + id, 2, 2, 1, 0, "S" + id, 6, EnemyFunctions.moveRandomly, EnemyFunctions.favourCurrentWeaponAndThenDamage, false);
        Weapon weapon;
        switch ((int)(Math.random()*4)) {
            default:
            case 0: weapon = Weapon.getShortSword(); break;
            case 1: weapon = Weapon.getLongSword(); break;
            case 2: weapon = Weapon.getBrokenDagger(); break;
            case 3: weapon = Weapon.getLongStaff(); break;
        }
        skeleton.equip(weapon);
        return skeleton;
    }

    public static Enemy getSkeletonMage(char id) {
        Enemy skeletonMage = new Enemy("Skeleton Mage " + id, 2, 2, 1, 3, "M" + id, 6, EnemyFunctions.moveRandomly, EnemyFunctions.favourSpellThenWeaponDamage, true);
        skeletonMage.loadSpell(Spell.getMagicMissile(10));
        return skeletonMage;
    }

    public static Enemy getGoblin(char id) {
        Enemy goblin = new Enemy("Goblin " + id, 2, 1, 1, 1, "G" + id, 10, EnemyFunctions.followHero, EnemyFunctions.favourCurrentWeaponAndThenDamage, false);
        for (int i = 0; i < 5; i++) goblin.addToInventory(Weapon.getDagger());
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
                .filter(x -> x != null && x.getModifier().getAttribute() == attribute)
                .mapToInt(x -> x.getModifier().getModifier())
                .sum();
    }

    public int getSteps() {
        return Dice.rollMovementDice(2);
    }

    public boolean isHero() { return this.defendDiceValue == Dice.CombatDiceValue.HERO_SHIELD; }

    public int getAttackDamage() {
        return attackDice + getModifiersFor(Attribute.ATTACKDICE, false);
    }

    public int getDefendDice() {
        return defendDice + getModifiersFor(Attribute.DEFENDDICE, true);
    }

    // Equipment accessors
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

    public void chooseWeapon(int hand) {
        this.selectedWeapon = hand;
    }

    public void addToInventory(Item item) {
        inventory.addItem(item);
    }

    public Equipment getCurrentWeapon() { return hands[selectedWeapon]; }

    public Map<Integer, Item> getInventory() {
        return this.inventory.getItems();
    }

    public Map<String, Equipment> getCurrentlyEquipped() {
        HashMap<String, Equipment> result = new HashMap<>();
        for (Equipment.Category category : Equipment.Category.values()) {
            if (category == Equipment.Category.ONEHAND || category == Equipment.Category.TWOHAND) {
                result.put("RIGHT HAND", hands[RIGHT_HAND]);
                result.put("LEFT HAND", hands[LEFT_HAND]);
            }
            else
                result.put(category.getCategoryName(), body.getOrDefault(category.getCategoryName(), null));
        }
        return result;
    }

    public Item dropItem(int id) {
        return inventory.drop(id);
    }

    // Spell acessors
    public void loadSpell(Spell spell) {
        this.spellBook.addItem(spell);
    }

    public Map<Integer, Spell> getSpellBook() { return this.spellBook.getSpells(); }


    // Actions
    public void attack(Character target) throws Exception {
        if (!Dungeon.getInstance().hasVisibility(this.getPosition(), target.getPosition()))
            throw new Exception("Can't see target");

        int distance = Point.manhattanDistance(this.getPosition(), target.getPosition());
        boolean isWeapon = hands[selectedWeapon] instanceof Weapon;
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
            if(hands[i] != null && hands[i].getModifier().getAttribute() == Attribute.DEFENDDICE && hands[i].isSingleUse())
                hands[i] = null;
        return defenseValue;
    }

    public void collect(Treasure loot) {
        inventory.addItem(loot.getLoot());
    }
    
    public void castSpell(Spell spell, Point target) throws Exception {
        this.spellBook.castSpell(spell, target);
    }
}