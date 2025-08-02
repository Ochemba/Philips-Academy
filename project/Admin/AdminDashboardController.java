package com.se.philips.Admin;

import com.se.philips.AdminDashboard.LogOutDialogController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminDashboardController implements Initializable {

    @FXML private StackPane content;
    @FXML private BorderPane Border;
    @FXML private Button logoutButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/com/se/philips/dashb/Dashboard.fxml"));
            content.getChildren().removeAll();
            content.getChildren().setAll(root);
        }catch(Exception e){
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "null", e );
        }
    }

    public void Dash(javafx.event.ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/com/se/philips/dashb/Dashboard.fxml"));
        content.getChildren().removeAll();
        content.getChildren().setAll(root);
    }

    public void Students(javafx.event.ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/com/se/philips/dashb/StudentCreate.fxml"));
        content.getChildren().removeAll();
        content.getChildren().setAll(root);
    }

    public void Teachers(javafx.event.ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/com/se/philips/dashb/TeacherCreate.fxml"));
        content.getChildren().removeAll();
        content.getChildren().setAll(root);
    }


    public void Course(javafx.event.ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/com/se/philips/dashb/CourseDashboard.fxml"));
        content.getChildren().removeAll();
        content.getChildren().setAll(root);
    }

    public void Calendar (javafx.event.ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/com/se/philips/dashb/AdminCalendar.fxml"));
        content.getChildren().removeAll();
        content.getChildren().setAll(root);
    }

    public void Finance (javafx.event.ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/com/se/philips/dashb/FinanceDashboard.fxml"));
        content.getChildren().removeAll();
        content.getChildren().setAll(root);
    }

    public void Notice (javafx.event.ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/com/se/philips/dashb/NoticeDashboard.fxml"));
        content.getChildren().removeAll();
        content.getChildren().setAll(root);
    }

    public void Message (javafx.event.ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/com/se/philips/dashb/MessageDashboard.fxml"));
        content.getChildren().removeAll();
        content.getChildren().setAll(root);
    }

    public void Profile (javafx.event.ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/com/se/philips/dashb/AdminProfile.fxml"));
        content.getChildren().removeAll();
        content.getChildren().setAll(root);

    }

    @FXML
    private void LogOut() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/se/philips/dashb/LogOutDialog.fxml"));
        Parent dialogRoot = loader.load();

        // Get dialog controller and pass the main stage (admin dashboard window)
        LogOutDialogController controller = loader.getController();
        controller.setMainStage((Stage) logoutButton.getScene().getWindow());

        // Show dialog with transparent style
        Stage dialogStage = new Stage();
        dialogStage.initOwner(logoutButton.getScene().getWindow());
        dialogStage.initStyle(javafx.stage.StageStyle.TRANSPARENT);

        Scene scene = new Scene(dialogRoot);
        scene.setFill(null);
        dialogStage.setScene(scene);
        dialogStage.show();
    }




}
