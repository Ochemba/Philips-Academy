package com.se.philips;

import com.se.philips.TEACHER.LoginSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import com.se.philips.TEACHER.TeacherDashboardController;

import java.io.IOException;
import java.sql.*;

public class TeacherLoginController {

    @FXML private TextField IDField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private Button loginButton;
    @FXML private Label BackLabel;

    @FXML
    void Back(MouseEvent event) throws IOException {
        Stage stage = (Stage) BackLabel.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/com/se/philips/scenes/welcome.fxml"));
        stage.setTitle("Welcome");
        stage.setScene(new Scene(root));
    }

    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        String id = IDField.getText();
        String password = passwordField.getText();

        if (verifyTeacherLogin(id, password)) {
            // ✅ Save ID globally in session
            LoginSession.setTeacherId(id);

            // ✅ Load dashboard with teacherId passed
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/se/philips/TEACHER/TeacherDashboard.fxml"));
            Parent root = loader.load();

            TeacherDashboardController controller = loader.getController();
            controller.setTeacherId(id);  // Pass ID into dashboard

            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Teacher Dashboard");
            stage.show();
        } else {
            showAlert("Login failed", "Invalid staff ID or password.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private boolean verifyTeacherLogin(String id, String password) {
        String url = "jdbc:sqlite:MainDB.db";
        String query = "SELECT * FROM teachers WHERE staff_id = ? AND password = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, id);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // ✅ Store ID in session
                String teacherId = rs.getString("staff_id"); // or "teacher_id" if that's the column
                LoginSession.setTeacherId(teacherId);

                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
