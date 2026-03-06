package com.downkombat.combat;

import com.downkombat.config.GameConfig;
import com.downkombat.game.Player;

public class DefaultPunch implements SpecialAttack {

    @Override
    public void execute(Player attacker, Player defender) {

        defender.damage(GameConfig.ATTACK_DAMAGE);
        defender.applyKnockback(attacker, GameConfig.KNOCKBACK_FORCE);

    }
}
