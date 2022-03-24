package com.vargha;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class Main extends Application {
    private static Controller c = new Controller();

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("editUI.fxml"));
        loader.setControllerFactory(t -> c);
        Parent root = loader.load();
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("NuEdit");
        primaryStage.setMinHeight(500);
        primaryStage.setMinWidth(650);
        primaryStage.getIcons().add(new Image("file:icon.png"));
        primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                event.consume();
                c.close();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
