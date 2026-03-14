package com.downkombat.combat.attacks;

import com.downkombat.combat.SpecialAttack;
import com.downkombat.fighters.Fighter;

public class NepalShieldAttack implements SpecialAttack {

    private boolean active = false;
    private long endTime = 0;

    private static final int DURATION = 6000;

    @Override
    public void execute(Fighter attacker, Fighter defender) {

        if (active) return;

        active = true;

        endTime = System.currentTimeMillis() + DURATION;

        System.out.println("PEPE ACTIVA NEPAL SHIELD");
    }

    @Override
    public void update(Fighter attacker) {

        if (!active) return;

        if (System.currentTimeMillis() > endTime) {

            active = false;

            System.out.println("NEPAL SHIELD TERMINA");
        }
    }
}