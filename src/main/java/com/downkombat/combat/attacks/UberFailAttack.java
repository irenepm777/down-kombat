package com.downkombat.combat.attacks;

import com.downkombat.combat.SpecialAttack;
import com.downkombat.combat.projectiles.Car;
import com.downkombat.combat.projectiles.CarManager;
import com.downkombat.fighters.Fighter;

public class UberFailAttack implements SpecialAttack {

    private final CarManager carManager;

    public UberFailAttack(CarManager carManager) {
        this.carManager = carManager;
    }

    @Override
    public void execute(Fighter attacker, Fighter defender) {

        System.out.println("JUANMA LLAMA UN UBER");

        boolean direction = attacker.isFacingRight();

        double spawnX;

        if (direction) {
            spawnX = -200;
        } else {
            spawnX = 1600;
        }

        double spawnY = attacker.getNode().getTranslateY() - 400;

        Car car = new Car(
                spawnX,
                spawnY,
                direction,
                attacker
        );

        carManager.spawn(car);
    }

    @Override
    public void update(Fighter attacker) {
        // sin lógica continua
    }
}
