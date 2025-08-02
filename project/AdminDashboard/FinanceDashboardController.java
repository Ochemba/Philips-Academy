package com.se.philips.AdminDashboard;

import com.se.philips.AdminDashboard.Payment.AdBox.DialogBoxController;
import com.se.philips.AdminDashboard.Payment.AdminPayment;
import com.se.philips.AdminDashboard.Payment.StudentPayment;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.UUID;

public class FinanceDashboardController {

    @FXML private TextField searchField;

    // Student table
    @FXML private TableView<StudentPayment> studentPaymentTable;
    @FXML private TableColumn<StudentPayment, String> matricCol;
    @FXML private TableColumn<StudentPayment, String> nameCol;
    @FXML private TableColumn<StudentPayment, String> invoiceCol;
    @FXML private TableColumn<StudentPayment, String> amountCol;
    @FXML private TableColumn<StudentPayment, String> statusCol;
    @FXML private TableColumn<StudentPayment, String> dateCol;
    @FXML private TableColumn<StudentPayment, Void> studentActionsCol;

    private final ObservableList<StudentPayment> studentPayments = FXCollections.observableArrayList();

    // Admin table
    @FXML private TableView<AdminPayment> adminPaymentTable;
    @FXML private TableColumn<AdminPayment, String> titleNameCol;
    @FXML private TableColumn<AdminPayment, String> adminInvoiceCol;
    @FXML private TableColumn<AdminPayment, String> adminAmountCol;
    @FXML private TableColumn<AdminPayment, String> adminPurposeCol;
    @FXML private TableColumn<AdminPayment, String> adminDateCol;
    @FXML private TableColumn<AdminPayment, Void> adminActionsCol;

