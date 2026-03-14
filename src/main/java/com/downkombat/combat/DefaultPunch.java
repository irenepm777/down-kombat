package com.downkombat.combat;

import com.downkombat.config.GameConfig;
import com.downkombat.fighters.Fighter;

public class DefaultPunch implements SpecialAttack {

    @Override
    public void execute(Fighter attacker, Fighter defender) {

        // aplicar daño directamente
        defender.damage(GameConfig.ATTACK_DAMAGE);

    }

    @Override
    public void update(Fighter attacker) {
        // este ataque no necesita lógica extra
    }
}