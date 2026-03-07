package com.downkombat.game;

import com.downkombat.fighters.Fighter;

public class FightManager {

    private Fighter fighter1;
    private Fighter fighter2;

    public FightManager(Fighter fighter1, Fighter fighter2) {

        this.fighter1 = fighter1;
        this.fighter2 = fighter2;
    }

    public boolean tryAttack(Fighter attacker, Fighter defender) {

        if (!attacker.canAttack()) return false;
        if (!attacker.isNear(defender)) return false;
        if (!attacker.isFacing(defender)) return false;

        attacker.performAttack(defender);

        return true;
    }

    public boolean trySpecial(Fighter attacker, Fighter defender) {

        if (!attacker.canSpecial()) return false;
        if (!attacker.isNear(defender)) return false;

        attacker.performSpecial(defender);

        return true;
    }
}
