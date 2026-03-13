package com.downkombat.ui;

import javafx.scene.text.Font;

public class FontLoader {

    public static void loadPixelFont() {
        Font.loadFont(
            FontLoader.class.getResourceAsStream("/fonts/PressStart2P.ttf"),
            32
        );
    }
}
