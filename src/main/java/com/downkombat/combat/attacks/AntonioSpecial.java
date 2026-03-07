package com.downkombat.combat.attacks;

import com.downkombat.combat.SpecialAttack;
import com.downkombat.config.GameConfig;
import com.downkombat.fighters.Fighter;

public class AntonioSpecial implements SpecialAttack {

    private boolean transformed = false;
    private long transformEndTime = 0;

    private static final int TRANSFORM_DURATION = 15000;
    private static final int HYDE_DAMAGE = 12;

    @Override
    public void execute(Fighter attacker, Fighter defender) {

        long now = System.currentTimeMillis();

        if (!transformed) {

            transformed = true;
            transformEndTime = now + TRANSFORM_DURATION;

            System.out.println("ANTONIO → MR HYDE!");
        }

        if (transformed) {

            defender.damage(HYDE_DAMAGE);
            defender.applyKnockback(attacker, GameConfig.KNOCKBACK_FORCE);
        }

        if (transformed && now > transformEndTime) {

            transformed = false;
            System.out.println("MR HYDE termina");
        }
    }
}
