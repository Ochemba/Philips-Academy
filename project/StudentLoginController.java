package com.se.philips;


import com.se.philips.STUDENT.StudentLoginSession;
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
import java.sql.*;

public class StudentLoginController {
    @FXML
    private Label welcomeText;

    @FXML
    private Label BackLabel;

    @FXML
    void Back(MouseEvent event) throws IOException {
        Stage stage = (Stage) BackLabel.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/com/se/philips/scenes/welcome.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Welcome");
        stage.setScene(scene);
    }

//    @FXML
//    protected void onHelloButtonClick() {
//        welcomeText.setText("Welcome to JavaFX Application!");
//    }

    @FXML private TextField matricField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private Button loginButton;

    @FXML
    protected void onLoginClick() throws IOException {
        String matric = matricField.getText();
        String password = passwordField.getText();

        if (verifyStudentLogin(matric, password)) {
            errorLabel.setText("Welcome!");
            System.out.println("Login successful");

//            // Store session
//            StudentSession.setMatricId(matric);

            // Open dashboard
            Stage stage = (Stage) loginButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/com/se/philips/STUDENT/StudentDashboard.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle("Student Dashboard");
            stage.show();
        } else {
            errorLabel.setText("Invalid Matric ID or Password.");
        }
    }


    @FXML
    void StudentDash(MouseEvent event) throws IOException {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/com/se/philips/STUDENT/StudentDashboard.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Student Login");
        stage.setScene(scene);
    }

    private boolean verifyStudentLogin(String id, String password) {
        String url = "jdbc:sqlite:MainDB.db";
        String query = "SELECT * FROM students WHERE matricId = ? AND password = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, id);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Store student session info
                StudentLoginSession.setSession(
                        rs.getString("matricId"),
                        rs.getString("firstname"),
                        rs.getString("level"),
                        rs.getString("department")
                );
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }




}