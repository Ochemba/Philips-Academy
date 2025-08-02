package com.se.philips.AdminDashboard;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;

public class StudentCreateController {

    @FXML private TextField lastNameField;
    @FXML private TextField firstNameField;
    @FXML private TextField matricIdField;
    @FXML private TextField levelField;
    @FXML private TextField departmentField;
    @FXML private TextField genderField;
    @FXML private PasswordField passwordField;

    @FXML private TableView<StudentCreate> tableView;
    @FXML private TableColumn<StudentCreate, String> lastNameCol;
    @FXML private TableColumn<StudentCreate, String> firstNameCol;
    @FXML private TableColumn<StudentCreate, String> matricIdCol;
    @FXML private TableColumn<StudentCreate, String> levelCol;
    @FXML private TableColumn<StudentCreate, String> departmentCol;
    @FXML private TableColumn<StudentCreate, String> genderCol;
    @FXML private TableColumn<StudentCreate, String> passwordCol;

    private final ObservableList<StudentCreate> studentList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        lastNameCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getLastName()));
        firstNameCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getFirstName()));
        matricIdCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getMatricId()));
        levelCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getLevel()));
        departmentCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDepartment()));
        genderCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getGender()));
        passwordCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPassword()));
        loadFromSQLite();
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateForm(newSelection);
            }
        });
    }

    @FXML
    private void onCreate() {
        if (lastNameField.getText().isEmpty() || matricIdField.getText().isEmpty() || passwordField.getText().isEmpty() || levelField.getText().isEmpty() || departmentField.getText().isEmpty() || genderField.getText().isEmpty()) {
            showAlert("Missing Required Fields", "Missing Credentials.");
            return;
        }

        String url = "jdbc:sqlite:MainDB.db";
        String sql = "INSERT INTO Students(lastName, firstName, matricId, level, department, gender, password) VALUES(?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, lastNameField.getText());
            pstmt.setString(2, firstNameField.getText());
            pstmt.setString(3, matricIdField.getText());
            pstmt.setString(4, levelField.getText());
            pstmt.setString(5, departmentField.getText());
            pstmt.setString(6, genderField.getText());
            pstmt.setString(7, passwordField.getText());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Created");
        onClear();
        loadFromSQLite();
    }




    @FXML
    private void onUpdate() {
        String url = "jdbc:sqlite:MainDB.db";
        String sql = "UPDATE Students SET lastName = ?, firstName = ?, level = ?, department = ?, gender = ?, password = ? WHERE matricId = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, lastNameField.getText());
            pstmt.setString(2, firstNameField.getText());
            pstmt.setString(3, levelField.getText());
            pstmt.setString(4, departmentField.getText());
            pstmt.setString(5, genderField.getText());
            pstmt.setString(6, passwordField.getText());

            // matricId in WHERE clause
            pstmt.setString(7, matricIdField.getText());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Update successful");
            } else {
                System.out.println("No record found with that Matric ID");
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        onClear();
        loadFromSQLite();

    }

    @FXML
    private void onDelete() {
        String url = "jdbc:sqlite:MainDB.db";
        String sql = "DELETE FROM Students WHERE matricId = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, matricIdField.getText());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Record deleted successfully.");
            } else {
                System.out.println("No record found with that Matric ID.");
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        onClear();
        loadFromSQLite();

    }

    @FXML
    public void onClear() {
        lastNameField.clear();
        firstNameField.clear();
        matricIdField.clear();
        levelField.clear();
        departmentField.clear();
        genderField.clear();
        passwordField.clear();
        tableView.getSelectionModel().clearSelection();
        System.out.println("Cleared");
    }

    @FXML
    private void onClearAll() {
        onClear(); // Clear fields and selection
        studentList.clear(); // Also clear the entire table
        System.out.println("clear All");
    }


    private void populateForm(StudentCreate student) {
        lastNameField.setText(student.getLastName());
        firstNameField.setText(student.getFirstName());
        matricIdField.setText(student.getMatricId());
        levelField.setText(student.getLevel());
        departmentField.setText(student.getDepartment());
        genderField.setText(student.getGender());
        passwordField.setText(student.getPassword());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadFromSQLite() {
        String url = "jdbc:sqlite:MainDB.db"; // Adjust this
        String query = "SELECT * FROM Students";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            studentList.clear();

            while (rs.next()) {
                StudentCreate student = new StudentCreate(
                        rs.getString("lastName"),
                        rs.getString("firstName"),
                        rs.getString("matricId"),
                        rs.getString("level"),
                        rs.getString("department"),
                        rs.getString("gender"),
                        rs.getString("password")
                );

                studentList.add(student);
            }

            tableView.setItems(studentList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public ObservableList<StudentCreate> getStudentList() {
        return studentList;
    }
}


