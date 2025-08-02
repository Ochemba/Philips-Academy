//package com.se.philips.AdminDashboard.Payment.AdBox;
//
//import javafx.fxml.FXML;
//import javafx.scene.control.Alert;
//import javafx.scene.control.TextField;
//import javafx.stage.Stage;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.time.LocalDate;
//
//public class DialogBoxController {
//
//    @FXML private TextField titleField;
//    @FXML private TextField amountField;
//    @FXML private TextField PurposeField;
//
//    @FXML
//    private void handleCancel() {
//        ((Stage) titleField.getScene().getWindow()).close();
//    }
//
//    @FXML
//    private void handlePay() {
//        String title = titleField.getText().trim();
//        String amountText = amountField.getText().trim();
//        String Purpose = PurposeField.getText().trim();
//
//        if (title.isEmpty() || amountText.isEmpty()) {
//            showAlert(Alert.AlertType.ERROR, "All fields are required.");
//            return;
//        }
//
//        // Validate amount using regex: only positive numbers (optionally with 1-2 decimal places)
//        if (!amountText.matches("^\\d+(\\.\\d{1,2})?$")) {
//            showAlert(Alert.AlertType.ERROR, "Amount must be a valid number (e.g. 2500 or 123.45).");
//            return;
//        }
//
//        try {
//            double amount = Double.parseDouble(amountText);
//            String date = LocalDate.now().toString();
//
//            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:MainDB.db");
//                 PreparedStatement stmt = conn.prepareStatement(
//                         "INSERT INTO admin_bills (title, amount, purpose,paid_date) VALUES (?, ?, ?,?)")) {
//                stmt.setString(1, title);
//                stmt.setDouble(2, amount);
//                stmt.setString(3, Purpose);
//                stmt.setString(4, date);
//                stmt.executeUpdate();
//            }
//
//            showAlert(Alert.AlertType.INFORMATION, "Bill paid successfully.");
//            ((Stage) titleField.getScene().getWindow()).close(); // Close dialog
//
//        } catch (NumberFormatException e) {
//            showAlert(Alert.AlertType.ERROR, "Amount must be a number.");
//        } catch (SQLException e) {
//            showAlert(Alert.AlertType.ERROR, "Database error: " + e.getMessage());
//        }
//    }
//
//    private void showAlert(Alert.AlertType type, String msg) {
//        Alert alert = new Alert(type);
//        alert.setTitle(type == Alert.AlertType.INFORMATION ? "Success" : "Error");
//        alert.setHeaderText(null);
//        alert.setContentText(msg);
//        alert.showAndWait();
//    }
//}

package com.se.philips.AdminDashboard.Payment.AdBox;

import com.se.philips.AdminDashboard.FinanceDashboardController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DialogBoxController {

    @FXML private TextField titleField;
    @FXML private TextField amountField;
    @FXML private TextField PurposeField;

    private FinanceDashboardController dashboardController;

    public void setDashboardController(FinanceDashboardController controller) {
        this.dashboardController = controller;
    }

    @FXML
    private void handleCancel() {
        ((Stage) titleField.getScene().getWindow()).close();
    }

    @FXML
    private void handlePay() {
        String title = titleField.getText().trim();
        String amountText = amountField.getText().trim();
        String purpose = PurposeField.getText().trim();

        if (title.isEmpty() || amountText.isEmpty() || purpose.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "All fields are required.");
            return;
        }

        if (!amountText.matches("^\\d+(\\.\\d{1,2})?$")) {
            showAlert(Alert.AlertType.ERROR, "Amount must be a valid number (e.g. 2500 or 123.45).");
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);

            // ✅ Call method in FinanceDashboardController to save and refresh
            dashboardController.saveNewAdminPayment(title, amount, purpose);

            showAlert(Alert.AlertType.INFORMATION, "Bill paid successfully.");
            ((Stage) titleField.getScene().getWindow()).close();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Amount must be a number.");
        }
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(type == Alert.AlertType.INFORMATION ? "Success" : "Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}

