package com.downkombat.animation;

import javafx.scene.image.Image;

import java.util.*;

public class AnimationLoader {

    private static final String BASE_PATH = "/sprites/fighters/";

    public static Map<AnimationState, List<Image>> load(String fighter) {

        Map<AnimationState, List<Image>> animations = new EnumMap<>(AnimationState.class);

        loadState(animations, fighter, AnimationState.IDLE, "idle", 4);
        loadState(animations, fighter, AnimationState.WALK, "walk", 3);
        loadState(animations, fighter, AnimationState.ATTACK, "attack", 4);
        loadState(animations, fighter, AnimationState.HIT, "hit", 2);

        return animations;
    }

    private static void loadState(
            Map<AnimationState, List<Image>> animations,
            String fighter,
            AnimationState state,
            String folder,
            int frames
    ) {

        List<Image> images = new ArrayList<>();

        for (int i = 1; i <= frames; i++) {

            String path = BASE_PATH + fighter + "/" + folder + "_" + i + ".PNG";

            Image img = new Image(
                    AnimationLoader.class.getResource(path).toExternalForm()
            );

            images.add(img);
        }

        animations.put(state, images);
    }
}
