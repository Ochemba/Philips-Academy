package com.se.philips.STUDENT;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.UUID;

public class StudentPaymentController {

    @FXML private TextField matricIdField;
    @FXML private TextField amountField;
    @FXML private TextField purposeField;
    @FXML private Label balanceLabel;
    @FXML private Label statusLabel;
    @FXML private TableView<Payment> paymentTable;
    @FXML private TableColumn<Payment, String> dateCol;
    @FXML private TableColumn<Payment, Double> amountCol;
    @FXML private TableColumn<Payment, String> purposeCol;
    @FXML private TableColumn<Payment, String> statusCol;
    @FXML private TableColumn<Payment, String> invoiceCol;
    @FXML private TableColumn<Payment, Double> balanceCol;

    private final ObservableList<Payment> paymentList = FXCollections.observableArrayList();
    private static final double TOTAL_FEE = 500000;

    @FXML
    public void initialize() {
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        purposeCol.setCellValueFactory(new PropertyValueFactory<>("purpose"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        invoiceCol.setCellValueFactory(new PropertyValueFactory<>("invoiceNumber"));
        balanceCol.setCellValueFactory(new PropertyValueFactory<>("balance"));

        paymentTable.setItems(paymentList);
        loadPaymentHistory();
    }

    @FXML
    private void handlePayNow() {
        String matricId = matricIdField.getText().trim();
        String amountText = amountField.getText().trim();
        String purpose = purposeField.getText().trim();

        if (matricId.isEmpty() || amountText.isEmpty() || purpose.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "All fields are required.");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid amount.");
            return;
        }

        String date = LocalDate.now().toString();
        String invoiceNumber = generateInvoiceNumber();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:MainDB.db")) {

            int studentId = -1;
            String lookupQuery = "SELECT student_id FROM students WHERE matricId = ?";
            try (PreparedStatement lookup = conn.prepareStatement(lookupQuery)) {
                lookup.setString(1, matricId);
                ResultSet rs = lookup.executeQuery();
                if (rs.next()) {
                    studentId = rs.getInt("student_id");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Matric ID not found.");
                    return;
                }
            }

            double totalPaid = getTotalPaid(conn, studentId);
            double newBalance = TOTAL_FEE - (totalPaid + amount);
            double updatedTotal = totalPaid + amount;

            String status;
            if (updatedTotal >= TOTAL_FEE) {
                status = "Sufficient";
            } else if (updatedTotal >= TOTAL_FEE * 0.4) {
                status = "Partial";
            } else {
                status = "Insufficient";
            }

            String insertSQL = "INSERT INTO student_payments (student_id, matricId, amount_paid, total_fee, balance, status, payment_purpose, paid_date, invoice_number) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertSQL)) {
                stmt.setInt(1, studentId);
                stmt.setString(2, matricId);
                stmt.setDouble(3, amount);
                stmt.setDouble(4, TOTAL_FEE);
                stmt.setDouble(5, newBalance);
                stmt.setString(6, status);
                stmt.setString(7, purpose);
                stmt.setString(8, date);
                stmt.setString(9, invoiceNumber);
                stmt.executeUpdate();
            }

            // Add to history table
            Payment payment = new Payment(date, amount, purpose, status, invoiceNumber, newBalance);
            paymentList.add(payment);

            updateBalanceAndStatus();
            clearForm();
            showAlert(Alert.AlertType.INFORMATION, "Payment successful. Invoice: " + invoiceNumber);

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database error: " + e.getMessage());
        }
    }

    private void updateBalanceAndStatus() {
        double totalPaid = paymentList.stream().mapToDouble(Payment::getAmount).sum();
        double balance = TOTAL_FEE - totalPaid;
        balanceLabel.setText("₦" + balance);

        if (totalPaid >= TOTAL_FEE) {
            statusLabel.setText("Sufficient");
        } else if (totalPaid >= TOTAL_FEE * 0.4) {
            statusLabel.setText("Partial");
        } else {
            statusLabel.setText("Insufficient");
        }
    }

    private void clearForm() {
        amountField.clear();
        purposeField.clear();
    }

    private double getTotalPaid(Connection conn, int studentId) throws SQLException {
        String sql = "SELECT SUM(amount_paid) FROM student_payments WHERE student_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getDouble(1) : 0.0;
        }
    }

    private String generateInvoiceNumber() {
        return "INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.showAndWait();
    }

    private void loadPaymentHistory() {
        String matricId = StudentLoginSession.getMatricId(); // ✅ Get from session
        if (matricId == null || matricId.isEmpty()) return;

        //paymentList.clear(); // Optional: clear old entries

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:MainDB.db")) {
            String sql = "SELECT * FROM student_payments WHERE matricId = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, matricId);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    String date = rs.getString("paid_date");
                    double amount = rs.getDouble("amount_paid");
                    String purpose = rs.getString("payment_purpose");
                    String status = rs.getString("status");
                    String invoice = rs.getString("invoice_number");
                    double balance = rs.getDouble("balance");

                    paymentList.add(new Payment(date, amount, purpose, status, invoice, balance));
                }
            }

            updateBalanceAndStatus(); // Update label or summary
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error loading payment history: " + e.getMessage());
        }
    }



    public static class Payment {
        private final String date;
        private final double amount;
        private final String purpose;
        private final String status;
        private final String invoiceNumber;
        private final Double balance;

        public Payment(String date, double amount, String purpose, String status, String invoiceNumber, Double balance) {
            this.date = date;
            this.amount = amount;
            this.purpose = purpose;
            this.status = status;
            this.invoiceNumber = invoiceNumber;
            this.balance = balance;
        }

        public String getDate() { return date; }
        public double getAmount() { return amount; }
        public String getPurpose() { return purpose; }
        public String getStatus() { return status; }
        public String getInvoiceNumber() { return invoiceNumber; }
        public Double getBalance() { return balance; }
    }
}
