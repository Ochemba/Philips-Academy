package com.se.philips.TEACHER;

import com.se.philips.AdminDashboard.LogOutDialogController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TeacherDashboardController implements Initializable {

    @FXML private StackPane content;
    @FXML private BorderPane Border;
    @FXML private Button logoutButton;
    @FXML private Label welcomeLabel;

    private String teacherId;

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
        if (welcomeLabel != null) {
            welcomeLabel.setText("Welcome, " + teacherId);
        }
    }

    public String getTeacherId() {
        return teacherId;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Fallback: Get teacherId from LoginSession if not already set
        if (this.teacherId == null) {
            this.teacherId = LoginSession.getTeacherId();

        }

        // Show welcome label if possible
        if (welcomeLabel != null && teacherId != null) {
            welcomeLabel.setText("Welcome, " + teacherId);
        }

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/se/philips/TEACHER/Dashboard.fxml"));
            content.getChildren().setAll(root);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
    }


    // Other views
    public void Dash(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/se/philips/TEACHER/Dashboard.fxml"));
        content.getChildren().setAll(root);
    }

    public void Students(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/se/philips/dashb/StudentCreate.fxml"));
        content.getChildren().setAll(root);
    }

    public void Teachers(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/se/philips/dashb/TeacherCreate.fxml"));
        content.getChildren().setAll(root);
    }


    @FXML
    public void Course(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/se/philips/TEACHER/CourseTeacher.fxml"));
        Parent root = loader.load();

        TeacherCourseController controller = loader.getController();
        controller.setTeacherId(this.teacherId); // ensure teacherId is passed

        content.getChildren().setAll(root);
    }

    public void Calendar(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/se/philips/TEACHER/CalenderDashboard.fxml"));
        content.getChildren().setAll(root);
    }

    public void Finance(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/se/philips/TEACHER/FinanceDashboard.fxml"));
        content.getChildren().setAll(root);
    }

    public void Notice(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/se/philips/TEACHER/NoticeDashboard.fxml"));
        content.getChildren().setAll(root);
    }

    public void Message(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/se/philips/TEACHER/TeacherMessageDashboard.fxml"));
        content.getChildren().setAll(root);
    }

    public void Profile(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/se/philips/TEACHER/ProfileDashboard.fxml"));
        content.getChildren().setAll(root);
    }

    @FXML
    private void LogOut() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/se/philips/TEACHER/LogOutDialog.fxml"));
        Parent dialogRoot = loader.load();

        LogOutDialogController controller = loader.getController();
        controller.setMainStage((Stage) logoutButton.getScene().getWindow());

        Stage dialogStage = new Stage();
        dialogStage.initOwner(logoutButton.getScene().getWindow());
        dialogStage.initStyle(javafx.stage.StageStyle.TRANSPARENT);

        Scene scene = new Scene(dialogRoot);
        scene.setFill(null);
        dialogStage.setScene(scene);
        dialogStage.show();
    }
}
