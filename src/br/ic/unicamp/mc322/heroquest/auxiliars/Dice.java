package br.ic.unicamp.mc322.heroquest.auxiliars;

import java.util.Random;

public class Dice {
    public static final Random dice = new Random();
    public enum CombatDiceValue {
        SKULL,
        HERO_SHIELD,
        MONSTER_SHIELD,
    }
    public static int throwCombatDice(int qtt, CombatDiceValue desired) {
        int ret = 0;
        for(int i = 0; i < qtt; i++) {
            int d = dice.nextInt(6);
            CombatDiceValue result;
            if(d <= 2)
                result = CombatDiceValue.SKULL;
            else if(d < 5)
                result = CombatDiceValue.HERO_SHIELD;
            else
                result = CombatDiceValue.MONSTER_SHIELD;
            ret += result == desired ? 1 : 0;
        }
        return ret;
    }

    public static int throwMovementDice(int qtt) {
        return dice.ints(1, 7).limit(qtt).sum();
    }
}