package com.downkombat.combat;

import com.downkombat.config.GameConfig;
import com.downkombat.fighters.Fighter;

public class DefaultPunch implements SpecialAttack {

    @Override
    public void execute(Fighter attacker, Fighter defender) {

        defender.damage(GameConfig.ATTACK_DAMAGE);
        defender.applyKnockback(attacker, GameConfig.KNOCKBACK_FORCE);
    }

    @Override
    public void update(Fighter attacker) {
        // no tiene estado temporal
    }
}