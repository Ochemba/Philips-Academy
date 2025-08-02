package com.se.philips.AdminDashboard;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class LogOutDialogController {

    @FXML
    private StackPane dialogRoot;

    private Stage mainStage; // Reference to the main (admin) stage

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    @FXML
    private void handleCancel() {
        // Close only the dialog box
        ((Stage) dialogRoot.getScene().getWindow()).close();
    }

    @FXML
    private void handleConfirm() throws IOException {
        // Close the dialog
        Stage dialogStage = (Stage) dialogRoot.getScene().getWindow();
        dialogStage.close();

        // Close the admin dashboard
        if (mainStage != null) {
            mainStage.close();
        }

        // Open welcome page
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/se/philips/scenes/Welcome.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Welcome");
        stage.show();


    }
}
