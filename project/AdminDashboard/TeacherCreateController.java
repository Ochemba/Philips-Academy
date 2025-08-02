package com.se.philips.AdminDashboard;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;

public class TeacherCreateController {

    @FXML private TextField IDField;
    @FXML private TextField lastNameField;
    @FXML private TextField firstNameField;
    @FXML private TextField genderField;
    @FXML private PasswordField passwordField;
    @FXML private TextField phoneNumberField;
    @FXML private TextField departmentField;
    @FXML private TextField courseAssignedField;
    @FXML private TextField levelAssignedField;



    @FXML private TableView<TeacherCreate> tableView;
    @FXML private TableColumn<TeacherCreate, String> ID;
    @FXML private TableColumn<TeacherCreate, String> lastNameCol;
    @FXML private TableColumn<TeacherCreate, String> firstNameCol;
    @FXML private TableColumn<TeacherCreate, String> genderCol;
    @FXML private TableColumn<TeacherCreate, String> passwordCol;
    @FXML private TableColumn<TeacherCreate, String> phoneNumberCol;
    @FXML private TableColumn<TeacherCreate, String> levelAssignedCol;
    @FXML private TableColumn<TeacherCreate, String> departmentCol;
    @FXML private TableColumn<TeacherCreate, String> courseAssignedCol;
    

    private final ObservableList<TeacherCreate> TeacherList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        ID.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getID()));
        lastNameCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getLastName()));
        firstNameCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getFirstName()));
        genderCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getGender()));
        passwordCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPassword()));
        phoneNumberCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPhoneNumber()));
        departmentCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDepartment()));
        levelAssignedCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getLevelAssigned()));
        courseAssignedCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCourseAssigned()));

        loadFromSQLite();
        tableView.setItems(TeacherList);

        // Populate form when a row is selected
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateForm(newSelection);
            }
        });
    }

    @FXML
    private void onCreate() {
        if ( IDField.getText().isEmpty() || lastNameField.getText().isEmpty() || firstNameField.getText().isEmpty() || passwordField.getText().isEmpty() || levelAssignedField.getText().isEmpty() || departmentField.getText().isEmpty() || genderField.getText().isEmpty() || phoneNumberField.getText().isEmpty() || courseAssignedField.getText().isEmpty()) {
            showAlert("Missing Required Fields", "Missing Credentials.");
            return;
        }

        String url = "jdbc:sqlite:MainDB.db";
        String sql = "INSERT INTO teachers (staff_id, last_name, first_name, gender,password,phone_number, department,course_assigned,level_assigned) VALUES( ?, ?, ?, ?, ?, ?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, IDField.getText());
            pstmt.setString(2, lastNameField.getText());
            pstmt.setString(3, firstNameField.getText());
            pstmt.setString(4, genderField.getText());
            pstmt.setString(5, passwordField.getText());
            pstmt.setString(6, phoneNumberField.getText());
            pstmt.setString(7, departmentField.getText());
            pstmt.setString(8, levelAssignedField.getText());
            pstmt.setString(9, courseAssignedField.getText());
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
        String sql = "UPDATE teachers SET  last_name = ?, first_name = ?,gender = ?, password = ?,phone_number = ?, department = ?, course_assigned = ?, level_assigned = ? WHERE staff_id = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {


            pstmt.setString(1, lastNameField.getText());
            pstmt.setString(2, firstNameField.getText());
            pstmt.setString(3, genderField.getText());
            pstmt.setString(4, passwordField.getText());
            pstmt.setString(5, phoneNumberField.getText());
            pstmt.setString(6, departmentField.getText());
            pstmt.setString(7, levelAssignedField.getText());
            pstmt.setString(8, courseAssignedField.getText());

            // matricId in WHERE clause
            pstmt.setString(9, IDField.getText());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Update successful");
            } else {
                System.out.println("No record found with that  ID");
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
        String sql = "DELETE FROM teachers WHERE staff_id = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, IDField.getText());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Record deleted successfully.");
            } else {
                System.out.println("No record found with that  ID.");
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        onClear();
        loadFromSQLite();

    }


    @FXML
    public void onClear() {
        IDField.clear();
        lastNameField.clear();
        firstNameField.clear();
        genderField.clear();
        passwordField.clear();
        phoneNumberField.clear();
        departmentField.clear();
        courseAssignedField.clear();
        levelAssignedField.clear();
        tableView.getSelectionModel().clearSelection();
        System.out.println("Cleared");
    }

    @FXML
    private void onClearAll() {
        onClear(); // Clear fields and selection
        TeacherList.clear(); // Also clear the entire table
        System.out.println("clear All");
    }


    private void populateForm(TeacherCreate teacher) {
        IDField.setText(teacher.getID());
        lastNameField.setText(teacher.getLastName());
        firstNameField.setText(teacher.getFirstName());
        genderField.setText(teacher.getGender());
        passwordField.setText(teacher.getPassword());
        phoneNumberField.setText(teacher.getPhoneNumber());
        departmentField.setText(teacher.getDepartment());
        courseAssignedField.setText(teacher.getCourseAssigned());
        levelAssignedField.setText(teacher.getLevelAssigned());

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
        String query = "SELECT * FROM teachers";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            TeacherList.clear();

            while (rs.next()) {
                TeacherCreate teacher = new TeacherCreate(
                        rs.getString("last_name"),
                        rs.getString("first_name"),
                        rs.getString("staff_id"),
                        rs.getString("phone_number"),
                        rs.getString("department"),
                        rs.getString("gender"),
                        rs.getString("course_assigned"),
                        rs.getString("level_assigned"),
                        rs.getString("password")
                );

                TeacherList.add(teacher);
            }

            tableView.setItems(TeacherList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<TeacherCreate> getTeacherList() {
        return TeacherList;
    }
}


