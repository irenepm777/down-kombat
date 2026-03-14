package com.downkombat.combat.projectiles;

import com.downkombat.fighters.Fighter;
import javafx.scene.Group;

import java.util.ArrayList;
import java.util.List;

public class ProjectileManager {

    private List<Projectile> projectiles = new ArrayList<>();
    private Group root;

    public ProjectileManager(Group root) {
        this.root = root;
    }

    public void spawn(Projectile p) {

        projectiles.add(p);
        root.getChildren().add(p.getNode());
    }

    public void update(Fighter enemy) {

        for (Projectile p : projectiles) {

            p.update(enemy);
        }

        projectiles.removeIf(p -> {

            if (!p.isActive()) {
                root.getChildren().remove(p.getNode());
                return true;
            }

            return false;
        });
    }
}