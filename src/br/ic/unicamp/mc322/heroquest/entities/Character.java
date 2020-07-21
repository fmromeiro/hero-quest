package br.ic.unicamp.mc322.heroquest.entities;

import br.ic.unicamp.mc322.heroquest.auxiliars.Point;

import java.util.HashMap;

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

    private Point position;

    private final String stringRepresentation;
    private final boolean isHero;

    private Character(String name, int attackDice, int defendDice, int baseBodyPoints, int mindPoints, String stringRepresentation, boolean isHero) {
        this.name = name;
        this.attackDice = attackDice;
        this.defendDice = defendDice;
        this.baseBodyPoints = baseBodyPoints;
        this.mindPoints = mindPoints;
        this.currentBodyPoints = baseBodyPoints;
        this.statusModifiers = new HashMap<>();
        this.statusModifierIndex = 0;
        this.stringRepresentation = stringRepresentation;
        this.isHero = isHero;
    }

    public static Character getDefaultHero(String name) {
        return new Character(name, 2, 2, 10, 5, "ME", true);
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
        // TODO: esse método pode buscar por modificadores de equipamentos e somá-los antes de retornar
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
                .mapToInt(x -> x.getModifier())
                .sum();
    }

    public boolean isHero() { return this.isHero; }

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
}