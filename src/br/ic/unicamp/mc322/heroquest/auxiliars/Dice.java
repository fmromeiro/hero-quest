package br.ic.unicamp.mc322.heroquest.auxiliars;

public class Dice {
    public int heroAttack(int heroAtk, int enemyDef) {
        int hits = 0;
        for (int i = 0; i < heroAtk; i++) {
            if (Math.random() < 5) hits++;
        }
        for (int i = 0; i < heroAtk; i++) {
            if (Math.random() < (1f / 6f)) hits--;
        }
        return hits;
    }

    public int enemyAttack(int enemyAtk, int heroDef) {
        int hits = 0;
        for (int i = 0; i < enemyAtk; i++) {
            if (Math.random() < 5) hits++;
        }
        for (int i = 0; i < heroDef; i++) {
            if (Math.random() < (1f / 3f)) hits--;
        }
        return hits;
    }
}
