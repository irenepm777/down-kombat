package com.downkombat.animation;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnimationLoader {

    public static Map<AnimationState, List<Image>> load(String basePath) {

        Map<AnimationState, List<Image>> animations = new HashMap<>();

        loadAnimation(animations, AnimationState.IDLE, basePath + "/idle", "idle");
        loadAnimation(animations, AnimationState.WALK, basePath + "/walk", "walk");
        loadAnimation(animations, AnimationState.ATTACK, basePath + "/attack", "attack");
        loadAnimation(animations, AnimationState.HIT, basePath + "/hit", "hit");

        return animations;
    }

    private static void loadAnimation(
            Map<AnimationState, List<Image>> map,
            AnimationState state,
            String folder,
            String prefix
    ) {

        List<Image> frames = new ArrayList<>();

        for (int i = 1; i <= 20; i++) {

            String path = folder + "/" + prefix + "_" + i + ".png";
            var url = AnimationLoader.class.getResource(path);

            if (url == null) {
                break;
            }

            frames.add(new Image(url.toExternalForm()));
        }

        if (!frames.isEmpty()) {
            map.put(state, frames);
        }
    }
}