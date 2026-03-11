package com.downkombat;

import com.downkombat.fighters.CharacterType;
import com.downkombat.game.GameScene;
import com.downkombat.ui.select.CharacterSelectController;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private Stage stage;
    private String player1;

    @Override
    public void start(Stage stage) {

        this.stage = stage;

        // Primera pantalla: selección de Jugador 1
        CharacterSelectController controller = new CharacterSelectController(this, true);

        stage.setTitle("DOWN KOMBAT - Selecciona Jugador 1");
        stage.setScene(controller.getScene());
        stage.show();
    }

    // Llamado cuando se selecciona el jugador 1
    public void showSelectP2() {

        CharacterSelectController controller = new CharacterSelectController(this, false);

        stage.setTitle("DOWN KOMBAT - Selecciona Jugador 2");
        stage.setScene(controller.getScene());
    }

    // Llamado cuando se selecciona el jugador 2
    public void startGame(String player1, String player2) {

        GameScene game = new GameScene(
            CharacterType.valueOf(player1.toUpperCase()),
            CharacterType.valueOf(player2.toUpperCase())
        );

        stage.setScene(game.getScene());
    }

    public static void main(String[] args) {
        launch();
    }

	public void startGame(CharacterType characterType, CharacterType characterType2) {
		// TODO Auto-generated method stub
		
	}
}
