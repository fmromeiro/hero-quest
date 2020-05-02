package br.ic.unicamp.mc322.heroquest.entities;

import java.util.LinkedList;

public abstract class Character extends Entity {
    private final String name;
    private int attackDice;
    private int defendDice;

    private int baseBodyPoints;
    private int mindPoints;
    private int currentBodyPoints;

    public Character(String name, int attackDice, int defendDice, int baseBodyPoints, int mindPoints, int x, int y) {
        super(x, y);
        this.name = name;
        this.attackDice = attackDice;
        this.defendDice = defendDice;
        this.baseBodyPoints = baseBodyPoints;
        this.mindPoints = mindPoints;
    }

    public void takeDamage(int damage) {
        this.currentBodyPoints = Math.max(this.currentBodyPoints - damage, 0);
    }

    public void healDamage(int heal) {
        this.currentBodyPoints = Math.min(this.currentBodyPoints + heal, this.baseBodyPoints);
    }

    public boolean isAlive() { return this.currentBodyPoints > 0; }

    public int getAttackDice() {
        return attackDice;
    }

    public void setAttackDice(int attackDice) {
        this.attackDice = attackDice;
    }

    public int getDefendDice() {
        return defendDice;
    }

    public void setDefendDice(int defendDice) {
        this.defendDice = defendDice;
    }

    public int getBaseBodyPoints() {
        return baseBodyPoints;
    }

    public void setBaseBodyPoints(int baseBodyPoints) {
        this.baseBodyPoints = baseBodyPoints;
    }

    public int getMindPoints() {
        return mindPoints;
    }

    public void setMindPoints(int mindPoints) {
        this.mindPoints = mindPoints;
    }

    public int getCurrentBodyPoints() {
        return currentBodyPoints;
    }

}