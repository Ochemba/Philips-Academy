package com.se.philips;//package com.se.demo;
//
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Node;
//import javafx.scene.Scene;
//import javafx.scene.control.Label;
//import javafx.stage.Stage;
//
//import java.io.IOException;
//
//public class WelcomeController {
//
//    @FXML
//    private Label welcome;
//    private Stage stage;
//    private Scene scene;
//    private FXMLLoader fxmlLoader;
//
//
//    public void Students(ActionEvent event) throws IOException {
//        FXMLLoader fxmlLoader = FXMLLoader.load(getClass().getResource("AdminDashboard.fxml"));
//        stage =(Stage) ((Node)event.getSource()).getScene().getWindow();
//        scene = new Scene(fxmlLoader.load());
//        stage.setScene(scene);
//        stage.show();
//    }
//
//    public void Teacher(ActionEvent event) throws IOException {
//        FXMLLoader fxmlLoader = FXMLLoader.load(getClass().getResource("welcome.fxml"));
//        stage =(Stage) ((Node)event.getSource()).getScene().getWindow();
//        scene = new Scene(fxmlLoader.load());
//        stage.setScene(scene);
//        stage.show();
//    }
//
//    public void Admin(ActionEvent event) throws IOException {
//        FXMLLoader fxmlLoader = FXMLLoader.load(getClass().getResource("AdminDashboard.fxml"));
//        stage =(Stage) ((Node)event.getSource()).getScene().getWindow();
//        scene = new Scene(fxmlLoader.load());
//        stage.setScene(scene);
//        stage.show();
//    }
//}

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomeController2 {

    @FXML
    private Button StudentButton;

    @FXML
    private Button TeacherButton;

    @FXML
    private Button AdminButton;

    @FXML
    void Student(MouseEvent event) throws IOException {
        Stage stage = (Stage) StudentButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("StudentLogin.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Student Login");
        stage.setScene(scene);
    }

    @FXML
    void Teacher(MouseEvent event) throws IOException {
        Stage stage = (Stage) TeacherButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("TeacherLogin.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Teacher Login");
        stage.setScene(scene);
    }

    @FXML
    void Admin(MouseEvent event) throws IOException {
        Stage stage = (Stage) AdminButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("AdminLogin.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Admin Login");
        stage.setScene(scene);
    }
}

