package com.se.philips.Admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminLoginController {
    @FXML
    private Label welcomeText;


    @FXML
    private TextField AdminName;

    @FXML
    private PasswordField password;

    @FXML
    private Label loginMessageLabel;

    @FXML
    private Label BackLabel;

    @FXML
    private Button loginButton;

    @FXML
    void Back(MouseEvent event) throws IOException {
        Stage stage = (Stage) BackLabel.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/com/se/philips/scenes/welcome.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Welcome");
        stage.setScene(scene);
    }


    @FXML
    void loginButtonOnAction(ActionEvent event) throws IOException {

        if((AdminName.getText().contains("uju") && password.getText().contains("123")) ) {
            loginMessageLabel.setText("Login Successful!");
            Stage stage = (Stage) loginButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/com/se/philips/dashb/AdminDashboard.fxml"));
            Scene scene = new Scene(root);
            stage.setTitle("DashBoard");
            stage.setScene(scene);
        }else if (!AdminName.getText().contains("uju") && !password.getText().contains("123")){
            loginMessageLabel.setText("Login Failed!");
        }else {
            loginMessageLabel.setText("Enter a Valid Login!");
        }
    }

}