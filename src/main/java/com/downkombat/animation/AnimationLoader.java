package com.downkombat.animation;

import javafx.scene.image.Image;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnimationLoader {

    public static Map<AnimationState, List<Image>> load(String basePath) {

        Map<AnimationState, List<Image>> animations = new HashMap<>();

        loadAnimation(animations, AnimationState.IDLE, basePath + "/idle");
        loadAnimation(animations, AnimationState.WALK, basePath + "/walk");
        loadAnimation(animations, AnimationState.ATTACK, basePath + "/attack");
        loadAnimation(animations, AnimationState.HIT, basePath + "/hit");

        return animations;
    }

    private static void loadAnimation(
            Map<AnimationState, List<Image>> map,
            AnimationState state,
            String folderPath
    ) {
        try {
            URL url = AnimationLoader.class.getResource(folderPath);
            if (url == null) return;

            File folder = new File(url.toURI());

            File[] files = folder.listFiles((dir, name) ->
                    name.toLowerCase().endsWith(".png")
            );

            if (files == null || files.length == 0) return;

            Arrays.sort(files, (a, b) ->
                    a.getName().compareToIgnoreCase(b.getName())
            );

            List<Image> frames = new ArrayList<>();

            for (File file : files) {
                frames.add(new Image(file.toURI().toString()));
            }

            if (!frames.isEmpty()) {
                map.put(state, frames);
            }

        } catch (Exception e) {
            System.out.println("Failed loading animation: " + folderPath);
        }
    }

    public static List<Image> loadSpecial(String folderPath) {

        List<Image> frames = new ArrayList<>();

        try {
            URL url = AnimationLoader.class.getResource(folderPath);
            if (url == null) return frames;

            File folder = new File(url.toURI());

            File[] files = folder.listFiles((dir, name) ->
                    name.toLowerCase().matches("^special_\\d+\\.png$")
            );

            if (files == null || files.length == 0) return frames;

            Arrays.sort(files, (a, b) ->
                    a.getName().compareToIgnoreCase(b.getName())
            );

            for (File file : files) {
                frames.add(new Image(file.toURI().toString()));
            }

        } catch (Exception e) {
            System.out.println("Failed loading special animation: " + folderPath);
        }

        return frames;
    }
}