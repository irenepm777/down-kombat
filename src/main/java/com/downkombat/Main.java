package com.downkombat;

import com.downkombat.fighters.CharacterType;
import com.downkombat.game.GameScene;
import com.downkombat.ui.select.CharacterSelectController;
import com.downkombat.ui.select.MapsSelectController;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private Stage stage;

    private String player1;
    private String player2;

    @Override
    public void start(Stage stage) {

        this.stage = stage;

        // First screen: Player 1 selection
        CharacterSelectController controller = new CharacterSelectController(this, true);

        stage.setTitle("DOWN KOMBAT - Select Player 1");
        stage.setScene(controller.getScene());
        stage.show();
    }

    // Called when Player 1 is selected
    public void showSelectP2(String p1) {
        this.player1 = p1;

        CharacterSelectController controller = new CharacterSelectController(this, false);

        stage.setTitle("DOWN KOMBAT - Select Player 2");
        stage.setScene(controller.getScene());
    }

    // Called when Player 2 is selected
    public void showSelectMap(String p2) {
        this.player2 = p2;

        MapsSelectController controller = new MapsSelectController(this, true);

        stage.setTitle("DOWN KOMBAT - Select Map");
        stage.setScene(controller.getScene());
    }

    // Called when map is selected
    public void startGame(String map) {

        GameScene game = new GameScene(
            CharacterType.valueOf(player1.toUpperCase()),
            CharacterType.valueOf(player2.toUpperCase()),
            map
        );

        stage.setScene(game.getScene());
    }

    public static void main(String[] args) {
        launch();
    }
}