package com.downkombat.combat.attacks;

import com.downkombat.combat.SpecialAttack;
import com.downkombat.fighters.Fighter;
import javafx.scene.paint.Color;

public class NepalShieldAttack implements SpecialAttack {

    private boolean active = false;
    private long endTime = 0;

    private static final int DURATION = 6000;

    @Override
    public void execute(Fighter attacker, Fighter defender) {

        if (active) return;

        active = true;

        endTime = System.currentTimeMillis() + DURATION;

        attacker.setColor(Color.GOLD);

        attacker.setInvulnerable(true);

        System.out.println("PEPE ACTIVA NEPAL SHIELD");
    }

    @Override
    public void update(Fighter attacker) {

        if (!active) return;

        if (System.currentTimeMillis() > endTime) {

            attacker.setInvulnerable(false);

            attacker.resetColor();

            active = false;

            System.out.println("NEPAL SHIELD TERMINA");
        }
    }
}
