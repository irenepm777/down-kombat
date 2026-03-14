package com.downkombat.combat;

import com.downkombat.fighters.Fighter;

public interface Attack {

    void execute(Fighter attacker, Fighter defender);

}
