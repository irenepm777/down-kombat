package com.downkombat.game;

import com.downkombat.fighters.Fighter;

public class FightManager {

    public static void handleAttack(Fighter attacker, Fighter defender) {
        attacker.performAttack(defender);
    }

    public static void handleSpecial(Fighter attacker, Fighter defender) {
        attacker.performSpecial(defender);
    }
}