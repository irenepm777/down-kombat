package com.downkombat.combat;

import com.downkombat.fighters.Fighter;

public interface SpecialAttack {

    void execute(Fighter attacker, Fighter defender);
    void update(Fighter attacker);

}
