package br.ic.unicamp.mc322.heroquest.auxiliars;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Dice {
    public static final Random dice = new Random();
    public enum CombatDiceValue {
        SKULL,
        HERO_SHIELD,
        MONSTER_SHIELD,
    }

    public static int rollCombatDice(int qtt, CombatDiceValue desired) {
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
    public static List<Integer> rollNumberDice(int qtt, int sides) {
        return dice.ints(qtt, 1, sides + 1).boxed().collect(Collectors.toList());
    }

    public static int rollNumberDiceSum(int qtt, int sides) {
        return dice.ints(qtt, 1, sides + 1).sum();
    }
    
    public static int rollMovementDice(int qtt) {
        return rollNumberDiceSum(qtt, 6);
    }
}