    private final ObservableList<AdminPayment> adminPayments = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Student table setup
        matricCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getMatricId()));
        nameCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getStudentName()));
        invoiceCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getInvoiceNumber()));
        amountCol.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getAmountPaid())));
        statusCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPaymentStatus()));
        dateCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPaymentDate()));
        setupStudentActionsColumn();


        // Admin table setup
        titleNameCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTitleName()));
        adminInvoiceCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getInvoiceNumber()));
        adminAmountCol.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getAmountPaid())));
        adminPurposeCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPaymentPurpose()));
        adminDateCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPaymentDate()));
        setupAdminActionsColumn();

        loadPaymentsFromSQLite();
    }

    private void loadPaymentsFromSQLite() {
        studentPayments.clear();
        adminPayments.clear();

        String studentSql = """
            SELECT Students.matricId, Students.firstName || ' ' || Students.lastName AS name,
                   student_payments.invoice_number, student_payments.amount_paid,
                   student_payments.status, student_payments.paid_date
            FROM student_payments
            JOIN Students ON student_payments.student_id = Students.Student_id
        """;

        String adminSql = "SELECT * FROM admin_bills";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:MainDB.db")) {

            // Load student payments
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(studentSql)) {
                while (rs.next()) {
                    studentPayments.add(new StudentPayment(
                            rs.getString("matricId"),
                            rs.getString("name"),
                            rs.getString("invoice_number"),
                            "", // balance not needed for admin view
                            rs.getDouble("amount_paid"),
                            rs.getString("status"),
                            rs.getString("paid_date")
                    ));
                }
                studentPaymentTable.setItems(studentPayments);
            }

            // Load admin payments
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(adminSql)) {
                while (rs.next()) {
                    adminPayments.add(new AdminPayment(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("invoice_number"),
                            rs.getDouble("amount"),
                            rs.getString("purpose"),
                            rs.getString("paid_date")
                    ));
                }
                adminPaymentTable.setItems(adminPayments);
            }

        } catch (SQLException e) {
            showAlert("Error loading payments: " + e.getMessage());
        }
    }

    private void setupStudentActionsColumn() {
        studentActionsCol.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            private final HBox hbox = new HBox(10, editBtn, deleteBtn);

            {
                editBtn.setOnAction(event -> {
                    StudentPayment selected = getTableView().getItems().get(getIndex());
                    editStudentPayment(selected);
                });

                deleteBtn.setOnAction(event -> {
                    StudentPayment selected = getTableView().getItems().get(getIndex());
                    deleteStudentPayment(selected);
                });

                editBtn.setStyle("-fx-background-color: #F59E0B;");
                deleteBtn.setStyle("-fx-background-color: #F08080;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : hbox);
            }
        });
    }

    public void editStudentPayment(StudentPayment payment) {
        showAlert("Edit clicked for invoice: " + payment.getInvoiceNumber());
        // TODO: Show edit popup
    }

    public void deleteStudentPayment(StudentPayment payment) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Delete this payment?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try (Connection conn = DriverManager.getConnection("jdbc:sqlite:MainDB.db");
                     PreparedStatement stmt = conn.prepareStatement("DELETE FROM student_payments WHERE invoice_number = ?")) {
                    stmt.setString(1, payment.getInvoiceNumber());
                    stmt.executeUpdate();
                    loadPaymentsFromSQLite();
                } catch (SQLException e) {
                    showAlert("Failed to delete payment: " + e.getMessage());
                }
            }
        });
    }

    private void setupAdminActionsColumn() {
        adminActionsCol.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            private final HBox hbox = new HBox(10, editBtn, deleteBtn);

            {
                editBtn.setOnAction(event -> {
                    AdminPayment selected = getTableView().getItems().get(getIndex());
                    editAdminPayment(selected);
                });

                deleteBtn.setOnAction(event -> {
                    AdminPayment selected = getTableView().getItems().get(getIndex());
                    deleteAdminPayment(selected);
                });

                editBtn.setStyle("-fx-background-color: #F59E0B;");
                deleteBtn.setStyle("-fx-background-color: #F08080;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : hbox);
            }
        });
    }

    public void editAdminPayment(AdminPayment payment) {
        showAlert("Edit clicked for invoice: " + payment.getInvoiceNumber());
    }

    public void deleteAdminPayment(AdminPayment payment) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Delete this admin bill?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try (Connection conn = DriverManager.getConnection("jdbc:sqlite:MainDB.db");
                     PreparedStatement stmt = conn.prepareStatement("DELETE FROM admin_bills WHERE id = ?")) {
                    stmt.setInt(1, payment.getId());
                    stmt.executeUpdate();
                    loadPaymentsFromSQLite();
                } catch (SQLException e) {
                    showAlert("Failed to delete bill: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    void openPayBillDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/se/philips/dashb/DialogBox.fxml"));
            Parent root = loader.load();
            DialogBoxController dialogController = loader.getController();
            dialogController.setDashboardController(this);

            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setTitle("Pay Bill");
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();

        } catch (IOException e) {
            showAlert("Failed to load dialog: " + e.getMessage());
        }
    }

    public void saveNewAdminPayment(String title, double amount, String purpose) {
        String invoiceNumber = "INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String date = LocalDate.now().toString();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:MainDB.db");
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO admin_bills (title, invoice_number, amount, purpose, paid_date) VALUES (?, ?, ?, ?, ?)")) {
            stmt.setString(1, title);
            stmt.setString(2, invoiceNumber);
            stmt.setDouble(3, amount);
            stmt.setString(4, purpose);
            stmt.setString(5, date);
            stmt.executeUpdate();
            loadPaymentsFromSQLite();
        } catch (SQLException e) {
            showAlert("Failed to save admin payment: " + e.getMessage());
        }
    }

    @FXML
    private void handleSearch() {
        String term = searchField.getText().toLowerCase();
        ObservableList<StudentPayment> filtered = studentPayments.filtered(p ->
                p.getInvoiceNumber().toLowerCase().contains(term) ||
                        p.getMatricId().toLowerCase().contains(term) ||
                        p.getStudentName().toLowerCase().contains(term)
        );
        studentPaymentTable.setItems(filtered);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Finance Dashboard");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
