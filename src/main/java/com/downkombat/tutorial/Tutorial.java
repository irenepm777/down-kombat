package com.downkombat.tutorial;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Tutorial {

    public void mostrar(Stage stageAnterior) {
        // Cargar imagen (asegúrate de que la ruta exista)
        Image imagen = new Image(getClass().getResource("/images/tutorialr.png").toString());
        ImageView imageView = new ImageView(imagen);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        // Botón principal (texto Unicode flecha)
        Button btnMain = new Button("←");
        btnMain.getStyleClass().add("btn-volver-pixel-main");

        // Botón sombra (para efecto "pixel" sin imágenes)
        Button btnShadow = new Button("←");
        btnShadow.getStyleClass().add("btn-volver-pixel-shadow");
        // Deshabilitar interacción de la sombra para que solo el main reciba clicks
        btnShadow.setMouseTransparent(true);

        // Acción del botón principal
        btnMain.setOnAction(e -> {
            stageAnterior.show();
            Stage current = (Stage) btnMain.getScene().getWindow();
            current.close();
        });

        // Contenedor para la flecha (usamos StackPane para superponer shadow + main)
        StackPane botonPixelStack = new StackPane(btnShadow, btnMain);
        botonPixelStack.setAlignment(Pos.TOP_LEFT);
        botonPixelStack.setTranslateX(20);
        botonPixelStack.setTranslateY(20);

        // Root: imagen de fondo y encima el botón pixel
        StackPane root = new StackPane();
        root.getChildren().addAll(imageView, botonPixelStack);
        StackPane.setAlignment(botonPixelStack, Pos.TOP_LEFT);

        // Escena y binding para que la imagen se ajuste a la ventana
        Scene scene = new Scene(root, 1280, 720);
        imageView.fitWidthProperty().bind(scene.widthProperty());
        imageView.fitHeightProperty().bind(scene.heightProperty());

        // Añadir hoja de estilos (asegúrate de que styles.css esté en resources/css/)
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

        Stage stage = new Stage();
        stage.setTitle("Tutorial");
        stage.setScene(scene);
        stage.show();

        stageAnterior.hide();
    }
}