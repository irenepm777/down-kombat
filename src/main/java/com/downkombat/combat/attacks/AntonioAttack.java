package com.downkombat.combat;

import com.downkombat.config.GameConfig;
import com.downkombat.game.Player;

public class AntonioAttack implements SpecialAttack {

    private boolean transformed = false;
    private long transformEndTime = 0;

    private static final int TRANSFORM_DURATION = 15000;
    private static final int HYDE_DAMAGE = 12;

    @Override
    public void execute(Player attacker, Player defender) {

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

            System.out.println("MR HYDE se desvanece");

        }

    }
}
