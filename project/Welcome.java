package com.se.philips;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Welcome extends Application {

    private static HostServices hostServices; // ✅ Declare static field

    @Override
    public void start(Stage primaryStage) throws IOException {
        hostServices = getHostServices(); // ✅ Assign value here

        Parent root = FXMLLoader.load(getClass().getResource("scenes/welcome.fxml"));
        Scene scene = new Scene(root, 1500, 800);
        primaryStage.setTitle("Welcome");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    // ✅ Expose it to other classes
    public static HostServices getAppHostServices() {
        return hostServices;
    }

    public static void main(String[] args) {
        launch();
    }
}
